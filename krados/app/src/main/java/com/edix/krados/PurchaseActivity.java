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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.adapter.ProductAdapter;
import com.edix.krados.adapter.PurchaseAdapter;
import com.edix.krados.entity.Client;
import com.edix.krados.entity.Product;
import com.edix.krados.entity.Purchase;
import com.edix.krados.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity {

    private ListView listPurchaseContainer;
    private User currentUser;
    private Client c;
    private Purchase purchase;
    private List<Purchase> purchaseList = new ArrayList();
    private PurchaseAdapter pAdapter;
    private RequestQueue queue;
    private SimpleDateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));
        c = new Client();
        c.setId(getIntent().getLongExtra("id", 0));

        listPurchaseContainer = findViewById(R.id.purchase_container);

        format = new SimpleDateFormat("yyyy-MM-dd");
        queue = Volley.newRequestQueue(this);
        getPurchaseDataByClientIdVolley(c.getId());
        updateUI();
    }

    private void getPurchaseDataByClientIdVolley(Long clientId) {

        String url = "http://10.0.2.2:8086/krados/purchase/" + clientId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    purchase = null;
                    purchaseList.clear();
                    for(int i=0; i<response.length(); i++) {
                        JSONObject jresponse = response.getJSONObject(i);
                        purchase = new Purchase();
                        purchase.setId(Long.parseLong(jresponse.getString("id")));
                        purchase.setPurchaseDate(format.parse(jresponse.getString("purchaseDate")));
                        purchase.setStatus(jresponse.getString("status"));
                        purchase.setTotalPrice(Double.parseDouble(jresponse.getString("totalPrice")));
                        purchaseList.add(purchase);
                    }
                    updateUI();

                } catch (JSONException | ParseException e) {
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

    private void updateUI(){
        if(purchaseList.isEmpty()){
            listPurchaseContainer.setAdapter(null);
        } else {
            pAdapter = new PurchaseAdapter(this, purchaseList);
            listPurchaseContainer.setAdapter(pAdapter);

        }
    }

    public Purchase searchPurchaseById(Long id, List<Purchase> purchaseList){
        for(Purchase p: purchaseList){
            if(p.getId() == id){
                return p;
            }
        }
        return null;
    }

    public void goBack(View view){
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }

    public void goInfoPurchase(View view){
        View parent = (View) view.getParent();
        TextView idText = parent.findViewById(R.id.id_tag);
        Purchase p = searchPurchaseById(Long.parseLong(idText.getText().toString()), purchaseList);

        Intent intent = new Intent(this, InfoPurchaseActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("id", p.getId());
        intent.putExtra("clientId", c.getId());
        intent.putExtra("totalPrice", p.getTotalPrice());

        startActivity(intent);
    }
}