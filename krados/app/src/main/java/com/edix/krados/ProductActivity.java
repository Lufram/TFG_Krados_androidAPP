package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.edix.krados.adapter.CartAdapter;
import com.edix.krados.entity.Client;
import com.edix.krados.entity.Product;
import com.edix.krados.entity.User;
import com.edix.krados.utilities.InputFilterMinMax;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private Product currentProduct;
    private CartAdapter pAdapter;
    private TextView name;
    private TextView price;
    private TextView info;
    private ImageView image;
    private EditText editTextNumOfProd;
    private RequestQueue queue;
    private User currentUser;
    private Client c;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));
        currentUser.setJwt(getIntent().getStringExtra("jwt"));
        currentProduct = new Product();
        currentProduct.setId(getIntent().getLongExtra("id", 0));
        currentProduct.setName(getIntent().getStringExtra("name"));
        currentProduct.setuPrice(getIntent().getDoubleExtra("price", 0));
        currentProduct.setInfo(getIntent().getStringExtra("info"));
        currentProduct.setUrl(getIntent().getStringExtra("url"));


        image = (ImageView) findViewById(R.id.imageViewProduct);
        name = (TextView) findViewById(R.id.current_product_name_text);
        price = (TextView) findViewById(R.id.current_product_price_text);
        info = (TextView) findViewById(R.id.current_product_info_text);
        editTextNumOfProd = findViewById(R.id.current_product_editTextNumber);

        editTextNumOfProd.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});

        queue = Volley.newRequestQueue(this);
        getUserDataByUsernameVolley(currentUser.getUserName());
        updateUI();
    }

    private void addProductCartVolley(Long cartId, Long productId, int amount, View view) {
        String url = String.format("http://10.0.2.2:8086/krados/cart/productInCart?cartId=%1$s&productId=%2$s&amount=%3$s", cartId, productId, amount);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                goCart(view);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", currentUser.getJwt());

                return params;
            }
        };
        queue.add(request);
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
                    c.setCartId(Long.parseLong(response.getString("cartId")));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", currentUser.getJwt());

                return params;
            }
        };
        queue.add(request);
    }

    private void updateUI() {
        name.setText(currentProduct.getName());
        price.setText(String.valueOf(currentProduct.getuPrice()) + " €");
        info.setText(currentProduct.getInfo());
        Glide.with(getApplicationContext()).load(Uri.parse(currentProduct.getUrl())).into(image);
    }

    public void addProductCart(View view) {
        addProductCartVolley(c.getCartId(), currentProduct.getId(), Integer.parseInt(editTextNumOfProd.getText().toString()), view);

    }

    public void add(View view) {
        int initialNumber = Integer.parseInt(editTextNumOfProd.getText().toString());
        if (initialNumber == 100) {
            Toast toast = new Toast(getApplicationContext());
            View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
            toast.setView(toast_layout);
            TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
            textView.setText("No se pueden añadir más productos");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            int finalNumber = initialNumber + 1;
            editTextNumOfProd.setText(String.valueOf(finalNumber));
        }
    }

    public void subtract(View view) {
        int initialNumber = Integer.parseInt(editTextNumOfProd.getText().toString());
        if (initialNumber == 1) {
            Toast toast = new Toast(getApplicationContext());
            View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
            toast.setView(toast_layout);
            TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
            textView.setText("La cantidad mínima es 1 producto");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            int finalNumber = initialNumber - 1;
            editTextNumOfProd.setText(String.valueOf(finalNumber));
        }
    }

    public void goCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("username", currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        startActivity(intent);
    }

    public void backAction(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        startActivity(intent);
    }

}