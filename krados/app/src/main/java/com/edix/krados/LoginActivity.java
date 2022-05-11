package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        Intent intentSplashToLogin = new Intent(this, MainActivity.class);
        startActivity(intentSplashToLogin);
        finish();
    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void validateFields(View view) {
        int error = 0;

        TextInputEditText user = findViewById(R.id.userBox);
        TextInputEditText password = findViewById(R.id.passBox);

        String name = user.getText().toString();
        String pass = password.getText().toString();

        if (name.equals("") || pass.equals("")) {
            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_LONG).show();
            error++;
        }

        if (error == 0) {
            login(view);
        }
    }

}