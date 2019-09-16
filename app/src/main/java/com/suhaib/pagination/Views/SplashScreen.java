package com.suhaib.pagination.Views;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.suhaib.pagination.R;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

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
                Log.i(TAG,"Run function in handler");
                MainActivity.startActivityAndFinish(SplashScreen.this);
            }
        }, SPLASH_TIME_OUT);
    }// end on create

    /***
     * Branch IO
     */

    @Override
    public void onStart() {
        super.onStart();

        // Branch init
        Branch instance = Branch.getInstance();
        if (instance != null) {
            instance.initSession(new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(JSONObject referringParams, BranchError error) {
                    if (error == null) {
                        Log.i("BRANCH SDK", referringParams.toString());

                        try {

                            Log.i(TAG,referringParams.getString("key"));
                            int key = Integer.parseInt(referringParams.getString("key"));
                            MainActivity.startActivityAndFinish(SplashScreen.this, key);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.i("BRANCH SDK", error.getMessage());
                    }
                }
            }, this.getIntent().getData(), this);
        }
    }//end on start


    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}
