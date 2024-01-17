package com.example.traficoreto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private EditText name,city,country,age,street,mail,password,repeatedpassword;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Context context = getApplicationContext();
        Users users = new Users(context,this);
        password = findViewById(R.id.editTex_register_password);

        repeatedpassword = findViewById(R.id.editTex_register_repeated_password);

        sendButton = findViewById(R.id.register_button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword(users,password.getText().toString(),repeatedpassword.getText().toString());
            }
        });
    }

    void checkPassword(Users users,String password,String repeatedPass){
        if (password.equals(repeatedPass))
        {
            Toast.makeText(this,"Correct password",Toast.LENGTH_SHORT).show();
            users.register(mail.getText().toString(),password.getText().toString());
        }else {
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();

        }
    }
}