package com.ick.emailguard.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ick.emailguard.R;
import com.ick.emailguard.helper.SessionManager;

/**
 * Created by Edo Firmansyah on 01/12/2017.
 */

public class SplashScreen extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sessionManager = new SessionManager(getApplicationContext());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sessionManager.checkLogin();
                finish();
            }
        },5000);
    }
}
