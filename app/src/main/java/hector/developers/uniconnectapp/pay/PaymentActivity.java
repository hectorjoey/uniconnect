package hector.developers.uniconnectapp.pay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.activities.MainActivity;
import hector.developers.uniconnectapp.activities.SuccessPageActivity;
import hector.developers.uniconnectapp.model.CreditCardTextFormatter;
import hector.developers.uniconnectapp.utils.Util;

public class PaymentActivity extends AppCompatActivity {

    private Transaction transaction;
    private Charge charge;
    ProgressBar mLoading_pay_order;
    Button mBtn_pay;

    EditText mEtCardNumber, mEtExpiry, mEtCvv;

    String operatorID, sector, id, address, product_id, account_id, name, device_number, beneficiary_msisdn, amount, meter_type, operator_id;

    String sectors;
    private Util util;
    String usersemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        AndroidNetworking.initialize(getApplicationContext());
        util = new Util();
        mBtn_pay = findViewById(R.id.btn_pay);
        mEtCardNumber = findViewById(R.id.et_card_number);
        mEtExpiry = findViewById(R.id.et_expiry);
        mEtCvv = findViewById(R.id.et_cvv);
        mLoading_pay_order = findViewById(R.id.loading_pay_order);
        amount = getIntent().getStringExtra("amount");
        device_number = getIntent().getStringExtra("device_number");
        beneficiary_msisdn = getIntent().getStringExtra("beneficiary_msisdn");
        name = getIntent().getStringExtra("name");
        meter_type = getIntent().getStringExtra("meter_type");
        operatorID = getIntent().getStringExtra("operatorId");


        HashMap<String, String> email = getUsersemail();
        usersemail = email.get("usersemail");

        HashMap<String, String> sector = getSector();
        sectors = sector.get("sectors");


        HashMap<String, String> id = getProductId();
        product_id = id.get("product_id");

        HashMap<String, String> operatorId = getOperatorId();
        operator_id = operatorId.get("operator_id");

