package hector.developers.uniconnectapp.list;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import hector.developers.uniconnectapp.adapter.AirtimeAdapter;
import hector.developers.uniconnectapp.model.Airtime;


public class AirtimeListActivity extends AppCompatActivity {

    private AirtimeAdapter adapter;
    private ArrayList<Airtime> airtimeArrayList;
    private RecyclerView recyclerView;

    String id, desc, sector;

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
        setContentView(R.layout.activity_airtime_list);

            recyclerView = findViewById(R.id.airRecyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            id = getIntent().getStringExtra("id");
            desc = getIntent().getStringExtra("desc");
            sector = getIntent().getStringExtra("sector");

            loadData();
        }


        private void loadData () {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();
            AndroidNetworking.get("https://api.blochq.io/v1/bills/operators/" + id + "/products?bill=telco")
                    .addHeaders("Authorization", "Bearer sk_live_648dec6240ffc21ad42aa3ff648dec6240ffc21ad42aa400")
                    .setTag("test")
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            airtimeArrayList = new ArrayList<>();

                            try {
                                JSONArray dataArray = response.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    JSONObject metaObject = dataObject.optJSONObject("meta");

                                    Airtime airtime = new Airtime();
                                    airtime.setCategory(dataObject.optString("category"));
                                    airtime.setOperator(dataObject.optString("operator"));
                                    airtime.setId(dataObject.getString("id"));
                                    airtime.setFee_type(dataObject.optString("fee_type"));
                                    if (airtime.getOperator().equals(mtnOperator)) {
                                        airtime.setImgUrl(mtnLogo);
                                    }

                                    if (airtime.getOperator().equals(airtelOperator)) {
                                        airtime.setImgUrl(airtelLogo);
                                    }

                                    if (airtime.getOperator().equals(gloOperator)) {
                                        airtime.setImgUrl(gloLogo);
                                    }
                                    if (airtime.getOperator().equals(etisalatOperator)) {
                                        airtime.setImgUrl(etisalatLogo);
                                    }
                                    if (airtime.getOperator().equals(visafoneOperator)) {
                                        airtime.setImgUrl(visafoneLogo);
                                    }
                                    if (airtime.getOperator().equals(smileOperator)) {
                                        airtime.setImgUrl(smileLogo);
                                    }
                                    if (airtime.getCategory().equals(category)) {
                                        airtime.setDesc(desc);
                                        airtime.setName(dataObject.getString("name"));

                                        if (metaObject != null) {
                                            airtime.setMinimum_fee(metaObject.optString("minimum_fee"));
                                            airtime.setMaximum_fee(metaObject.optString("maximum_fee"));
                                        }
                                        airtimeArrayList.add(airtime);
                                    }
                                }

                                adapter = new AirtimeAdapter(airtimeArrayList, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Network error occurred!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onDestroy () {
            super.onDestroy();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        }

        @Override
        public void onBackPressed () {
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