package com.example.traficoreto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traficoreto.helpers.MyPreferences;
import com.example.traficoreto.helpers.UsersHelpers;

public class LoginScreen extends AppCompatActivity {
    private EditText mail,password;
    private Button signIn;
    private TextView registerLink;
    String url ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        url = MyPreferences.getUrlRequest(context);
        mail = findViewById(R.id.editTextText);
        password = findViewById(R.id.editTextText2);
        signIn = findViewById( R.id.button);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mail.getText().toString().isEmpty()) {
                    // Check if mail is empty
                    Toast.makeText(context, "Ingrese un correo electrónico", Toast.LENGTH_LONG).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {
                    // Check if mail is valid
                    Toast.makeText(context, "Ingrese un correo electrónico válido", Toast.LENGTH_LONG).show();
                }else if (password.getText().toString().isEmpty()) {
                    // Check if password is empty
                    Toast.makeText(context, "Ingrese una contraseña válida", Toast.LENGTH_LONG).show();
                }else {
                   //Login method
                    UsersHelpers.login(url,"login",mail.getText().toString(),password.getText().toString(),context);
                }

            }
        });

        registerLink = findViewById(R.id.register_button);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(intent);
            }
        });


    }
}