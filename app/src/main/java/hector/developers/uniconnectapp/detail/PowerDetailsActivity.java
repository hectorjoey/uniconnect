package hector.developers.uniconnectapp.detail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.paystack.android.PaystackSdk;
import co.paystack.android.api.model.ApiResponse;
import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.activities.MainActivity;
import hector.developers.uniconnectapp.api.Api;
import hector.developers.uniconnectapp.pay.PaymentActivity;
import hector.developers.uniconnectapp.utils.SessionManagement;
import hector.developers.uniconnectapp.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PowerDetailsActivity extends AppCompatActivity {
    private static final String TAG = "Power detail";
    TextView mBill;
    Spinner mMeterTypeSpinner;
    Button mSubmit;
    String operatorID;
    String sector;
    String product_id;
    EditText mDeviceNumber, mBeneficiary_msisdn, mAmount;
    private SharedPreferences pref;
    private String responseEmail;

    ProgressDialog progressDialog;

    private Util util;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_details);
        PaystackSdk.initialize(getApplicationContext());
        util = new Util();

//        responseEmail = "stanleychike@yahoo.com";

        AndroidNetworking.initialize(getApplicationContext());

        mBill = findViewById(R.id.detailBill);
        mSubmit = findViewById(R.id.submit);
        mDeviceNumber = findViewById(R.id.et_device);
        mMeterTypeSpinner = findViewById(R.id.meterTypeSpinner);
        operatorID = getIntent().getStringExtra("id");
        sector = getIntent().getStringExtra("sector");
        mBeneficiary_msisdn = findViewById(R.id.phone);
        mAmount = findViewById(R.id.et_amount);

        System.out.println("Sector::: " + sector);
        loadMethod();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> meterTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.meter_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        meterTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mMeterTypeSpinner.setAdapter(meterTypeAdapter);


        mSubmit.setOnClickListener(v -> {
            String beneficiary_msisdn = mBeneficiary_msisdn.getText().toString().trim();
            String amount = mAmount.getText().toString().trim();
            String meter_type = mMeterTypeSpinner.getSelectedItem().toString().trim();
            String device_number = mDeviceNumber.getText().toString().trim();
            String operator_id = operatorID;
//            String bill_Type = mBill.getText().toString().trim();

            if (util.isNetworkAvailable(getApplicationContext())) {
                if (beneficiary_msisdn.isEmpty()) {
                    mBeneficiary_msisdn.setError("Phone number is required!");
                    mBeneficiary_msisdn.requestFocus();
                    return;
                }
                if (amount.isEmpty()) {
                    mAmount.setError("Amount required!");
                    mAmount.requestFocus();
                    return;
                }

                if (device_number.isEmpty()) {
                    mDeviceNumber.setError("Device Number is required!");
                    mDeviceNumber.requestFocus();
                    return;

                }

                verifyDevice();

//                submitButton(amount, product_id, operator_id, meter_type, device_number, beneficiary_msisdn);
            } else {
                Toast.makeText(PowerDetailsActivity.this, "Please Check your network connection...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyDevice() {
            // Create a Retrofit instance
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Device Verification... Please Wait...");
        progressDialog.show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.blochq.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of the ApiService
            Api apiService = retrofit.create(Api.class);

            // Define query parameters
            String meterType = mMeterTypeSpinner.getSelectedItem().toString().trim();
            String sectors = sector;
            String deviceNumber = mDeviceNumber.getText().toString().trim();

            String meter = meterType.toLowerCase();
            String sect = sectors.toLowerCase();

            // Make the API call
            Call<ApiResponse> call = apiService.validateBill(meter,sect, deviceNumber);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(), "Device verification successful!", Toast.LENGTH_SHORT).show();

                        String beneficiary_msisdn = mBeneficiary_msisdn.getText().toString().trim();
                        String amount = mAmount.getText().toString().trim();
                        String meter_type = mMeterTypeSpinner.getSelectedItem().toString().trim();
                        String device_number = mDeviceNumber.getText().toString().trim();
                        String operator_id = operatorID;

                       submitButton(amount, product_id, operator_id, meter_type, device_number, beneficiary_msisdn);

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Verification failed!", Toast.LENGTH_SHORT).show();
                        mDeviceNumber.setError("Enter correct meter number");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    // Handle API call failure here
                    Toast.makeText(getApplicationContext(), "Network error occurred!", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void submitButton(String amount, String id, String operator_id, String meter_type, String device_number, String beneficiary_msisdn) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating Please Wait...");
        progressDialog.show();

        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra("product_id", id);
        intent.putExtra("operator_id", operatorID);
        intent.putExtra("amount", amount);
        intent.putExtra("beneficiary_msisdn", beneficiary_msisdn);
        intent.putExtra("device_number", device_number);
        intent.putExtra("meter_type", meter_type);

        intent.putExtra("sector", sector);
        System.out.println("product_id ::" + id);
        startActivity(intent);
        finish();

        progressDialog.dismiss();
    }


    private void loadMethod() {
        AndroidNetworking.get("https://api.blochq.io/v1/bills/operators/" + operatorID + "/products?bill=electricity")
                .addHeaders("Authorization", "Bearer sk_live_648dec6240ffc21ad42aa3ff648dec6240ffc21ad42aa400")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("status22", "" + jsonObject.toString());
                        try {
                            JSONArray array = jsonObject.getJSONArray("data");
                            String id = array.getJSONObject(0).getString("id");
                            String category = array.getJSONObject(0).getString("category");
                            String fee_type = array.getJSONObject(0).getString("fee_type");
                            String name = array.getJSONObject(0).getString("name");
                            String operator = array.getJSONObject(0).getString("operator");

                            Log.d("status10", id);
                            Log.d("status11", category);
                            Log.d("status12", fee_type);
                            Log.d("status13", name);
                            Log.d("status14", operator);

                            saveProductId(id);
                            saveOperatorId(operatorID);
                            mBill.setText(name);
                            System.out.println("product Id::: " + id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error2222", anError.toString());
                        Toast.makeText(getApplicationContext(), "Error Occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveOperatorId(String operatorID) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("operator_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("operator_id", operatorID + "");
        edit.apply();
    }

    public void saveProductId(String id) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("product_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("product_id", id + "");
        edit.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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