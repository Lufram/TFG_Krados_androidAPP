package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edix.krados.adapter.ChartAdapter;
import com.edix.krados.entity.Product;
import com.edix.krados.utilities.InputFilterMinMax;

public class ProductActivity extends AppCompatActivity {

    private Product currentProduct;
    private ChartAdapter pAdapter;
    private TextView name;
    private TextView price;
    private TextView info;
    private EditText editTextNumOfProd;

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
        editTextNumOfProd = findViewById(R.id.current_product_editTextNumber);
        editTextNumOfProd.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});
        updateUI();
    }

    public void backAction(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void updateUI(){
        name.setText(currentProduct.getName());
        price.setText(String.valueOf(currentProduct.getuPrice())+ " €");
        info.setText(currentProduct.getInfo());
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
}