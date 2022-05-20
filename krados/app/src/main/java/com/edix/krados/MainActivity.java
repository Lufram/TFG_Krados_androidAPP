package com.edix.krados;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.adapter.ProductAdapter;
import com.edix.krados.entity.Product;
import com.edix.krados.entity.User;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listProductContainer;
    private RequestQueue queue;
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter pAdapter;
    private User currentUser;
    private FloatingActionButton boton;
    private Button infoButton;
    private String url;
    private Product p;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));
        currentUser.setJwt(getIntent().getStringExtra("jwt"));

        infoButton = findViewById(R.id.info_button);
        listProductContainer = (ListView) findViewById(R.id.product_container);
        boton = findViewById(R.id.fab);

        url = "https://www.razer.com/campaigns/halo-infinite";
        findViewById(R.id.topContainerAppBar).bringToFront();
        findViewById(R.id.bottomNavigationView).setBackground(null);
        boton.setColorFilter(Color.WHITE);
        findViewById(R.id.page_1);

        queue = Volley.newRequestQueue(this);
        getDataVolley();
        updateUI();
    }



    private void getDataVolley (){
        String url = "http://10.0.2.2:8086/krados/products/offer";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try{
                    productList.clear();
                    for(int i=0; i<response.length(); i++){
                        p = new Product();
                        JSONObject jresponse = response.getJSONObject(i);
                        p.setId(Long.parseLong(jresponse.getString("id")));
                        p.setName(jresponse.getString("name"));
                        p.setInfo(jresponse.getString("info"));
                        p.setuPrice(Double.parseDouble(jresponse.getString("uPrice")));
                        p.setUrl(jresponse.getString("url"));
                        productList.add(p);
                    }
                    updateUI();

                }catch(JSONException e){
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
    
    private Product getDataById(long id){
        for (Product p: productList) {
            if(p.getId().equals(id)){
                return p;
            }
        }
        return null;
    }

    private Product getDataByName(String name){
        for (Product p: productList) {
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    private void updateUI(){
        if(productList.isEmpty()){
            listProductContainer.setAdapter(null);
        } else {
            pAdapter = new ProductAdapter(this, productList);
            listProductContainer.setAdapter(pAdapter);

        }
    }

    public void viewProduct(View view){
        View parent = (View) view.getParent();
        TextView textNameProduct = parent.findViewById(R.id.product_name_text);
        Product p = getDataByName(textNameProduct.getText().toString());

        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("id",p.getId());
        intent.putExtra("name",p.getName());
        intent.putExtra("price",p.getuPrice());
        intent.putExtra("info",p.getInfo());
        intent.putExtra("url", p.getUrl());

        startActivity(intent);
    }

    public void goUserActivity(MenuItem menu){
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("url", p.getUrl());
        startActivity(intent);
    }

    public void goCart(View view){
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("url", p.getUrl());
        startActivity(intent);
    }

    public void goBack(MenuItem menu){

    }

    public void goSearch(MenuItem menu){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("url", p.getUrl());
        startActivity(intent);
    }

    public void goCategory1(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","1");
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("url", p.getUrl());
        startActivity(intent);
    }

    public void goCategory2(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","2");
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("url", p.getUrl());
        startActivity(intent);
    }

    public void goCategory3(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","3");
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("url", p.getUrl());
        startActivity(intent);
    }
    public void goCategory4(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","4");
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("url", p.getUrl());
        startActivity(intent);
    }
    public void goCategory5(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","5");
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("url", p.getUrl());
        startActivity(intent);
    }
    public void goCategory6(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","6");
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("jwt", currentUser.getJwt());
        intent.putExtra("url", p.getUrl());
        startActivity(intent);
    }

    public void goInfoPage(View view){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}