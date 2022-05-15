package com.edix.krados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.edix.krados.entity.Product;
import com.edix.krados.entity.Purchase;
import com.edix.krados.entity.User;

import java.util.ArrayList;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity {
    private User currentUser;
    private List<Purchase> purchaseList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
    }

    public Purchase searchPurchaseById(Long id, List<Purchase> purchaseList){
        for(Purchase p: purchaseList){
            if(p.getId() == id){
                return p;
            }
        }
        return null;
    }

    public void goBack(View view){
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    public void goInfoPurchase(View view){
        View parent = (View) view.getParent();
        TextView idText = parent.findViewById(R.id.id_tag);
        Purchase p = searchPurchaseById(Long.parseLong(idText.getText().toString()), purchaseList);

        Intent intent = new Intent(this, InfoPurchaseActivity.class);
        intent.putExtra("id", p.getId());

        startActivity(intent);
    }
}