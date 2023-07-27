package hector.developers.uniconnectapp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import hector.developers.uniconnectapp.R;


public class activity_profile extends AppCompatActivity {

    String usersname, usersphone, usersfirstname, userslastname, usersemail;
    TextView mUser, mPhone, mEmail, mUsers;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mUser = findViewById(R.id.profile_username);
        mPhone = findViewById(R.id.prof_phone);
        mEmail = findViewById(R.id.prof_email);
        mUsers = findViewById(R.id.fullname);

        HashMap<String, String> username = getUsersUsername();
        usersname = username.get("usersname");

        HashMap<String, String> phone = getUsersPhone();
        usersphone = phone.get("usersphone");

        HashMap<String, String> firstname = getUsersFirstname();
        usersfirstname = firstname.get("usersfirstname");

        HashMap<String, String> lastname = getUserslastname();
        userslastname = lastname.get("userslastname");

        HashMap<String, String> email = getUsersemail();
        usersemail = email.get("usersemail");

        mUser.setText(usersname);
        mPhone.setText(usersphone);
        mUsers.setText(usersfirstname + " " + userslastname);
        mEmail.setText(usersemail);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
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
                    startActivity(new Intent(getApplicationContext(), activity_settings.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    return true;
            }
            return false;
        });
    }

    private HashMap<String, String> getUsersemail() {
        HashMap<String, String> email = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("usersemail", Context.MODE_PRIVATE);
        email.put("usersemail", sharedPreferences.getString("usersemail", null));
        return email;
    }

    private HashMap<String, String> getUserslastname() {
        HashMap<String, String> lastname = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userslastname", Context.MODE_PRIVATE);
        lastname.put("userslastname", sharedPreferences.getString("userslastname", null));
        return lastname;
    }

    private HashMap<String, String> getUsersFirstname() {
        HashMap<String, String> firstname = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("usersfirstname", Context.MODE_PRIVATE);
        firstname.put("usersfirstname", sharedPreferences.getString("usersfirstname", null));
        return firstname;
    }

    private HashMap<String, String> getUsersPhone() {

        HashMap<String, String> phone = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("usersphone", Context.MODE_PRIVATE);
        phone.put("usersphone", sharedPreferences.getString("usersphone", null));
        return phone;
    }

    private HashMap<String, String> getUsersUsername() {
        HashMap<String, String> username = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("usersname", Context.MODE_PRIVATE);
        username.put("usersname", sharedPreferences.getString("usersname", null));
        return username;
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