package hector.developers.uniconnectapp.list;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

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
import hector.developers.uniconnectapp.adapter.UniOperatorsAdapter;
import hector.developers.uniconnectapp.model.UniOperators;


public class UniOperatorActivity extends AppCompatActivity {

    private UniOperatorsAdapter adapter;
    private ArrayList<UniOperators> dataList;
    private RecyclerView recyclerView;

    String dstvLogo = "https://assets.sunnewsonline.com/2017/01/zzExdJhz-DSTV-1.jpg";
    String gotvLogo = "https://assets.sunnewsonline.com/2021/05/GOtv-1.jpg";
    String startimesLogo = "https://cdn.punchng.com/wp-content/uploads/2016/07/10202202/startimes-logo.jpg";

    String dstv = "op_7MWH3mdtKzAEcZRFXvxmNN";
    String gotv = "op_uD3ricdTkcDHz7XcKTydTn";
    String startimes = "op_ZbLWKcuM8wBdoEEeieQqeU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uni_operator);

        recyclerView = findViewById(R.id.uniRecyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadData();
    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        AndroidNetworking.get("https://api.blochq.io/v1/bills/operators?bill=television")
                .addHeaders("Authorization", "Bearer sk_live_648dec6240ffc21ad42aa3ff648dec6240ffc21ad42aa400")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dataList = new ArrayList<>();

                        try {
                            JSONArray dataArray = response.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                UniOperators operators = new UniOperators();
                                operators.setId(jsonObject.optString("id"));
                                operators.setName(jsonObject.optString("name"));
                                operators.setDesc(jsonObject.optString("desc"));
                                operators.setSector(jsonObject.optString("sector"));

                                if (operators.getId().equals(dstv)) {
                                    operators.setImgUrl(dstvLogo);
                                } else if (operators.getId().equals(gotv)) {
                                    operators.setImgUrl(gotvLogo);
                                } else if (operators.getId().equals(startimes)) {
                                    operators.setImgUrl(startimesLogo);
                                }
                                dataList.add(operators);
                            }

                            adapter = new UniOperatorsAdapter(dataList, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                            System.out.println(dataList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.getMessage());
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