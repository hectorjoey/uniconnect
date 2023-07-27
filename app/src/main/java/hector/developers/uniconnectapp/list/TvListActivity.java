package hector.developers.uniconnectapp.list;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.activities.MainActivity;
import hector.developers.uniconnectapp.adapter.TvAdapter;
import hector.developers.uniconnectapp.model.Tv;


public class TvListActivity extends AppCompatActivity {

    private TvAdapter adapter;
    private ArrayList<Tv> tvlist;
    private RecyclerView recyclerView;

    String id, desc, sector;

    String dstvLogo = "https://assets.sunnewsonline.com/2017/01/zzExdJhz-DSTV-1.jpg";
    String gotvLogo = "https://assets.sunnewsonline.com/2021/05/GOtv-1.jpg";
    String startimesLogo = "https://cdn.punchng.com/wp-content/uploads/2016/07/10202202/startimes-logo.jpg";

    String dstv = "op_7MWH3mdtKzAEcZRFXvxmNN";
    String gotv = "op_uD3ricdTkcDHz7XcKTydTn";
    String startimes = "op_ZbLWKcuM8wBdoEEeieQqeU";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_list);

        recyclerView = findViewById(R.id.tvRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        id = getIntent().getStringExtra("id");
        desc = getIntent().getStringExtra("desc");
        sector=  getIntent().getStringExtra("sector");

        System.out.println("TV LIst :::::  sector ::: " + sector);


        saveSectors(sector);


        loadData();
    }

    private void saveSectors(String sector) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("sectors", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("sectors", sector + "");
        edit.apply();
    }


    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        AndroidNetworking.get("https://api.blochq.io/v1/bills/operators/" + id + "/products?bill=television")
                .addHeaders("Authorization", "Bearer sk_test_648dec6240ffc21ad42aa403648dec6240ffc21ad42aa404")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        tvlist = new ArrayList<>();

                        try {
                            JSONArray dataArray = response.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                JSONObject metaObject = jsonObject.optJSONObject("meta");

                                Tv tv = new Tv();
                                tv.setOperator(jsonObject.optString("operator"));
                                tv.setId(jsonObject.getString("id"));
                                tv.setName(jsonObject.getString("name"));
                                tv.setFee_type(jsonObject.getString("fee_type"));
                                if (id.equals(dstv)) {
                                    tv.setImgUrl(dstvLogo);
                                }
                                if (id.equals(gotv)) {
                                    tv.setImgUrl(gotvLogo);

                                }
                                if (id.equals(startimes)) {
                                    tv.setImgUrl(startimesLogo);
                                }


                                if (metaObject != null) {
                                    tv.setAmount(metaObject.optString("fee"));
                                }

                                tvlist.add(tv);
                            }

                            adapter = new TvAdapter(tvlist, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(TvListActivity.this, "Network error occurred!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to go back?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked "Yes," perform the desired action
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Close the activity
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // User clicked "No," dismiss the dialog and continue with the activity
            dialog.dismiss();
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}