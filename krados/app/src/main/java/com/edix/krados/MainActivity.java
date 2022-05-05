package com.edix.krados;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.adapter.ProductAdapter;
import com.edix.krados.entity.Product;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //private ActivityMainBinding binding;
    private ListView listProductContainer;
    private RequestQueue queue;
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter pAdapter;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();

        queue = Volley.newRequestQueue(this);
        listProductContainer = (ListView) findViewById(R.id.product_container);
        getDataVolley();
        updateUI();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu( menu);
    }

    private void getDataVolley (){
        String url = "http://10.0.2.2:8086/krados/products/";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try{
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
            String names[] = getListNames();
            //ProductListAdapter adapter = new ProductListAdapter(this, R.layout.product_list_item, productList);
            pAdapter = new ProductAdapter(this, productList);
            listProductContainer.setAdapter(pAdapter);

        }
    }

    private String[] getListNames(){
        String[] names;
        if(productList.isEmpty()){
            return null;
        }else {
            names = new String[productList.size()];
            for (int i=0;i<productList.size();i++) {
                names[i] = productList.get(i).getName();
            }
            return names;
        }
    }

    public void viewProduct(View view){
        View parent = (View) view.getParent();
        TextView textNameProduct = parent.findViewById(R.id.product_name_text);
        Product p = getDataByName(textNameProduct.getText().toString());

        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("id",p.getId());
        intent.putExtra("name",p.getName());
        intent.putExtra("price",p.getuPrice());
        intent.putExtra("info",p.getInfo());

        startActivity(intent);
    }
}