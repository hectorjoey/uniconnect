package hector.developers.uniconnectapp.detail;

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
import hector.developers.uniconnectapp.model.Airtime;
import hector.developers.uniconnectapp.pay.DataPayActivity;
import hector.developers.uniconnectapp.utils.Util;

public class AirtimeDetailActivity extends AppCompatActivity {

    TextInputEditText mPhoneNumber, mAmount;
    Button mBuyAirtime;

    String mOperator_id;
    TextView airVName;

    String mProduct_id, mSector;

    Util util;

//    Button mPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime_detail);
        mPhoneNumber = findViewById(R.id.airtimePhone);
        mAmount = findViewById(R.id.airtimeAmount);
        mBuyAirtime = findViewById(R.id.buyAirtime);
        airVName =findViewById(R.id.airVName);

        mOperator_id = getIntent().getStringExtra("operator");
        mProduct_id = getIntent().getStringExtra("id");

        System.out.println("operator id:::  "+ mOperator_id);
        System.out.println("product id:::  "+ mProduct_id);


        loadDataDetails();

        System.out.println(mProduct_id);
        util = new Util();

        loadDataDetails();
//        loadMethod();

        mBuyAirtime.setOnClickListener(v -> {
            String beneficiary_msisdn = mPhoneNumber.getText().toString().trim();
            String amount = mAmount.getText().toString().trim();
            String operator_id = mOperator_id;
            String product_id = mProduct_id;

            if (beneficiary_msisdn.isEmpty()) {
                mPhoneNumber.setError("Enter Phone!");
                mPhoneNumber.requestFocus();
                return;
            }
            if (!(beneficiary_msisdn.startsWith("080") || beneficiary_msisdn.startsWith("090") || beneficiary_msisdn.startsWith("081")
                    || beneficiary_msisdn.startsWith("070"))) {
                mPhoneNumber.setError("Enter a valid phone number!");
                mPhoneNumber.requestFocus();
                return;
            }
            if (amount.isEmpty()){
                mAmount.setError("Enter amount!");
                mAmount.requestFocus();
                return;
            }
            buyAirtime(amount, operator_id, product_id, beneficiary_msisdn);

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

    private void buyAirtime(String amount, String mProduct_id, String operator_id, String beneficiary_msisdn) {
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
        System.out.println(beneficiary_msisdn);
        System.out.println(amount);
//        intent.putExtra("sector", sector);
        startActivity(intent);
        finish();

        progressDialog.dismiss();
    }

    private void loadDataDetails() {
        Airtime airtime = (Airtime) getIntent().getSerializableExtra("key");
        airVName.setText(airtime.getName());
    }
}