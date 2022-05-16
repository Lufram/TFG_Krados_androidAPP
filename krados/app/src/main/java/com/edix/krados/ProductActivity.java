package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.adapter.ChartAdapter;
import com.edix.krados.entity.Product;
import com.edix.krados.entity.User;
import com.edix.krados.utilities.InputFilterMinMax;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private Product currentProduct;
    private ChartAdapter pAdapter;
    private TextView name;
    private TextView price;
    private TextView info;
    private EditText editTextNumOfProd;
    private RequestQueue queue;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));
        currentProduct = new Product();
        currentProduct.setId(getIntent().getLongExtra("id", 0));
        currentProduct.setName(getIntent().getStringExtra("name"));
        currentProduct.setuPrice(getIntent().getDoubleExtra("price", 0));
        currentProduct.setInfo(getIntent().getStringExtra("info"));

        name = (TextView) findViewById(R.id.current_product_name_text);
        price = (TextView) findViewById(R.id.current_product_price_text);
        info = (TextView) findViewById(R.id.current_product_info_text);
        editTextNumOfProd = findViewById(R.id.current_product_editTextNumber);

        editTextNumOfProd.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});

        queue = Volley.newRequestQueue(this);
        updateUI();
    }

    private void addProductCartVolley (Long cartId, Long productId, int amount, View view ){
        String url = String.format("http://10.0.2.2:8086/krados/cart/productInCart?cartId=%1$s&productId=%2$s&amount=%3$s", cartId, productId, amount);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                goChart(view);
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
        name.setText(currentProduct.getName());
        price.setText(String.valueOf(currentProduct.getuPrice())+ " €");
        info.setText(currentProduct.getInfo());
    }

    public void addProductCart(View view){
        addProductCartVolley(1L, currentProduct.getId(),Integer.parseInt(editTextNumOfProd.getText().toString()), view);

    }

    public void add(View view){
        int initialNumber = Integer.parseInt(editTextNumOfProd.getText().toString());
        if(initialNumber == 100){
            Toast.makeText(getApplicationContext(),"No se pueden añadir más productos",Toast.LENGTH_LONG).show();
        }else{
            int finalNumber = initialNumber + 1;
            editTextNumOfProd.setText(String.valueOf(finalNumber));
        }
    }

    public void subtract(View view){
        int initialNumber = Integer.parseInt(editTextNumOfProd.getText().toString());
        if(initialNumber == 1){
            Toast.makeText(getApplicationContext(),"La cantidad mínima es 1 producto",Toast.LENGTH_LONG).show();
        }else{
            int finalNumber = initialNumber - 1;
            editTextNumOfProd.setText(String.valueOf(finalNumber));
        }
    }
    public void goChart(View view){
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }

    public void backAction(View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("username",currentUser.getUserName());
        startActivity(intent);
    }
}