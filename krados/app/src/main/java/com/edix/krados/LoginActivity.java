package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        Intent intentSplashToMain = new Intent(this,MainActivity.class);
        startActivity(intentSplashToMain);
    }

    public void createAccount(View view){
        Intent intentSplashToMain = new Intent(this,RegisterActivity.class);
        startActivity(intentSplashToMain);
    }

}