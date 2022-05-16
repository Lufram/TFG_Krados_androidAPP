package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class InfoPurchaseActivity extends AppCompatActivity {

    private User currentUser;
    private Client client;
    private TextView totalPriceText;
    private ListView listInfoPurchaseContainer;
    private InfoPurchaseAdapter pAdapter;
    private Product product;
    private RequestQueue queue;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_purchase);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));
        client = new Client();
        client.setId(getIntent().getLongExtra("clientId", 0));

        totalPriceText = findViewById(R.id.info_purchase_total_product_price_text);
        listInfoPurchaseContainer = findViewById(R.id.info_purchase_container);

        totalPriceText.setText(String.valueOf(getIntent().getDoubleExtra("totalPrice", 0)) + "€");

        queue = Volley.newRequestQueue(this);
        getInfoPurchaseByClientIdVolley(getIntent().getLongExtra("id", 0));
        updateUI();
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
        });
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

    public void goBack(View view) {
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.putExtra("username", currentUser.getUserName());
        intent.putExtra("id", client.getId());

        startActivity(intent);
    }
}