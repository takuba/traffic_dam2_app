package com.example.traficoreto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.traficoreto.helpers.UsersHelpers;

public class RegisterScreen extends AppCompatActivity {
    private EditText name,city,country,age,street,mail,password,repeatedpassword;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Context context = getApplicationContext();
        UsersHelpers users = new UsersHelpers(context,this);

        name = findViewById(R.id.editTex_register_name);
        city = findViewById(R.id.editTex_register_city);
        country = findViewById(R.id.editTex_register_country);
        age = findViewById(R.id.editTex_register_age);
        street = findViewById(R.id.editTex_register_street);
        mail = findViewById(R.id.editTex_register_mail);
        password = findViewById(R.id.editTex_register_password);
        repeatedpassword = findViewById(R.id.editTex_register_repeated_password);
        sendButton = findViewById(R.id.register_button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword(users,name.getText().toString(),city.getText().toString(),country.getText().toString(),age.getText().toString(),street.getText().toString(),mail.getText().toString(),password.getText().toString(),repeatedpassword.getText().toString());
            }
        });
    }

    void checkPassword(UsersHelpers users, String name, String city, String country, String age, String street, String mail, String password, String repeatedPass){
        if (password.equals(repeatedPass))
        {
            //Toast.makeText(this,"Correct password",Toast.LENGTH_SHORT).show();
            Log.d("fernando",name);
            Log.d("fernando",city);
            Log.d("fernando",country);
            Log.d("fernando",age);
            Log.d("fernando",street);
            Log.d("fernando",mail);
            Log.d("fernando",password);
            Log.d("fernando",repeatedPass);
            users.register( mail,  password, name,  city,  country,  Integer.parseInt(age), street);
        }else {
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();

        }
    }
}