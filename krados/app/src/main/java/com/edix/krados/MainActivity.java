package com.edix.krados;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.entity.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.edix.krados.databinding.ActivityMainBinding;

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
                        //System.out.println(p.toString());
                    }
                    System.out.println(productList);
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
            //ProductListAdapter adapter = new ProductListAdapter(this, R.layout.product_list_item, productList);
            ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, R.layout.product_list_item, R.id.product_name_text, getListNames());
            listProductContainer.setAdapter(myAdapter);

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
}