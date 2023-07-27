package hector.developers.uniconnectapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import co.paystack.android.api.model.ApiResponse;
import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_settings extends AppCompatActivity {

    LinearLayout mLinearLayout;
    TextView mPhone, mEmail;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mEmail = findViewById(R.id.feedback_email);
        mPhone = findViewById(R.id.feedback_phone);
        mLinearLayout = findViewById(R.id.delete_account);

        HashMap<String, String> id = getUserId();
        userId = id.get("userId");

        System.out.println("ids:: " + userId);


        Long users = Long.parseLong(userId);

        mLinearLayout.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Are you sure you want to delete? You will not have access to you account!");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    deleteAccount(users);
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        mEmail.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmail.getText().toString()});
            email.putExtra(Intent.EXTRA_SUBJECT, "");
            email.putExtra(Intent.EXTRA_TEXT, "");

            //need this to prompts email client only
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        });

        mPhone.setOnClickListener(v -> makeCall());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_settings);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_search:
                    startActivity(new Intent(getApplicationContext(), activity_search.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_settings:
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), activity_profile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
    }

    private void deleteAccount(Long users) {
        retrofit2.Call<ApiResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteAccount(users);
        System.out.println("Users Id >>> " + users);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting User please wait...");
        progressDialog.show();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                    Intent deleteIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(deleteIntent);
                    System.out.println("DEleting REsponse{{} " + response);
                    finish();
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to delete!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    System.out.println("Error REsponse{{} " + response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network error!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private HashMap<String, String> getUserId() {
        HashMap<String, String> id = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        id.put("userId", sharedPreferences.getString("userId", null));
        return id;
    }

    private void makeCall() {
        try {
            Log.e("Setting Activity", "Invoking call");
            if (ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE") == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + mPhone.getText().toString()));
                startActivity(i);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 0);
            }

        } catch (Exception e) {
            Log.e("Setting Activity", "Failed to invoke call", e);
            Toast.makeText(getApplicationContext(), "Call invoking failed!", Toast.LENGTH_SHORT).show();
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