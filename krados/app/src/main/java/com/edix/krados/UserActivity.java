package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        findViewById(R.id.bottomNavigationView).setBackground(null);
        FloatingActionButton boton = findViewById(R.id.fab);
        boton.setColorFilter(Color.WHITE);
    }

    public void backAction(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void goUserActivity(MenuItem menu){
    }

    public void goBack(MenuItem menu){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void closeSesion(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void goChart(View view){
        Intent intent = new Intent(this, ChartActivity.class);
        startActivity(intent);
    }
}