package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText user;
    private TextInputEditText password;
    private RequestQueue queue;
    private String jwt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.userBox);
        password = findViewById(R.id.passBox);

        queue = Volley.newRequestQueue(this);
    }

    private void loginUserVolley (String email, String password, View view) {
        String url = String.format("http://10.0.2.2:8086/krados/login?username=%1$s&password=%2$s", email, password);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jwt = "Bearer " + response.getString("access_token");
                    login(view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 403){
                    Toast toast = new Toast(LoginActivity.this);
                    View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
                    toast.setView(toast_layout);
                    TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
                    textView.setText("El usuario o la contraseña no son correctos");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(request);
    }

    public void login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username",user.getText().toString().trim().toLowerCase());
        intent.putExtra("jwt", jwt);
        startActivity(intent);
        finish();
    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void validateFields(View view) {
        int error = 0;

        String name = user.getText().toString().toLowerCase();
        String pass = password.getText().toString();

        if (name.equals("") || pass.equals("")) {
            Toast toast = new Toast(this);
            View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
            toast.setView(toast_layout);
            TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
            textView.setText("Debes completar todos los campos");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            error++;
        }

        if (error == 0) {
            loginUserVolley(name, pass, view);
        }
    }

}