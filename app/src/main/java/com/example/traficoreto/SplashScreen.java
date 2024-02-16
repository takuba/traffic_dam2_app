package com.example.traficoreto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.traficoreto.helpers.MyPreferences;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String username = MyPreferences.getUserId(this);
        MyApplication.setLanguage(this);

        int splashDuration = 2000;
        new Handler().postDelayed(() -> {
            if (username == null || username.equals("")){
                Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }else {

                Intent intent = new Intent(SplashScreen.this, ListScreen.class);
                startActivity(intent);
                finish();
            }

        }, splashDuration);
    }
}