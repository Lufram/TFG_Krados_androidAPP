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
import com.edix.krados.entity.Address;
import com.edix.krados.entity.Client;
import com.edix.krados.entity.User;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserActivity extends AppCompatActivity {

    private Button myPurchaseButton;
    private Button changePasswordButton;
    private Button closeSesionButton;
    private Button modifyButton;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton boton;
    private ScrollView infoProfileScroll;
    private TextInputEditText emailInput;
    private TextInputEditText nameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText addressInput;
    private TextInputEditText cityInput;
    private TextInputEditText stateInput;
    private TextInputEditText postalCodeInput;
    private TextInputLayout postalCodeLayout;
    private Boolean isPressed;
    private User currentUser;
    private Client c;
    private RequestQueue queue;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));
        currentUser.setJwt(getIntent().getStringExtra("jwt"));

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
        postalCodeLayout = findViewById(R.id.user_input_layout_postal_code);

        findViewById(R.id.bottomNavigationView).setBackground(null);
        boton.setColorFilter(Color.WHITE);
        queue = Volley.newRequestQueue(this);
        getUserDataByUsernameVolley(currentUser.getUserName());
    }

    private void getUserDataByUsernameVolley(String username) {

        String url = String.format("http://10.0.2.2:8086/krados/client?userName=%1$s", username);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    c = null;
                    JSONObject jresponse = response.getJSONObject("address");
                    c = new Client();
                    Address address = new Address();
                    address.setRoadName(jresponse.getString("roadName"));
                    address.setCityName(jresponse.getString("cityName"));
                    address.setStateName(jresponse.getString("extraInfo"));
                    address.setPostalCode(jresponse.getString("postalCode"));

                    c.setId(Long.parseLong(response.getString("id")));
                    c.setName(response.getString("name"));
                    c.setSurname(response.getString("surname"));
                    c.setAddress(address);

                    setInitialValues();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", currentUser.getJwt());

                return params;
            }
        };
        queue.add(request);
    }

    private void changeUserVolley (HashMap hasMap) {
        String url = "http://10.0.2.2:8086/krados/client/update";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(hasMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast = new Toast(UserActivity.this);
                View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_correct, (ViewGroup) findViewById(R.id.correct_toast));
                toast.setView(toast_layout);
                TextView textView = (TextView) toast_layout.findViewById(R.id.toastCorrectMessage);
                textView.setText("Usuario modificado con exito");
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                setAllSave();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast toast = new Toast(UserActivity.this);
                    View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
                    toast.setView(toast_layout);
                    TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
                    textView.setText("No se ha podido modificar el usuario");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
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
        bottomAppBar.setVisibility(View.GONE);
        boton.setVisibility(View.GONE);
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
        findViewById(R.id.bottomNavigationView).setBackground(null);
        modifyButton.setText("MODIFICAR");
        nameInput.setEnabled(false);
        lastNameInput.setEnabled(false);
        addressInput.setEnabled(false);
        cityInput.setEnabled(false);
        stateInput.setEnabled(false);
        postalCodeInput.setEnabled(false);
        isPressed = false;
    }

    private boolean validateIsNumber(String number){
        try{
            Integer.parseInt(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public void validateFields() {
        int error = 0;

        postalCodeLayout.setErrorEnabled(false);

        String name = nameInput.getText().toString();
        String lastname = lastNameInput.getText().toString();
        String address = addressInput.getText().toString();
        String city = cityInput.getText().toString();
        String state = stateInput.getText().toString();
        String postalCode = postalCodeInput.getText().toString();

        if (name.equals("")
                || lastname.equals("") || address.equals("") || city.equals("")
                || state.equals("") || postalCode.equals("")) {
            Toast toast = new Toast(getApplicationContext());
            View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
            toast.setView(toast_layout);
            TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
            textView.setText("Debes completar todos los campos");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            error++;
        }
        if(!validateIsNumber(postalCode)){
            postalCodeLayout.setErrorEnabled(true);
            postalCodeLayout.setError("EL codigo postal tiene que se un numero");
            postalCodeLayout.requestFocus();
            error++;
        }

        if (error == 0) {
            hashMap = new HashMap<>();
            hashMap.put("id", String.valueOf(c.getId()));
            hashMap.put("name", name );
            hashMap.put("surname", lastname);
            hashMap.put("roadName", address);
            hashMap.put("cityName", city);
            hashMap.put("extraInfo", state);
            hashMap.put("postalCode", postalCode);
            changeUserVolley(hashMap);
        }
    }

    private void setInitialValues() {
        emailInput.setText(currentUser.getUserName());
        nameInput.setText(c.getName());
        lastNameInput.setText(c.getSurname());
        addressInput.setText(c.getAddress().getRoadName());
        cityInput.setText(c.getAddress().getCityName());
        stateInput.setText(c.getAddress().getStateName());
        postalCodeInput.setText(String.valueOf(c.getAddress().getPostalCode()));
    }

    public void backAction(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        startActivity(intent);
    }

    public void goUserActivity(MenuItem menu) {
    }

    public void goChangePassword(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("username", currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        startActivity(intent);
    }

    public void goBack(MenuItem menu) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        startActivity(intent);
    }

    public void closeSesion(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    public void goCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("username", currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        startActivity(intent);
    }

    public void goPurschase(View view) {
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.putExtra("username", currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("id", c.getId());
        startActivity(intent);
    }
}