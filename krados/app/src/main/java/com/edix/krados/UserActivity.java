package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class UserActivity extends AppCompatActivity {

    Button myPurchaseButton;
    Button changePasswordButton;
    Button closeSesionButton;
    Button modifyButton;
    BottomAppBar bottomAppBar;
    FloatingActionButton boton;
    ScrollView infoProfileScroll;
    TextInputEditText emailInput;
    TextInputEditText nameInput;
    TextInputEditText lastNameInput;
    TextInputEditText addressInput;
    TextInputEditText cityInput;
    TextInputEditText stateInput;
    TextInputEditText postalCodeInput;
    Boolean isPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        isPressed = false;
        bottomAppBar = findViewById(R.id.bottomAppBar);
        modifyButton = findViewById(R.id.button_modify);
        myPurchaseButton = findViewById(R.id.purchase_button);
        changePasswordButton = findViewById(R.id.change_password_button);
        closeSesionButton = findViewById(R.id.logout_button);
        infoProfileScroll = findViewById(R.id.info_profile_scroll);
        boton = findViewById(R.id.fab);
        emailInput = findViewById(R.id.profile_input_text_email);
        nameInput = findViewById(R.id.profile_input_text_name);
        lastNameInput = findViewById(R.id.profile_input_text_surname);
        addressInput = findViewById(R.id.profile_input_text_address);
        cityInput = findViewById(R.id.profile_input_text_city);
        stateInput = findViewById(R.id.profile_input_text_state);
        postalCodeInput = findViewById(R.id.profile_input_text_postal_code);

        findViewById(R.id.bottomNavigationView).setBackground(null);
        boton.setColorFilter(Color.WHITE);


    }

    public void modifyProfileData(View view) {
        if (isPressed == false) {
            setAllModify();
        } else {
            validateFields();
        }
    }

    private void setAllModify() {
        myPurchaseButton.setEnabled(false);
        changePasswordButton.setEnabled(false);
        closeSesionButton.setEnabled(false);
        bottomAppBar.setVisibility(View.INVISIBLE);
        boton.setVisibility(View.INVISIBLE);
        modifyButton.setText("GUARDAR");
        nameInput.setEnabled(true);
        nameInput.requestFocus();
        lastNameInput.setEnabled(true);
        addressInput.setEnabled(true);
        cityInput.setEnabled(true);
        stateInput.setEnabled(true);
        postalCodeInput.setEnabled(true);
        isPressed = true;
    }

    private void setAllSave() {
        myPurchaseButton.setEnabled(true);
        changePasswordButton.setEnabled(true);
        closeSesionButton.setEnabled(true);
        bottomAppBar.setVisibility(View.VISIBLE);
        boton.setVisibility(View.VISIBLE);
        modifyButton.setText("MODIFICAR");
        nameInput.setEnabled(false);
        lastNameInput.setEnabled(false);
        addressInput.setEnabled(false);
        cityInput.setEnabled(false);
        stateInput.setEnabled(false);
        postalCodeInput.setEnabled(false);
        isPressed = false;
    }

    public void validateFields() {
        int error = 0;

        String name = nameInput.getText().toString();
        String lastname = lastNameInput.getText().toString();
        String address = addressInput.getText().toString();
        String city = cityInput.getText().toString();
        String state = stateInput.getText().toString();
        String postalCode = postalCodeInput.getText().toString();

        if (name.equals("")
                || lastname.equals("") || address.equals("") || city.equals("")
                || state.equals("") || postalCode.equals("")) {
            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_LONG).show();
            error++;
        }

        if (error == 0) {
            setAllSave();
        }
    }

    public void backAction(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goUserActivity(MenuItem menu) {
    }

    public void goBack(MenuItem menu) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void closeSesion(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void goChart(View view) {
        Intent intent = new Intent(this, ChartActivity.class);
        startActivity(intent);
    }
}