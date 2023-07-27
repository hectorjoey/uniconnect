package hector.developers.uniconnectapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.api.RetrofitClient;
import hector.developers.uniconnectapp.model.Transactions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessPageActivity extends AppCompatActivity {

    TextView mTv_token;
    Button mClose;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_page);
        mTv_token = findViewById(R.id.tv_token);
        mClose = findViewById(R.id.close);

        HashMap<String, String> id = getUserId();
        userId = id.get("userId");

//        boolean success = getIntent().getBooleanExtra("success", false);
        String created_at = getIntent().getStringExtra("created_at");
        String status = getIntent().getStringExtra("status");
        int amount = getIntent().getIntExtra("amount", 0);
        String reference = getIntent().getStringExtra("reference");
//        String customerName = getIntent().getStringExtra("customer_name");
        String operator_id = getIntent().getStringExtra("operator_id");
        String product_id = getIntent().getStringExtra("product_id");
        String bill_type = getIntent().getStringExtra("bill_type");
        String operator_name = getIntent().getStringExtra("operator_name");
        String token = getIntent().getStringExtra("token");

        if (operator_id.equals("op_tHvaAHp85mTsRtEU2yqxLM")) {
            bill_type = "Telecommunication";
        }

        if (token == null) {
            mTv_token.setVisibility(View.INVISIBLE);
        } else {
            mTv_token.setVisibility(View.VISIBLE);
            mTv_token.setText("Your Electricty token \n\n" + token);
        }


        String finalBill_type = bill_type;
        mClose.setOnClickListener(v -> createTransactions(created_at, status, amount, reference, operator_id, product_id, finalBill_type, operator_name, userId, token));
    }

    private void createTransactions(String created_at, String status, int amount, String reference, String operator_id, String product_id, String bill_type, String operator_name, String userId, String token) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        Call<Transactions> call = RetrofitClient
                .getInstance()
                .getApi()
                .createTransactions(created_at, status, amount, reference, operator_id, product_id, bill_type, operator_name, userId, token);

        call.enqueue(new Callback<Transactions>() {
            @Override
            public void onResponse(Call<Transactions> call, Response<Transactions> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Transactions> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private HashMap<String, String> getUserId() {
        HashMap<String, String> id = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        id.put("userId", sharedPreferences.getString("userId", null));
        return id;
    }
}