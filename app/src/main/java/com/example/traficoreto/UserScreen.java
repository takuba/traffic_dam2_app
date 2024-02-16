package com.example.traficoreto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.traficoreto.helpers.HelperFuntions;
import com.example.traficoreto.helpers.MyPreferences;
import com.example.traficoreto.helpers.UserCallback;
import com.example.traficoreto.helpers.UsersHelpers;
import com.example.traficoreto.structure.User;

public class UserScreen extends AppCompatActivity {
    private Button maps,settings,logout,list;
    String urlRequest,userId;
    private TextView textView,userName,userCity,userCountry,userAge,userStreet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        urlRequest =  MyPreferences.getUrlRequest(UserScreen.this);
        userId = MyPreferences.getUserId(this);
        textView = findViewById(R.id.textView);
        userName = findViewById(R.id.textView2);
        userAge = findViewById(R.id.textView3);
        userCity = findViewById(R.id.textView4);
        UsersHelpers.getUserInfoFromDb(userId,urlRequest,this, new UserCallback() {
            @Override
            public void onSuccess(User user) {
                textView.setText(user.getMail());
                userName.setText(user.getName());
                userAge.setText(String.valueOf(user.getAge()));
                userCity.setText(user.getCity());
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        list = findViewById(R.id.list_btn);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(UserScreen.this, ListScreen.class);
                //intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLogin);
                finish();
            }
        });

        settings = findViewById(R.id.setting_btn);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserScreen.this, SettingScreen.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}