package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.edix.krados.adapter.CurrentProductAdapter;
import com.edix.krados.adapter.ProductAdapter;
import com.edix.krados.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private Product currentProduct;
    private CurrentProductAdapter pAdapter;
    private TextView name;
    private TextView price;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        currentProduct = new Product();
        currentProduct.setId(getIntent().getLongExtra("id", 0));
        currentProduct.setName(getIntent().getStringExtra("name"));
        currentProduct.setuPrice(getIntent().getDoubleExtra("price", 0));
        currentProduct.setInfo(getIntent().getStringExtra("info"));
        name = (TextView) findViewById(R.id.current_product_name_text);
        price = (TextView) findViewById(R.id.current_product_price_text);
        info = (TextView) findViewById(R.id.current_product_info_text);
        updateUI();
    }

    public void backAction(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void updateUI(){
        name.setText(currentProduct.getName());
        price.setText(String.valueOf(currentProduct.getuPrice()));
        info.setText(currentProduct.getInfo());
    }
}