package hector.developers.uniconnectapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import hector.developers.uniconnectapp.R;
import hector.developers.uniconnectapp.list.AirtimeOperatorActivity;
import hector.developers.uniconnectapp.list.OperatorActivity;
import hector.developers.uniconnectapp.list.PowerListActivity;
import hector.developers.uniconnectapp.list.UniOperatorActivity;

public class MainActivity extends AppCompatActivity {
    CardView mUtilityCard, airtimeCard, dataCard, mTvCard;

    TextView mGreetings;

    String greeting, usersname;

    TextView mUser;

    String userFirstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUtilityCard = findViewById(R.id.utilityCard);
        airtimeCard = findViewById(R.id.airtimeCard);
        dataCard = findViewById(R.id.dataCard);
        mTvCard = findViewById(R.id.tvCard);
        mGreetings = findViewById(R.id.greeting);
        mUser = findViewById(R.id.username);




        HashMap<String, String> username = getUsersUsername();
        usersname = username.get("usersname");
        System.out.println("Users><><" + usersname);


        //Get the time of day
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening";
        } else if (hour >= 21 && hour < 24) {
            greeting = "Good Night";
        } else {
            greeting = "Good Morning";
        }
        mGreetings.setText(greeting);
        mUser.setText(usersname);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.networkby, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.television, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.phcn, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.logo, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        dataCard.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OperatorActivity.class);
            startActivity(intent);
            finish();
        });
        mTvCard.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UniOperatorActivity.class);
            startActivity(intent);
            finish();
        });
        mUtilityCard.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PowerListActivity.class);
            startActivity(intent);
            finish();
        });

        airtimeCard.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AirtimeOperatorActivity.class);
            startActivity(intent);
            finish();
//            Toast.makeText(getApplicationContext(), "Will be available", Toast.LENGTH_SHORT).show();
        });

        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
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
                    startActivity(new Intent(getApplicationContext(), activity_profile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
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