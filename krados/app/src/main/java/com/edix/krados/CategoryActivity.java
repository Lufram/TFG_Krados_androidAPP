package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ListView listProductContainerCategory;
    private BottomAppBar bottomAppBar;
    private List<Product> productList = new ArrayList<>();
    private RequestQueue queue;
    private ProductAdapter pAdapter;
    private String categoryId;
    private Toolbar toolbar;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));
        categoryId = getIntent().getStringExtra("categoryId");

        listProductContainerCategory = findViewById(R.id.product_container_category);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        FloatingActionButton boton = findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.topAppBarSearch);


        findViewById(R.id.bottomNavigationView).setBackground(null);
        boton.setColorFilter(Color.WHITE);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);
        getCategoryByIdVolley(categoryId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarsearch, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search_search_activity);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                getProductByCategoryIdAndNameVolley(categoryId, s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void getCategoryByIdVolley (String id){
        String url = "http://10.0.2.2:8086/krados/products/findByCategory/" + id;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try{
                    productList.clear();
                    for(int i=0; i<response.length(); i++){
                        JSONObject jresponse = response.getJSONObject(i);
                        Product p = new Product();
                        p.setId(Long.parseLong(jresponse.getString("id")));
                        p.setName(jresponse.getString("name"));
                        p.setInfo(jresponse.getString("info"));
                        p.setuPrice(Double.parseDouble(jresponse.getString("uPrice")));
                        productList.add(p);
                    }
                    updateUI();

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
    }


    private void getProductByCategoryIdAndNameVolley (String id, String name ) {
        String url = String.format("http://10.0.2.2:8086/krados/products/findByCategoryAndName?categoryId=%1$s&name=%2$s", id, name);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    productList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jresponse = response.getJSONObject(i);
                        Product p = new Product();
                        p.setId(Long.parseLong(jresponse.getString("id")));
                        p.setName(jresponse.getString("name"));
                        p.setInfo(jresponse.getString("info"));
                        p.setuPrice(Double.parseDouble(jresponse.getString("uPrice")));
                        productList.add(p);
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

        private void updateUI(){
        if(productList.isEmpty()){
            listProductContainerCategory.setAdapter(null);

        } else {
            pAdapter = new ProductAdapter(this, productList);
            listProductContainerCategory.setAdapter(pAdapter);

        }
    }

    private Product getDataByName(String name){
        for (Product p: productList) {
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public void viewProduct(View view){
        View parent = (View) view.getParent();
        TextView textNameProduct = parent.findViewById(R.id.product_name_text);
        Product p = getDataByName(textNameProduct.getText().toString());

        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        intent.putExtra("id",p.getId());
        intent.putExtra("name",p.getName());
        intent.putExtra("price",p.getuPrice());
        intent.putExtra("info",p.getInfo());

        startActivity(intent);
    }

    public void goUserActivity(MenuItem menu){
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }

    public void goBack(MenuItem menu){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }

    public void goChart(View view){
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }
}