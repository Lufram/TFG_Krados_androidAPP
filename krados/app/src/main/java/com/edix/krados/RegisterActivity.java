package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void createAccount(View view) {
        Intent intentSplashToMain = new Intent(this, MainActivity.class);
        startActivity(intentSplashToMain);
    }

    public void validateFields(View view) {
        int error = 0;

        TextInputEditText emailInput = findViewById(R.id.register_input_text_email);
        TextInputEditText passwordInput = findViewById(R.id.register_input_text_password);
        TextInputEditText checkPasswordInput = findViewById(R.id.register_input_text_password_check);
        TextInputEditText nameInput = findViewById(R.id.register_input_text_name);
        TextInputEditText lastNameInput = findViewById(R.id.register_input_text_surname);
        TextInputEditText addressInput = findViewById(R.id.register_input_text_address);
        TextInputEditText cityInput = findViewById(R.id.register_input_text_city);
        TextInputEditText stateInput = findViewById(R.id.register_input_text_state);
        TextInputEditText postalCodeInput = findViewById(R.id.register_input_text_postal_code);

        TextInputLayout passwordLayout = findViewById(R.id.register_input_layout_password);
        TextInputLayout passwordCheckLayout = findViewById(R.id.register_input_layout_password_check);

        passwordLayout.setErrorEnabled(false);
        passwordCheckLayout.setErrorEnabled(false);

        String email = emailInput.getText().toString();
        String pass = passwordInput.getText().toString();
        String passCheck = checkPasswordInput.getText().toString();

        if (email.equals("") || pass.equals("") || passCheck.equals("") || nameInput.equals("")
                || lastNameInput.equals("") || addressInput.equals("") || cityInput.equals("")
                || stateInput.equals("") || postalCodeInput.equals("")) {
            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_LONG).show();
            error++;
        }

        if(pass.length() < 6){
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Debe que tener 6 caracteres como mínimo");
            error++;
            if(!passCheck.equals(pass)){
                passwordCheckLayout.setErrorEnabled(true);
                passwordCheckLayout.setError("Las contraseñas deben coincidir");
                error++;
            }
        }else if(!passCheck.equals(pass)){
            passwordCheckLayout.setErrorEnabled(true);
            passwordCheckLayout.setError("Las contraseñas deben coincidir");
            error++;
        }

        if (error == 0) {
            createAccount(view);
        }
    }
}