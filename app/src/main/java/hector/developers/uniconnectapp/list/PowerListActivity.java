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
import hector.developers.uniconnectapp.adapter.PowerAdapter;
import hector.developers.uniconnectapp.model.Power;


public class PowerListActivity extends AppCompatActivity {
    public static final String CATEGORY_ID = "hector.developers.uniconnect.CATEGORY_ID";

    private PowerAdapter adapter;
    private ArrayList<Power> dataList;
    private RecyclerView recyclerView;

    String aedcLogo = "https://fmic.gov.ng/wp-content/uploads/2016/12/Leading-reporters-AEDC.jpg";
    String ekoLogo = "https://pbs.twimg.com/media/ET4TUWEX0AAnqqX.jpg";
    String ikejaLogo = "https://tribuneonlineng.com/wp-content/uploads/2017/03/IKEDC.jpg";
    String enuguLogo = "https://businesspost.ng/wp-content/uploads/2021/12/Enugu-Disco.jpg";
    String ibadanLogo = "https://gazettengr.com/wp-content/uploads/IMG_1955.jpg";
    String kadunaLogo = "https://consciencetriumphs.org.ng/wp-content/uploads/2019/10/KAEDCO.jpg";
    String beninLogo = "https://beninelectric.com/wp-content/uploads/2022/10/BEDC-Logo-new-dark-1.png";
    String josLogo = "https://pbs.twimg.com/profile_images/898118928404295681/qqSdt2Ir_400x400.jpg";
    String aesLogo = "https://w7.pngwing.com/pngs/184/516/png-transparent-aes-corporation-company-power-station-electric-power-industry-aes-logo-text-distribution-business.png";
    String abaLogo = "https://geometricpower.com/wp-content/uploads/2022/09/APLE-Logo.png";
    String kanoLogo = "https://www.kedco.ng/assets/images/Kedco%20Logo%20web.png";
    String phLogo = "https://cdn.punchng.com/wp-content/uploads/2022/02/06072227/Port-Harcourt-Electricity-Distribution-Company-PHED.png";

    String aedc = "op_4iG4k2F6gAiotGfLfCrA67";
    String ekodc = "op_MzqRpUo2NchxZt4G56RVPf";
    String ikeja = "op_iZMtH6S6PxG3A7T9geNXqV";
    String enugu = "op_82CL7zK9UTzNaBwDBr69EF";
    String ibadan = "op_Aa6nzucgFck5nfqtcHHb4t";
    String kaduna = "op_qft8zY6VbGpqQ5dkskeAJY";
    String benin = "op_U8dSC7GQtBABNcBSVphLSC";
    String aba = "op_QkkGbGQGyQhKn48N9We3Mg";
    String jos = "op_NuiVktEQPHNaZeAwn9GJn5";
    String kano = "op_xdS33jqYHKbCBgEwW8qbT7";
    String ph = "op_ZYETpzSYgPHVZ8YJPjKcVh";
    String aesJos = "op_GAoU3ciaXyUDrcQ8FhNF7L";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_list);
        recyclerView = findViewById(R.id.powerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData();
    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        AndroidNetworking.get("https://api.blochq.io/v1/bills/operators?bill=electricity")
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
                                JSONObject metaObject = jsonObject.optJSONObject("meta");

                                Power power = new Power();
//                                power.setCategory(jsonObject.optString("category"));
                                power.setOperator(jsonObject.optString("operator"));
                                power.setId(jsonObject.getString("id"));

                                if (power.getId().equals(aedc)) {
                                    power.setImgUrl(aedcLogo);
                                }
                                if (power.getId().equals(ekodc)) {
                                    power.setImgUrl(ekoLogo);
                                }
                                if (power.getId().equals(ikeja)) {
                                    power.setImgUrl(ikejaLogo);
                                }
                                if (power.getId().equals(enugu)) {
                                    power.setImgUrl(enuguLogo);
                                }
                                if (power.getId().equals(kaduna)) {
                                    power.setImgUrl(kadunaLogo);
                                }
                                if (power.getId().equals(ibadan)) {
                                    power.setImgUrl(ibadanLogo);
                                }
                                if (power.getId().equals(benin)) {
                                    power.setImgUrl(beninLogo);
                                }
                                if (power.getId().equals(aba)) {
                                    power.setImgUrl(abaLogo);
                                }
                                if (power.getId().equals(jos)) {
                                    power.setImgUrl(josLogo);
                                }
                                if (power.getId().equals(kano)) {
                                    power.setImgUrl(kanoLogo);
                                }
                                if (power.getId().equals(ph)) {
                                    power.setImgUrl(phLogo);
                                }
                                if (power.getId().equals(aesJos)) {
                                    power.setImgUrl(aesLogo);
                                }
                                power.setId(jsonObject.getString("id"));
                                power.setDesc(jsonObject.getString("desc"));
                                power.setName(jsonObject.getString("name"));
                                power.setSector(jsonObject.getString("sector"));

                                dataList.add(power);
                            }

                            adapter = new PowerAdapter(dataList, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(PowerListActivity.this, "Network error occurred!", Toast.LENGTH_SHORT).show();
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