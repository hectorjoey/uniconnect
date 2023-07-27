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
import hector.developers.uniconnectapp.adapter.OperatorsAdapter;
import hector.developers.uniconnectapp.model.Operators;


public class OperatorActivity extends AppCompatActivity {
    public static final String CATEGORY_ID = "hector.developers.uniconnect.CATEGORY_ID";

    private OperatorsAdapter adapter;
    private ArrayList<Operators> dataList;
    private RecyclerView recyclerView;

    String mtnLogo = "https://images.africanfinancials.com/63be93ed-ng-mtn-logo.png";
    String airtelLogo = "https://play-lh.googleusercontent.com/uFg3zOsnGZkIrswmvXyFYhoF3gC4tv0ovFZv0zisJFQ2DZqJyh9SUGrK6D-Tnn1lGqc";
    String gloLogo = "https://www.wantedinafrica.com/i/preview/storage/uploads/2012/05/f49ifkmnt.jpg";
    String etisalatLogo = "https://dailypost.ng/wp-content/uploads/2020/03/9mobile-logo_2-scaled.jpg";
    String visafoneLogo = "https://pbs.twimg.com/profile_images/3749738718/a5ab3444e5a435ff7f5de14be2d8b5e4_400x400.png";
    String smileLogo = "https://media.premiumtimesng.com/wp-content/files/2021/04/Telecoms.png";

    String category = "pctg_z6dJLqhj85UeBhd7kCCZZX";
    String mtnOperator = "op_tHvaAHp85mTsRtEU2yqxLM";
    String airtelOperator = "op_KiaNQPRBU2tLC3wpvNjgsh";
    String gloOperator = "op_Jto3E4YkFucZLTVwZftJCj";
    String etisalatOperator = "op_YLrkGrJmiN9a3MBBhtDKtn";
    String visafoneOperator = "op_zQG65u4Ax7HyXDHyJaHCLK";
    String smileOperator = "op_4NJKCdevbLiZPCxdrRmmCX";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        AndroidNetworking.get("https://api.blochq.io/v1/bills/operators?bill=telco")
                .addHeaders("Authorization", "Bearer sk_test_648dec6240ffc21ad42aa403648dec6240ffc21ad42aa404")
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
                                Operators operators = new Operators();
                                operators.setId(jsonObject.optString("id"));
                                if (operators.getId().equals(mtnOperator)) {
                                    operators.setImgUrl(mtnLogo);
                                }
                                if (operators.getId().equals(airtelOperator)) {
                                    operators.setImgUrl(airtelLogo);
                                }
                                if (operators.getId().equals(etisalatOperator)) {
                                    operators.setImgUrl(etisalatLogo);
                                }
                                if (operators.getId().equals(visafoneOperator)) {
                                    operators.setImgUrl(visafoneLogo);
                                }
                                if (operators.getId().equals(gloOperator)) {
                                    operators.setImgUrl(gloLogo);
                                }
                                if (operators.getId().equals(smileOperator)) {
                                    operators.setImgUrl(smileLogo);
                                }
                                if (!operators.getId().equals(visafoneOperator)) {
                                    operators.setId(jsonObject.getString("id"));
                                    operators.setDesc(jsonObject.getString("desc"));
                                    operators.setName(jsonObject.getString("name"));
                                    operators.setSector(jsonObject.getString("sector"));


                                    String sectors = jsonObject.getString("sector");
                                    saveSector(sectors);

                                    dataList.add(operators);
                                }


                            }

                            adapter = new OperatorsAdapter(dataList, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(OperatorActivity.this, "Network error occurred!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }




    public void saveSector(String sectors) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("sectors", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("sectors", sectors + "");
        edit.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        final ProgressDialog progressDialog = new ProgressDialog(this);

        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
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