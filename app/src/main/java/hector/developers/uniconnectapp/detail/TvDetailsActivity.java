package hector.developers.uniconnectapp.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

import co.paystack.android.api.model.ApiResponse;
import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.activities.MainActivity;
import hector.developers.uniconnectapp.api.Api;
import hector.developers.uniconnectapp.model.Tv;
import hector.developers.uniconnectapp.pay.TVPayActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvDetailsActivity extends AppCompatActivity {

    TextView detailName, detailAmount;
    TextInputEditText mCardNumber;

    String mProduct_id, mOperator_id, mSector;

    Button mPay;

    String  sectors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_details);
        detailName =findViewById(R.id.tvDetailName);
        detailAmount = findViewById(R.id.tvDetailsAmount);
        mCardNumber = findViewById(R.id.tvCardNumber);
        mOperator_id = getIntent().getStringExtra("operator");
        mProduct_id = getIntent().getStringExtra("id");

        mPay = findViewById(R.id.payTv);


        HashMap<String, String> sector = getSectors();
        sectors = sector.get("sectors");
        System.out.println("Sect><><" + sectors);

        System.out.println(mOperator_id);

        loadDataDetails();

        mPay.setOnClickListener(v -> {
            String amount = detailAmount.getText().toString().trim();
            String operator_id = mOperator_id;
            String product_id = mProduct_id;
            String device_number = String.valueOf(mCardNumber.getText());

            if (device_number.isEmpty()) {
                mCardNumber.setError("Enter Phone!");
                mCardNumber.requestFocus();
            }
//            pay(amount, product_id, operator_id, device_number);
            verifyDevice();
        });
    }

    private HashMap<String, String> getSectors() {
        HashMap<String, String> sector = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("sectors", Context.MODE_PRIVATE);
        sector.put("sectors", sharedPreferences.getString("sectors", null));
        return sector;
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

        String deviceNumber = String.valueOf(mCardNumber.getText());

        // Make the API call
        Call<ApiResponse> call = apiService.validateDevice(sectors.toLowerCase(), deviceNumber);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Device verification successful!", Toast.LENGTH_SHORT).show();
//
                    String amount = detailAmount.getText().toString().trim();
                    String operator_id = mOperator_id;
                    String product_id = mProduct_id;
                    String device_number = String.valueOf(mCardNumber.getText());
//
                    pay(amount, product_id, operator_id, device_number);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Verification failed!", Toast.LENGTH_SHORT).show();
                    mCardNumber.setError("Enter correct smartcard number!");
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

    private void loadDataDetails() {
        Tv tv = (Tv) getIntent().getSerializableExtra("key");
        detailName.setText(tv.getName());
        detailAmount.setText(tv.getAmount());
    }

    private void pay(String amount, String product_id, String operator_id, String device_number) {

        System.out.println("amount " + amount);
        System.out.println("product_id  " + product_id);
        System.out.println("operator" + operator_id);
        System.out.println("device" + device_number);


        Intent intent = new Intent(getApplicationContext(), TVPayActivity.class);

        intent.putExtra("amount", amount);
        intent.putExtra("product_id" , product_id);
        intent.putExtra("operator_id" , operator_id);
        intent.putExtra("device_number" , device_number);

        startActivity(intent);
        finish();
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