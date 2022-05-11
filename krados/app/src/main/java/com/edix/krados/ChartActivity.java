package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.edix.krados.adapter.ChartAdapter;
import com.edix.krados.adapter.ProductAdapter;
import com.edix.krados.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private ListView listProductContainer;
    private List<Product> productList = new ArrayList<>();
    private ChartAdapter pAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        listProductContainer = (ListView) findViewById(R.id.product_container);
    }

    
    private void updateUI(){
        if(productList.isEmpty()){
            listProductContainer.setAdapter(null);
        } else {
            pAdapter = new ChartAdapter(this, productList);
            listProductContainer.setAdapter(pAdapter);
        }
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}