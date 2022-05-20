package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.entity.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText currentPassword;
    private TextInputEditText newPasswordInput;
    private TextInputEditText checkPasswordInput;
    private TextInputLayout currentPasswordLayout;
    private TextInputLayout newPasswordLayout;
    private TextInputLayout passwordCheckLayout;
    private User currentUser;
    private RequestQueue queue;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));
        currentUser.setJwt(getIntent().getStringExtra("jwt"));

        currentPassword = findViewById(R.id.change_password_input_text_current_password);
        newPasswordInput = findViewById(R.id.change_password_input_text_new_password);
        checkPasswordInput = findViewById(R.id.change_password_input_text_check_password);
        currentPasswordLayout = findViewById(R.id.change_password_layout_current_password);
        newPasswordLayout = findViewById(R.id.change_password_layout_new_password);
        passwordCheckLayout = findViewById(R.id.change_password_layout_check_password);

        queue = Volley.newRequestQueue(this);
    }

    private void changePasswordVolley (HashMap hasMap) {
        String url = "http://10.0.2.2:8086/krados/updatePassword";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(hasMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast = new Toast(ChangePasswordActivity.this);
                View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_correct, (ViewGroup) findViewById(R.id.correct_toast));
                toast.setView(toast_layout);
                TextView textView = (TextView) toast_layout.findViewById(R.id.toastCorrectMessage);
                textView.setText("Contraseña modificada con exito");
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 400){
                    Toast toast = new Toast(ChangePasswordActivity.this);
                    View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
                    toast.setView(toast_layout);
                    TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
                    textView.setText("La contraseña no es correcta");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = new Toast(ChangePasswordActivity.this);
                    View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
                    toast.setView(toast_layout);
                    TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
                    textView.setText("No se ha podido modificar la contraseña");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", currentUser.getJwt());

                return params;
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(request);
    }

    public void validateFields(View view){
        int error = 0;

        newPasswordLayout.setErrorEnabled(false);
        passwordCheckLayout.setErrorEnabled(false);

        String currentPass = currentPassword.getText().toString();
        String newPass = newPasswordInput.getText().toString();
        String passCheck = checkPasswordInput.getText().toString();

        if (newPass.equals("") || passCheck.equals("") || currentPass.equals("") ) {
            Toast toast = new Toast(this);
            View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
            toast.setView(toast_layout);
            TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
            textView.setText("Debes completar todos los campos");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            error++;
        }

        if(newPass.length() < 6){
            newPasswordLayout.setErrorEnabled(true);
            newPasswordLayout.setError("Debe que tener 6 caracteres como mínimo");
            newPasswordLayout.requestFocus();
            error++;
            if(!passCheck.equals(newPass)){
                passwordCheckLayout.setErrorEnabled(true);
                passwordCheckLayout.setError("Las contraseñas deben coincidir");
                passwordCheckLayout.requestFocus();
                error++;
            }
        }else if(!passCheck.equals(newPass)){
            passwordCheckLayout.setErrorEnabled(true);
            passwordCheckLayout.setError("Las contraseñas deben coincidir");
            passwordCheckLayout.requestFocus();
            error++;
        }

        if (error == 0) {
            hashMap = new HashMap<>();
            hashMap.put("username", currentUser.getUserName());
            hashMap.put("oldPassword", currentPass);
            hashMap.put("newPassword", newPass);
            currentPassword.setText("");
            newPasswordInput.setText("");
            checkPasswordInput.setText("");
            currentPasswordLayout.requestFocus();
            changePasswordVolley(hashMap);
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        startActivity(intent);
    }
}