package com.edix.krados;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.adapter.ProductAdapter;
import com.edix.krados.entity.Product;
import com.edix.krados.entity.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listProductContainer;
    private RequestQueue queue;
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter pAdapter;
    private User currentUser;
    private FloatingActionButton boton;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));

        listProductContainer = (ListView) findViewById(R.id.product_container);
        boton = findViewById(R.id.fab);

        findViewById(R.id.topContainerAppBar).bringToFront();
        findViewById(R.id.bottomNavigationView).setBackground(null);
        boton.setColorFilter(Color.WHITE);

        queue = Volley.newRequestQueue(this);
        getDataVolley();
        updateUI();
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

    public void goChart(View view){
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }

    public void goBack(MenuItem menu){

    }

    public void goSearch(MenuItem menu){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }

    public void goCategory1(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","1");
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }

    public void goCategory2(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","2");
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }

    public void goCategory3(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","3");
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }
    public void goCategory4(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","4");
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }
    public void goCategory5(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","5");
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }
    public void goCategory6(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId","6");
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }
}