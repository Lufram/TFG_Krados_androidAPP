package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.adapter.ChartAdapter;
import com.edix.krados.adapter.ProductAdapter;
import com.edix.krados.entity.Product;
import com.edix.krados.utilities.InputFilterMinMax;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private ListView listProductContainer;
    private List<Product> productList = new ArrayList<>();
    private RequestQueue queue;
    private ChartAdapter pAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        listProductContainer = (ListView) findViewById(R.id.cart_product_container);
        queue = Volley.newRequestQueue(this);
        getDataByCartIdVolley("1");
        updateUI();

    }

    private void getDataByCartIdVolley (String cartId){

        String url = "http://10.0.2.2:8086/krados/products/productInCart/" + cartId;
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
                        p.setuPrice(Double.parseDouble(jresponse.getString("uprice")));
                        p.setAmount(Integer.parseInt(jresponse.getString("amount")));
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

    private void updateDataCartVolley (HashMap params ){
        String url = "http://10.0.2.2:8086/krados/products/productInCart";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Modificado");
                updateUI();
            }

        }, error -> {
            System.out.println(error);
        });
        queue.add(request);
    }

    private void deleteDataCartVolley(){

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

    private Product searchProductInArrayByName(String name, List<Product> productList){
        for(Product p: productList){
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public void addCartAmount(View view){
        HashMap<String, String> params = new HashMap();

        View parent = (View) view.getParent();
        View principalComponent = (View) parent.getParent();

        TextView prodName = principalComponent.findViewById(R.id.chart_product_name_text);
        EditText editTextNumOfCartProd = parent.findViewById(R.id.chart_product_editTextNumber);

        Product currentProduct = searchProductInArrayByName(prodName.getText().toString(), productList);

        editTextNumOfCartProd.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});
        int initialNumber = Integer.parseInt(editTextNumOfCartProd.getText().toString());

        params.put("cartId", "1");
        params.put("productId", String.valueOf(currentProduct.getId()));
        params.put("amount",String.valueOf(currentProduct.getAmount()));

        System.out.println(params);


        updateDataCartVolley(params);

        if(initialNumber == 100){
            Toast.makeText(getApplicationContext(),"No se pueden añadir más productos",Toast.LENGTH_LONG).show();
        }else{
            int finalNumber = initialNumber + 1;
            editTextNumOfCartProd.setText(String.valueOf(finalNumber));
        }
    }

    public void subtractCartAmount(View view){
        HashMap<String, String> params = new HashMap();

        View parent = (View) view.getParent();
        View principalComponent = (View) parent.getParent();

        TextView prodName = principalComponent.findViewById(R.id.chart_product_name_text);
        EditText editTextNumOfCartProd = parent.findViewById(R.id.chart_product_editTextNumber);

        Product currentProduct = searchProductInArrayByName(prodName.getText().toString(), productList);

        editTextNumOfCartProd.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});
        int initialNumber = Integer.parseInt(editTextNumOfCartProd.getText().toString());

        params.put("cartId", "1");
        params.put("productId", String.valueOf(currentProduct.getId()));
        params.put("amount",String.valueOf(currentProduct.getAmount()));

        updateDataCartVolley(params);

        if(initialNumber == 1){
            Toast.makeText(getApplicationContext(),"La cantidad mínima es 1 producto",Toast.LENGTH_LONG).show();
        }else{
            int finalNumber = initialNumber - 1;
            editTextNumOfCartProd.setText(String.valueOf(finalNumber));
        }
    }
}