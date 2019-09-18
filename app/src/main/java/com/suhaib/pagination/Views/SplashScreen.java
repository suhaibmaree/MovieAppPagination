package com.suhaib.pagination.Views;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.suhaib.pagination.R;
public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screan);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Run function in handler");
                MainActivity.startActivityAndFinsh(SplashScreen.this);
            }
        }, SPLASH_TIME_OUT);
    }// end on create








}