        initViews();
    }

    private HashMap<String, String> getUsersemail() {
        HashMap<String, String> email = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("usersemail", Context.MODE_PRIVATE);
        email.put("usersemail", sharedPreferences.getString("usersemail", null));
        return email;
    }


    /**
     * Initialize all views here
     */
    private void initViews() {
        // Add formatting to card number, cvv, and expiry date
        addTextWatcherToEditText();

        // Set the amount to pay in the button
        String totalPrice = amount;
        mBtn_pay.setText(getString(R.string.pay_amount, totalPrice));

        // Handle button click
        handleClicks();
    }

    /**
     * Add formatting to card number, cvv, and expiry date
     */
    private void addTextWatcherToEditText() {
        // Make button unclickable for the first time
        mBtn_pay.setEnabled(false);
        mBtn_pay.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_round_opaque));

        // Add a text watcher to enable/disable the button based on input fields
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = mEtCardNumber.getText().toString();
                String s2 = mEtExpiry.getText().toString();
                String s3 = mEtCvv.getText().toString();

                // Check if any field is empty, make button unclickable
                if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty()) {
                    mBtn_pay.setEnabled(false);
                    mBtn_pay.setBackground(ContextCompat.getDrawable(
                            PaymentActivity.this,
                            R.drawable.btn_round_opaque
                    ));
                }

                // Check the length of all input fields, enable/disable the button accordingly
                if (s1.length() >= 16 && s2.length() == 5 && s3.length() == 3) {
                    mBtn_pay.setEnabled(true);
                    mBtn_pay.setBackground(ContextCompat.getDrawable(
                            PaymentActivity.this,
                            R.drawable.btn_border_blue_bg
                    ));
                } else {
                    mBtn_pay.setEnabled(false);
                    mBtn_pay.setBackground(ContextCompat.getDrawable(
                            PaymentActivity.this,
                            R.drawable.btn_round_opaque
                    ));
                }

                // Add a slash to expiry date after the first two characters (month)
                if (s2.length() == 2 && start == 2 && before == 1 && !s2.contains("/")) {
                    mEtExpiry.setText(getString(
                            R.string.expiry_space,
                            String.valueOf(s2.charAt(0))
                    ));
                    mEtExpiry.setSelection(1);
                } else if (s2.length() == 2 && before == 0) {
                    mEtExpiry.setText(getString(R.string.expiry_slash, s2));
                    mEtExpiry.setSelection(3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        // Add text watchers
        mEtCardNumber.addTextChangedListener(new CreditCardTextFormatter("-", 4));
        mEtExpiry.addTextChangedListener(watcher);
        mEtCvv.addTextChangedListener(watcher);
    }

    private void handleClicks() {
        // Handle pay button click
        mBtn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.isNetworkAvailable(getApplicationContext())) {
                    // Show loading progress
                    mLoading_pay_order.setVisibility(View.VISIBLE);
                    mBtn_pay.setVisibility(View.GONE);

                    // Perform payment
                    doPayment();
                } else {
                    Toast.makeText(PaymentActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void doPayment() {
        String publicKey = "pk_live_255a4098aad149c2679df29d9caad4bb7d008866";

        // Set public key for Paystack SDK
        PaystackSdk.setPublicKey(publicKey);

        // Initialize the charge
        charge = new Charge();
        charge.setCard(loadCardFromForm());
        charge.setAmount(Integer.parseInt(amount) * 100);
        charge.setEmail(usersemail);
        charge.setReference("payment" + System.currentTimeMillis());

        // Perform the card charge
        doChargeCard();
    }

    private Card loadCardFromForm() {
        String cardNumber = mEtCardNumber.getText().toString().trim();
        String expiryDate = mEtExpiry.getText().toString().trim();
        String cvc = mEtCvv.getText().toString().trim();

        // Remove any spaces from the card number
        String cardNumberWithoutSpace = cardNumber.replace(" ", "");

        // Extract month and year from the expiry date
        String monthValue = expiryDate.substring(0, Math.min(expiryDate.length(), 2));
        String yearValue = expiryDate.substring(Math.max(0, expiryDate.length() - 2));

        // Build the card object with only the number, update the other fields later
        Card card = new Card.Builder(cardNumberWithoutSpace, 0, 0, "").build();

        // Update the cvc field of the card
        card.setCvc(cvc);

        // Validate and set the expiry month
        int month = 0;
        try {
            month = Integer.parseInt(monthValue);
        } catch (NumberFormatException ignored) {
        }
        card.setExpiryMonth(month);

        // Validate and set the expiry year
        int year = 0;
        try {
            year = Integer.parseInt(yearValue);
        } catch (NumberFormatException ignored) {
        }
        card.setExpiryYear(year);

        // Validate the card details
        if (!card.isValid()) {
            Toast.makeText(getApplicationContext(), "Invalid card details entered", Toast.LENGTH_LONG).show();
        }

        return card;
    }

    /**
     * Perform the charge and receive callbacks for successful and failed payments
     */
    private void doChargeCard() {
        transaction = null;

        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // Hide loading progress
                mLoading_pay_order.setVisibility(View.GONE);
                mBtn_pay.setVisibility(View.VISIBLE);

                // Show success message
                Toast.makeText(PaymentActivity.this, "Payment was successful", Toast.LENGTH_LONG).show();

                // Perform the power purchase
                buyPower();

                PaymentActivity.this.transaction = transaction;
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                PaymentActivity.this.transaction = transaction;
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // Hide loading progress
                mLoading_pay_order.setVisibility(View.GONE);
                mBtn_pay.setVisibility(View.VISIBLE);

                // Check if the access code has expired
                PaymentActivity.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    PaymentActivity.this.doChargeCard();
                    return;
                }

                // Handle error messages
                if (transaction.getReference() != null) {
                    Toast.makeText(PaymentActivity.this, error.getMessage() != null ? error.getMessage() : "", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PaymentActivity.this, error.getMessage() != null ? error.getMessage() : "", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void buyPower() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending...");
        progressDialog.show();

        int parsedAmount = Integer.parseInt(amount) * 100;
        System.out.println(parsedAmount);


        // Create a JSONObject with the request parameters
        JSONObject requestParams = new JSONObject();
        try {

            JSONObject deviceDetails = new JSONObject();
            deviceDetails.put("meter_type", meter_type.toLowerCase());
            deviceDetails.put("device_number", device_number);

            requestParams.put("amount", parsedAmount);
            requestParams.put("product_id", product_id);
            requestParams.put("operator_id", operator_id);
            requestParams.put("beneficiary_msisdn", beneficiary_msisdn);

            requestParams.put("device_details", deviceDetails);
            requestParams.put("meta_data", new JSONObject()); // Empty metadata

        } catch (JSONException e) {
            e.printStackTrace();
        }



//        System.out.println("sect" + sect);


        // Send the POST request using Android Networking Library
        AndroidNetworking.post("https://api.blochq.io/v1/bills/payment?bill=electricity")
                .addHeaders("Authorization", "Bearer sk_test_648dec6240ffc21ad42aa403648dec6240ffc21ad42aa404")
                .addHeaders("Content-Type", "application/json") // Set the content type to JSON
                .addStringBody(requestParams.toString()) // Pass the JSON string as the request body
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        System.out.println("Response::: " + response);
                        System.out.println("parames:: " + requestParams);

                        boolean success = false;
                        try {
                            success = response.getBoolean("success");

                            String created_at = response.getJSONObject("data").getString("created_at");
                            String status = response.getJSONObject("data").getString("status");
                            int amount = response.getJSONObject("data").getInt("amount");
                            String reference = response.getJSONObject("data").getString("reference");
                            String customer_name = response.getJSONObject("data").getString("customer_name");
                            String operator_id = response.getJSONObject("data").getString("operator_id");
                            String product_id = response.getJSONObject("data").getString("product_id");
                            String bill_type = response.getJSONObject("data").getString("bill_type");
                            String operator_name = response.getJSONObject("data").getJSONObject("meta_data").getString("operator_name");
                            String token = response.getJSONObject("data").getJSONObject("meta_data").getString("token");

                            // Create an Intent to start the successPageActivity
                            Intent intent = new Intent(getApplicationContext(), SuccessPageActivity.class);

                            // Pass the extracted data as extras to the intent
                            intent.putExtra("success", success);
                            intent.putExtra("created_at", created_at);
                            intent.putExtra("status", status);
                            intent.putExtra("amount", amount);
                            intent.putExtra("reference", reference);
                            intent.putExtra("customer_name", customer_name);
                            intent.putExtra("operator_id", operator_id);
                            intent.putExtra("product_id", product_id);
                            intent.putExtra("bill_type", bill_type);
                            intent.putExtra("operator_name", operator_name);
                            intent.putExtra("token", token);

                            // Start the successPageActivity
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        // Handle the error here
                        // This method will be called when there is an error with the request
                        System.out.println("Code " + anError.getErrorCode());
                        System.out.println("Message :::: " + anError.getMessage());
                        System.out.println("Details::: " + anError.getErrorDetail());
                        System.out.println("Error::   " + anError.getResponse());
                        Toast.makeText(getApplicationContext(), "Error occurred!, Please try again later!", Toast.LENGTH_SHORT).show();
                        System.out.println("parames:: " + requestParams);
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLoading_pay_order.setVisibility(View.GONE);
    }

    public HashMap<String, String> getProductId() {
        HashMap<String, String> id = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("product_id", Context.MODE_PRIVATE);
        id.put("product_id", sharedPreferences.getString("product_id", null));
        return id;
    }

    public HashMap<String, String> getSector() {
        HashMap<String, String> sector = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("sectors", Context.MODE_PRIVATE);
        sector.put("sectors", sharedPreferences.getString("sectors", null));
        return sector;
    }

    private HashMap<String, String> getOperatorId() {
        HashMap<String, String> operatorId = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("operator_id", Context.MODE_PRIVATE);
        operatorId.put("operator_id", sharedPreferences.getString("operator_id", null));
        return operatorId;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to go back?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish(); // Close the activity
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
