package hector.developers.uniconnectapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import hector.developers.uniconnectapp.R;


public class SplashScreen extends AppCompatActivity {

    ImageView mSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mSplash = findViewById(R.id.splash);
        mSplash.setAnimation(AnimationUtils.loadAnimation(SplashScreen.this, R.anim.blink));
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(3 * 1000);
                    Intent intent = new Intent(getBaseContext(), OnBoardingActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}