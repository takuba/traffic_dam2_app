package com.example.traficoreto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.traficoreto.helpers.AlertDialogHelper;
import com.example.traficoreto.helpers.MyPreferences;

public class SettingScreen extends AppCompatActivity {
    private Button maps,settings,logout,list,userInfo,language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        logout = findViewById(R.id.logout_btn);
        list = findViewById(R.id.list_btn);
        language = findViewById(R.id.changelang_btn);
            //String[] items = {getString(R.string.app_item_camera), getString(R.string.app_item_incidences), getString(R.string.app_item_meter)};
            //AlertDialogHelper.showCheckboxDialog(ListScreen.this, getString(R.string.app_fav_title), items,  recyclerView,ListScreen.this,urlRequest,userId);

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = {getString(R.string.app_login_lang_es), getString(R.string.app_login_lang_en)};
                AlertDialogHelper.changeLanguage(SettingScreen.this, getString(R.string.app_login_lang_title), items,SettingScreen.this);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPreferences.deleteUserId(SettingScreen.this);
                Intent intentLogin = new Intent(SettingScreen.this, LoginScreen.class);
                intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLogin);
                finish();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(SettingScreen.this, ListScreen.class);
                //intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLogin);
                finish();
            }
        });

        userInfo = findViewById(R.id.user_btn);
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SettingScreen.this, UserScreen.class);
                startActivity(intent2);
            }
        });


    }
}