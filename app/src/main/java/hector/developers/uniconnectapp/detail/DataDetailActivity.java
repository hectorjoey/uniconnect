package hector.developers.uniconnectapp.detail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.activities.MainActivity;
import hector.developers.uniconnectapp.model.MobileData;
import hector.developers.uniconnectapp.pay.DataPayActivity;
import hector.developers.uniconnectapp.utils.Util;

public class DataDetailActivity extends AppCompatActivity {

    TextView detailName, detailValue, detailExpiry, detailAmount;
    TextInputEditText mPhone;
    String sector, mProduct_id, id, product_id, operator_id, mOperator_id;
    Button mBuy;

    ProgressDialog progressDialog;

    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);
        detailName = findViewById(R.id.detailName);
        detailAmount = findViewById(R.id.detailsAmount);
        detailExpiry = findViewById(R.id.detailsExpiry);
        detailValue = findViewById(R.id.detailValue);
        mPhone = findViewById(R.id.dataPhone);
        mBuy = findViewById(R.id.buyData);

        mOperator_id = getIntent().getStringExtra("operator_id");
        mProduct_id = getIntent().getStringExtra("product_id");
        util = new Util();

        loadDataDetails();
        loadMethod();

        mBuy.setOnClickListener(v -> {
            String beneficiary_msisdn = String.valueOf(mPhone.getText());
            String amount = detailAmount.getText().toString().trim();
            String operator_id = mOperator_id;
            String product_id = mProduct_id;

            if (beneficiary_msisdn.isEmpty()) {
                mPhone.setError("Enter Phone!");
                mPhone.requestFocus();
                return;
            }
            if (!(beneficiary_msisdn.startsWith("080") || beneficiary_msisdn.startsWith("090") || beneficiary_msisdn.startsWith("081")
                    || beneficiary_msisdn.startsWith("070"))) {
                mPhone.setError("Enter a valid phone number!");
                mPhone.requestFocus();
                return;
            }
            buyData(amount, operator_id, product_id, beneficiary_msisdn);

        });
    }

    private void loadMethod() {
        AndroidNetworking.get("https://api.blochq.io/v1/bills/operators/" + mOperator_id + "/products?bill=telco")
                .addHeaders("Authorization", "Bearer sk_live_648dec6240ffc21ad42aa3ff648dec6240ffc21ad42aa400")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray array = jsonObject.getJSONArray("data");
                            String id = array.getJSONObject(0).getString("id");
                            String category = array.getJSONObject(0).getString("category");
                            String fee_type = array.getJSONObject(0).getString("fee_type");
                            String name = array.getJSONObject(0).getString("name");
                            String operator = array.getJSONObject(0).getString("operator");
                            saveOperatorId(mOperator_id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Error Occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveOperatorId(String operatorId) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("operator_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("operator_id", operatorId + "");
        edit.apply();
    }

    private void saveSector(String sector) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("sector", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("sector", sector + "");
        edit.apply();
    }

    private void buyData(String amount, String mProduct_id, String operator_id, String beneficiary_msisdn) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating Please Wait...");
        progressDialog.show();

        Intent intent = new Intent(getApplicationContext(), DataPayActivity.class);
        intent.putExtra("product_id", mProduct_id);
        intent.putExtra("operator_id", operator_id);
        intent.putExtra("amount", amount);
        intent.putExtra("beneficiary_msisdn", beneficiary_msisdn);

        System.out.println("product_id ::" + mProduct_id);
        System.out.println("product_idsXXX ::" + mProduct_id);
        intent.putExtra("sector", sector);
        startActivity(intent);
        finish();

        progressDialog.dismiss();
    }

    private void loadDataDetails() {
        MobileData mobileData = (MobileData) getIntent().getSerializableExtra("key");
        detailName.setText(mobileData.getName());
        detailValue.setText(mobileData.getData_value());
        detailExpiry.setText(mobileData.getData_expiry());
        detailAmount.setText(mobileData.getAmount());
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to go back?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked "Yes," perform the desired action
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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