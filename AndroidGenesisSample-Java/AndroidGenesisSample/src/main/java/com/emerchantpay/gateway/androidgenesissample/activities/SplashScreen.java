package com.emerchantpay.gateway.androidgenesissample.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.emerchantpay.gateway.androidgenesissample.R;

public class SplashScreen extends AppCompatActivity {

    public static final Integer SLEEP_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SLEEP_TIME);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
            }
        };

        splashThread.start();
    }
}
