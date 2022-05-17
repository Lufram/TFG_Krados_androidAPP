package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.entity.Register;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText checkPasswordInput;
    private TextInputEditText nameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText addressInput;
    private TextInputEditText cityInput;
    private TextInputEditText stateInput;
    private TextInputEditText postalCodeInput;
    private TextInputLayout passwordLayout;
    private TextInputLayout passwordCheckLayout;
    private RequestQueue queue;
    private Register newUserRegister;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.register_input_text_email);
        passwordInput = findViewById(R.id.register_input_text_password);
        checkPasswordInput = findViewById(R.id.register_input_text_password_check);
        nameInput = findViewById(R.id.register_input_text_name);
        lastNameInput = findViewById(R.id.register_input_text_surname);
        addressInput = findViewById(R.id.register_input_text_address);
        cityInput = findViewById(R.id.register_input_text_city);
        stateInput = findViewById(R.id.register_input_text_state);
        postalCodeInput = findViewById(R.id.register_input_text_postal_code);
        passwordLayout = findViewById(R.id.register_input_layout_password);
        passwordCheckLayout = findViewById(R.id.register_input_layout_password_check);

        queue = Volley.newRequestQueue(this);
    }


    private void registerNewUserVolley ( HashMap hasMap, View view) {
        String url = "http://10.0.2.2:8086/krados/register";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hasMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                createAccount(view);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 400){
                    Toast.makeText(RegisterActivity.this, "Ya existe este correo", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "No se ha podido realizar el registro", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(request);
    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void validateFields(View view) {
        int error = 0;

        passwordLayout.setErrorEnabled(false);
        passwordCheckLayout.setErrorEnabled(false);

        String email = emailInput.getText().toString();
        String pass = passwordInput.getText().toString();
        String passCheck = checkPasswordInput.getText().toString();
        String name = nameInput.getText().toString();
        String lastname = lastNameInput.getText().toString();
        String address = addressInput.getText().toString();
        String city = cityInput.getText().toString();
        String state = stateInput.getText().toString();
        String postalCode = postalCodeInput.getText().toString();

        if (email.equals("") || pass.equals("") || passCheck.equals("") || name.equals("")
                || lastname.equals("") || address.equals("") || city.equals("")
                || state.equals("") || postalCode.equals("")) {
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
            hashMap = new HashMap<>();
            hashMap.put("username", email);
            hashMap.put("password", pass);
            hashMap.put("name", name);
            hashMap.put("lastname", lastname);
            hashMap.put("roadName", address);
            hashMap.put("roadNum", String.valueOf(100));
            hashMap.put("city", city);
            hashMap.put("state", state);
            hashMap.put("postalCode", postalCode);

            registerNewUserVolley(hashMap, view);
        }
    }
}