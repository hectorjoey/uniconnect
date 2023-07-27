package hector.developers.uniconnectapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.api.RetrofitClient;
import hector.developers.uniconnectapp.model.Users;
import hector.developers.uniconnectapp.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    public static final String USER_PHONE_NUMBER = "hector.developers.uniconnect.USER_PHONE_NUMBER";
    public static final String USER_FIRSTNAME = "hector.developers.uniconnect.USER_FIRSTNAME";
    public static final String USER_LASTNAME = "hector.developers.uniconnect.USER_LASTNAME";
    public static final String USERNAME = "hector.developers.uniconnect.USERNAME";

    TextView tvLogin;

    TextInputEditText mEmail, mPassword, mConfirmPassword;

    Button mRegister;
    private Util util;

    private String firstname, lastname, username, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvLogin = findViewById(R.id.tv_login_text);
        firstname = getIntent().getStringExtra(USER_FIRSTNAME);
        lastname = getIntent().getStringExtra(USER_LASTNAME);
        username = getIntent().getStringExtra(USERNAME);
        phone = getIntent().getStringExtra(USER_PHONE_NUMBER);

        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mConfirmPassword = findViewById(R.id.et_confirm_password);
        mRegister = findViewById(R.id.btn_register);

        util = new Util();


        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        mRegister.setOnClickListener(v -> {
            String password = mPassword.getText().toString().trim();
            String email = mEmail.getText().toString().trim();
            String confirmPassword = mConfirmPassword.getText().toString().trim();
            if (util.isNetworkAvailable(getApplicationContext())) {

                if (email.isEmpty()) {
                    mEmail.setError("Enter email!");
                    mEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    mPassword.setError("Enter password!");
                    mPassword.requestFocus();
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    mConfirmPassword.setError("Confirm password!");
                    mConfirmPassword.requestFocus();
                    return;
                }

                if (!confirmPassword.equals(mPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(username, firstname, lastname, phone, email, password);
                System.out.println("Firsname::  " + firstname + " lastname:::  " + lastname + "Username::  " + username + "  email::  " + email + "phone::  " + phone);
            } else {
                Toast.makeText(RegisterActivity.this, "Please Check your network connection...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String username, String firstname, String lastname, String phone, String email, String password) {

        Call<Users> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(username, firstname, lastname, phone, email, password);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                    System.out.println("Successful.." + response);
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed!", Toast.LENGTH_SHORT).show();
                    System.out.println("Failed::::" + response);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Check your network connection!", Toast.LENGTH_SHORT).show();
                System.out.println("Failed::::" + t.getMessage());
                progressDialog.dismiss();
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
            Intent intent = new Intent(getApplicationContext(), RegOneActivity.class);
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