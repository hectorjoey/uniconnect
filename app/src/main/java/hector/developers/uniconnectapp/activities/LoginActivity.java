package hector.developers.uniconnectapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.api.RetrofitClient;
import hector.developers.uniconnectapp.model.Users;
import hector.developers.uniconnectapp.utils.SessionManagement;
import hector.developers.uniconnectapp.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String USER_FIRSTNAME = "hector.developers.uniconnect.USER_FIRSTNAME";
    private static final String TAG = "LoginActivity";
    TextInputEditText mEmail, mPassword;
    Button mLogin;
    private Util util;
    String firstname;
    SessionManagement sessionManagement;

    ProgressDialog progressDialog;

    TextView mRegLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRegLink = findViewById(R.id.btn_register_text);

        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mLogin = findViewById(R.id.btn_login);

        util = new Util();

        sessionManagement = new SessionManagement(this);
        sessionManagement.getLoginEmail();
        sessionManagement.getLoginPassword();

        mLogin.setOnClickListener(v -> {

            final String email = Objects.requireNonNull(mEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(mPassword.getText()).toString().trim();

            //validating fields
            if (util.isNetworkAvailable(getApplicationContext())) {
                if (email.isEmpty()) {
                    mEmail.setError("Email is required");
                    mEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Enter a valid email!");
                    mEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    mPassword.setError("Enter Password!");
                    mPassword.requestFocus();
                    return;
                }

                loginUser(email, password);
            } else {
                Toast.makeText(LoginActivity.this, "Please Check your network connection...", Toast.LENGTH_SHORT).show();
            }
        });

        mRegLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegOneActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginUser(String email, String password) {
        Call<Users> call;
        call = RetrofitClient
                .getInstance()
                .getApi()
                .login(email, password);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating Please Wait...");
        progressDialog.show();

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Users users = response.body();
                    assert response.body() != null;
                    assert users != null;

                    Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    sessionManagement.setLoginEmail(email);
                    sessionManagement.setLoginPassword(password);
                    assert response.body() != null;


                    progressDialog.dismiss();
                    mainIntent.putExtra("userId ", users.getId());
                    mainIntent.putExtra("username", response.body().getUsername());
                    mainIntent.putExtra("firstname ", response.body().getFirstname());
                    mainIntent.putExtra("userType", response.body().getUserType());
                    saveUserId(users.getId());
                    saveUsersUsername(users.getUsername());
                    saveUsersPhone(users.getPhone());
                    saveUsersLastname(users.getLastname());
                    saveUsersFirstname(users.getFirstname());
                    saveUsersUserType(users.getUserType());
                    saveUserEmail(users.getEmail());
                } else {
                    Toast.makeText(getApplicationContext(), " Failed!", Toast.LENGTH_SHORT).show();
                    System.out.println("Failed!>>" + response);
                    System.out.println("Failed!>>" + response.body());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void saveUserEmail(String email) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("usersemail", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("usersemail", email + "");
        edit.apply();
    }

    private void saveUsersFirstname(String firstname) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("usersfirstname", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("usersfirstname", firstname + "");
        edit.apply();
    }

    private void saveUsersLastname(String lastname) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("userslastname", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("userslastname", lastname + "");
        edit.apply();
    }

    private void saveUsersPhone(String phone) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("usersphone", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("usersphone", phone + "");
        edit.apply();
    }

    private void saveUsersUserType(String userType) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("userstype", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("userstype", userType + "");
        edit.apply();

    }

    private void saveUsersUsername(String username) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("usersname", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("usersname", username + "");
        edit.apply();
    }


    public void saveUserId(Long id) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("userId", id + "");
        edit.apply();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked "Yes," perform the desired action
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