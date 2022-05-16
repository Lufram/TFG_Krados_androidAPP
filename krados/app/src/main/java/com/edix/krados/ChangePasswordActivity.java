package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edix.krados.entity.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText currentPassword;
    private TextInputEditText newPasswordInput;
    private TextInputEditText checkPasswordInput;
    private TextInputLayout newPasswordLayout;
    private TextInputLayout passwordCheckLayout;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));

        currentPassword = findViewById(R.id.change_password_input_text_current_password);
        newPasswordInput = findViewById(R.id.change_password_input_text_new_password);
        checkPasswordInput = findViewById(R.id.change_password_input_text_check_password);
        newPasswordLayout = findViewById(R.id.change_password_layout_new_password);
        passwordCheckLayout = findViewById(R.id.change_password_layout_check_password);
    }

    public void validateFields(View view){
        int error = 0;

        newPasswordLayout.setErrorEnabled(false);
        passwordCheckLayout.setErrorEnabled(false);

        String currentPass = currentPassword.getText().toString();
        String newPass = newPasswordInput.getText().toString();
        String passCheck = checkPasswordInput.getText().toString();

        if (newPass.equals("") || passCheck.equals("") || currentPass.equals("") ) {
            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_LONG).show();
            error++;
        }

        if(newPass.length() < 6){
            newPasswordLayout.setErrorEnabled(true);
            newPasswordLayout.setError("Debe que tener 6 caracteres como mínimo");
            error++;
            if(!passCheck.equals(newPass)){
                passwordCheckLayout.setErrorEnabled(true);
                passwordCheckLayout.setError("Las contraseñas deben coincidir");
                error++;
            }
        }else if(!passCheck.equals(newPass)){
            passwordCheckLayout.setErrorEnabled(true);
            passwordCheckLayout.setError("Las contraseñas deben coincidir");
            error++;
        }

        if (error == 0) {
            currentPassword.setText("");
            newPasswordInput.setText("");
            checkPasswordInput.setText("");
            Toast.makeText(this, "La contraseña se ha actualizado con exito", Toast.LENGTH_LONG).show();
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }
}