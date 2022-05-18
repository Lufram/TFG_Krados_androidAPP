package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.adapter.InfoPurchaseAdapter;
import com.edix.krados.adapter.PurchaseAdapter;
import com.edix.krados.entity.Client;
import com.edix.krados.entity.Product;
import com.edix.krados.entity.Purchase;
import com.edix.krados.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoPurchaseActivity extends AppCompatActivity {

    private Long purchaseId;
    private Client c = new Client();
    private User currentUser;
    private Client client;
    private TextView totalPriceText;
    private ListView listInfoPurchaseContainer;
    private InfoPurchaseAdapter pAdapter;
    private Product product;
    private RequestQueue queue;
    private List<Product> productList = new ArrayList<>();
    private String pattern = "#.##";
    private DecimalFormat decimalFormat =  new DecimalFormat(pattern);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_purchase);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));
        currentUser.setJwt(getIntent().getStringExtra("jwt"));
        client = new Client();
        client.setId(getIntent().getLongExtra("clientId", 0));
        purchaseId = getIntent().getLongExtra("id", 0);

        totalPriceText = findViewById(R.id.info_purchase_total_product_price_text);
        listInfoPurchaseContainer = findViewById(R.id.info_purchase_container);

        totalPriceText.setText(decimalFormat.format(getIntent().getDoubleExtra("totalPrice", 0)) + "€");

        queue = Volley.newRequestQueue(this);
        getInfoPurchaseByClientIdVolley(getIntent().getLongExtra("id", 0));
        getUserDataByUsernameVolley(currentUser.getUserName());
        updateUI();
    }

    private void getUserDataByUsernameVolley(String username) {

        String url = String.format("http://10.0.2.2:8086/krados/client?userName=%1$s", username);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jresponse = response.getJSONObject("address");
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

    private void transformCartInPurchaseVolley(Long Id, View view) {
        String url = "http://10.0.2.2:8086/krados/purchase/buyAgainPurchaseById/"  + Id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast = new Toast(getApplicationContext());
                View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_correct, (ViewGroup) findViewById(R.id.correct_toast));
                toast.setView(toast_layout);
                TextView textView = (TextView) toast_layout.findViewById(R.id.toastCorrectMessage);
                textView.setText("El pedido se ha realizado con exito");
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                goBack(view);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = new Toast(getApplicationContext());
                View toast_layout = getLayoutInflater().inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.error_toast));
                toast.setView(toast_layout);
                TextView textView = (TextView) toast_layout.findViewById(R.id.toastErrorMessage);
                textView.setText("No se ha podido realizar el pedido");
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
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

    private void getInfoPurchaseByClientIdVolley(Long purchaseId) {

        String url = "http://10.0.2.2:8086/krados/purchase/purchaseById/" + purchaseId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    product = null;
                    productList.clear();
                    for(int i=0; i<response.length(); i++) {
                        JSONObject jresponse = response.getJSONObject(i);
                        product = new Product();
                        product.setName(jresponse.getString("name"));
                        product.setAmount(Integer.parseInt(jresponse.getString("amount")));
                        product.setuPrice(Double.parseDouble(jresponse.getString("uprice")));
                        productList.add(product);
                    }
                    updateUI();

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

    private void updateUI() {
        if (productList.isEmpty()) {
            listInfoPurchaseContainer.setAdapter(null);
        } else {
            pAdapter = new InfoPurchaseAdapter(this, productList);
            listInfoPurchaseContainer.setAdapter(pAdapter);
        }
    }

    public void makeOrderAgain(View view){
        transformCartInPurchaseVolley(purchaseId, view);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("username", currentUser.getUserName());
        intent.putExtra("id", client.getId());

        startActivity(intent);
    }
}