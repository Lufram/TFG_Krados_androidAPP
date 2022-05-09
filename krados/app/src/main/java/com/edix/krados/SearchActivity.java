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


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.adapter.ProductAdapter;
import com.edix.krados.entity.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView listProductContainer;
    private BottomAppBar bottomAppBar;

    List<Product> productList = new ArrayList<>();
    private RequestQueue queue;
    private ProductAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        findViewById(R.id.bottomNavigationView).setBackground(null);
        FloatingActionButton boton = findViewById(R.id.fab);
        boton.setColorFilter(Color.WHITE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBarSearch);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(this);
        listProductContainer = findViewById(R.id.product_container_search);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarsearch, menu);
        final MenuItem searchItem = menu.findItem(R.id.app_bar_search_search_activity);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                getDataByNameVolley(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void getDataByNameVolley (String name){
        String url = "http://10.0.2.2:8086/krados/products/findByName/" + name;
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
                        p.setCategory(Integer.parseInt(jresponse.getJSONObject("category").getString("id")));
                        productList.add(p);
                    }
                    updateUI();

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, error -> {
            System.out.println(error);
        });
        queue.add(request);
    }

        private void updateUI(){
        if(productList.isEmpty()){
            listProductContainer.setAdapter(null);
        } else {
            pAdapter = new ProductAdapter(this, productList);
            listProductContainer.setAdapter(pAdapter);

        }
    }

    public void goBack(MenuItem menu){
        System.out.println("LLEGA AQUI");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}