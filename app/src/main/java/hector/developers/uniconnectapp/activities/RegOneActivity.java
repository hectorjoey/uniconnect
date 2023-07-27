package hector.developers.uniconnectapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import hector.developers.uniconnectapp.R;


public class RegOneActivity extends AppCompatActivity {

    public static final String USER_PHONE_NUMBER = "hector.developers.uniconnect.USER_PHONE_NUMBER";
    public static final String USER_FIRSTNAME = "hector.developers.uniconnect.USER_FIRSTNAME";
    public static final String USER_LASTNAME = "hector.developers.uniconnect.USER_LASTNAME";
    public static final String USERNAME = "hector.developers.uniconnect.USERNAME";

    Button btnNext;

    TextInputEditText mUsername, mFirstname, mLastname, mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_one);
        mUsername = findViewById(R.id.et_username);
        mFirstname = findViewById(R.id.et_firstname);
        mLastname = findViewById(R.id.et_lastname);
        mPhone = findViewById(R.id.et_phone);

        btnNext = findViewById(R.id.btn_one);

        btnNext.setOnClickListener(v -> {

            String username = mUsername.getText().toString().trim();
            String firstname = mFirstname.getText().toString().trim();
            String lastname = mLastname.getText().toString().trim();
            String phone = mPhone.getText().toString().trim();

            if (username.isEmpty()) {
                mUsername.setError("Username is required");
                mUsername.requestFocus();
                return;
            }
            if (firstname.isEmpty()) {
                mFirstname.setError("Firstname is required");
                mFirstname.requestFocus();
                return;

            }
            if (lastname.isEmpty()) {
                mLastname.setError("Lastname is required");
                mLastname.requestFocus();
                return;
            }

            if (phone.isEmpty()) {
                mPhone.setError("Phone is required");
                mPhone.requestFocus();
                return;
            }
            if (phone.length() != 11) {
                mPhone.setError("Phone number is not complete!");
                mPhone.requestFocus();
                return;
            }
            if (!(phone.startsWith("080") || phone.startsWith("090") || phone.startsWith("081") || phone.startsWith("070"))) {
                mPhone.setError("Please enter a valid phone number!");
                mPhone.requestFocus();

            } else {
                regOneIntent(username, firstname, lastname, phone);
            }
        });
    }

    private void regOneIntent(String username, String firstname, String lastname, String phone) {
        Intent intent = new Intent(RegOneActivity.this, RegisterActivity.class);
        intent.putExtra(USERNAME, username);
        intent.putExtra(USER_PHONE_NUMBER, phone);
        intent.putExtra(USER_FIRSTNAME, firstname);
        intent.putExtra(USER_LASTNAME, lastname);
        startActivity(intent);

    }


    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to go back?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked "Yes," perform the desired action
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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