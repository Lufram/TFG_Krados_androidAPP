package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edix.krados.adapter.ChartAdapter;
import com.edix.krados.entity.Address;
import com.edix.krados.entity.Client;
import com.edix.krados.entity.Product;
import com.edix.krados.entity.User;
import com.edix.krados.utilities.InputFilterMinMax;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private ListView listProductContainer;
    private TextView cartTotalProductPrice;
    private List<Product> productList = new ArrayList<>();
    private RequestQueue queue;
    private ChartAdapter pAdapter;
    private User currentUser;
    private Client c;
    private LinearProgressIndicator bProgreso;

    //TODO
    //FIX TOTAL AMOUNT WHEN YOU DELETE ALL THE CART PRODUCTS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        currentUser = new User();
        currentUser.setUserName(getIntent().getStringExtra("username"));

        listProductContainer = (ListView) findViewById(R.id.cart_product_container);
        cartTotalProductPrice = findViewById(R.id.chart_total_product_price_text);
        bProgreso = findViewById(R.id.progressBar);

        bProgreso.setVisibility(View.INVISIBLE);
        queue = Volley.newRequestQueue(this);
        getUserDataByUsernameVolley(currentUser.getUserName());
        updateUI();

    }

    private void getDataByCartIdVolley(String cartId) {

        String url = "http://10.0.2.2:8086/krados/cart/productInCart/" + cartId;
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
                        p.setuPrice(Double.parseDouble(jresponse.getString("uprice")));
                        p.setAmount(Integer.parseInt(jresponse.getString("amount")));
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

    private void getUserDataByUsernameVolley(String username) {

        String url = String.format("http://10.0.2.2:8086/krados/client?userName=%1$s", username);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    c = null;
                    JSONObject jresponse = response.getJSONObject("address");
                    c = new Client();
                    c.setCartId(Long.parseLong(response.getString("cartId")));

                    getDataByCartIdVolley(String.valueOf(c.getCartId()));

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

    private void updateDataCartVolley(Long cartId, Long productId, int amount) {
        String url = String.format("http://10.0.2.2:8086/krados/cart/productInCart?cartId=%1$s&productId=%2$s&amount=%3$s", cartId, productId, amount);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getDataByCartIdVolley(String.valueOf(cartId));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
    }

    private void deleteDataCartVolley(Long cartId, Long productId) {
        String url = String.format("http://10.0.2.2:8086/krados/cart/productInCart?cartId=%1$s&productId=%2$s", cartId, productId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getDataByCartIdVolley(String.valueOf(cartId));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
    }

    private void deleteAllDataCartVolley(Long cartId) {
        String url = String.format("http://10.0.2.2:8086/krados/cart/deleteProductsInCart?cartId=%1$s", cartId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                getDataByCartIdVolley(String.valueOf(cartId));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
    }

    private void transformCartInPurchaseVolley(Long cartId) {
        String url = String.format("http://10.0.2.2:8086/krados/cart?cartId=%1$s", cartId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
    }


    private void updateUI() {
        if (productList.isEmpty()) {
            listProductContainer.setAdapter(null);
        } else {
            pAdapter = new ChartAdapter(this, productList);
            listProductContainer.setAdapter(pAdapter);
            setTotalPrice();
        }
    }


    private Product searchProductInArrayByName(String name, List<Product> productList) {
        for (Product p : productList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public void addCartAmount(View view) {
        View parent = (View) view.getParent();
        View principalComponent = (View) parent.getParent();

        TextView prodName = principalComponent.findViewById(R.id.chart_product_name_text);
        EditText editTextNumOfCartProd = parent.findViewById(R.id.chart_product_editTextNumber);

        Product currentProduct = searchProductInArrayByName(prodName.getText().toString(), productList);

        editTextNumOfCartProd.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});
        int initialNumber = Integer.parseInt(editTextNumOfCartProd.getText().toString());


        if (initialNumber == 100) {
            Toast.makeText(getApplicationContext(), "No se pueden añadir más productos", Toast.LENGTH_LONG).show();
        } else {
            int finalNumber = initialNumber + 1;
            editTextNumOfCartProd.setText(String.valueOf(finalNumber));

            updateDataCartVolley(c.getCartId(), currentProduct.getId(), finalNumber);
        }
    }

    public void subtractCartAmount(View view) {
        View parent = (View) view.getParent();
        View principalComponent = (View) parent.getParent();

        TextView prodName = principalComponent.findViewById(R.id.chart_product_name_text);
        EditText editTextNumOfCartProd = parent.findViewById(R.id.chart_product_editTextNumber);

        Product currentProduct = searchProductInArrayByName(prodName.getText().toString(), productList);

        editTextNumOfCartProd.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});
        int initialNumber = Integer.parseInt(editTextNumOfCartProd.getText().toString());

        if (initialNumber == 1) {
            Toast.makeText(getApplicationContext(), "La cantidad mínima es 1 producto", Toast.LENGTH_LONG).show();
        } else {
            int finalNumber = initialNumber - 1;
            editTextNumOfCartProd.setText(String.valueOf(finalNumber));

            updateDataCartVolley(c.getCartId(), currentProduct.getId(), finalNumber);
        }
    }

    public void deleleProductInCart(View view) {
        View parent = (View) view.getParent();
        TextView prodName = parent.findViewById(R.id.chart_product_name_text);

        Product currentProduct = searchProductInArrayByName(prodName.getText().toString(), productList);

        deleteDataCartVolley(c.getCartId(), currentProduct.getId());

    }

    private void setTotalPrice() {
        Double totalPrice = 0.00;
        for (Product p : productList) {
            totalPrice += p.getuPrice() * p.getAmount();
        }
        cartTotalProductPrice.setText(String.valueOf(totalPrice + "€"));
    }

    private void addPurchase() {
        bProgreso.setVisibility(View.VISIBLE);
        transformCartInPurchaseVolley(c.getCartId());
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", currentUser.getUserName());
        startActivity(intent);
    }

    public void makeAnOrder(View view) {
        if(productList.isEmpty()){
            Toast.makeText(getApplicationContext(), "No hay ningun producto en el carrito", Toast.LENGTH_LONG).show();
        }else{
            addPurchase();
            try {
                Thread.sleep(1000);
                cartTotalProductPrice.setText("0.00€");
                Toast.makeText(getApplicationContext(), "El pedido se ha realizado con exito", Toast.LENGTH_LONG).show();
                deleteAllDataCartVolley(c.getCartId());
                bProgreso.setVisibility(View.INVISIBLE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}