package com.edix.krados.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.edix.krados.R;
import com.edix.krados.entity.Product;
import com.edix.krados.entity.Purchase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PurchaseAdapter extends ArrayAdapter<Purchase> {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String pattern = "#.##";
    private DecimalFormat decimalFormat =  new DecimalFormat(pattern);
    public PurchaseAdapter(Context context, List<Purchase> objects) {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.purchase_list_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView id = (TextView) convertView.findViewById(R.id.id_tag);
        TextView date = (TextView) convertView.findViewById(R.id.date_text);
        TextView status = (TextView) convertView.findViewById(R.id.status_text);
        TextView amount = (TextView) convertView.findViewById(R.id.amount_text);

        // Lead actual.
        Purchase purchase = getItem(position);

        // Setup.
        id.setText(String.valueOf(purchase.getId()));
        date.setText(String.valueOf(format.format(purchase.getPurchaseDate())));
        status.setText(purchase.getStatus());
        amount.setText(decimalFormat.format(purchase.getTotalPrice() )+ "€");

        return convertView;
    }
}
