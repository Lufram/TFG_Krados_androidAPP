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

import java.util.List;

public class ChartAdapter extends ArrayAdapter<Product> {
    public ChartAdapter(Context context, List<Product> objects) {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.product_chart_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView name = (TextView) convertView.findViewById(R.id.chart_product_name_text);
        TextView price = (TextView) convertView.findViewById(R.id.chart_product_price_text);
        EditText amount = (EditText) convertView.findViewById(R.id.chart_product_editTextNumber);
        TextView subPriceView = (TextView) convertView.findViewById(R.id.chart_total_product_price_text);

        // Lead actual.
        Product product = getItem(position);

        // Setup.
        name.setText(product.getName());
        price.setText(String.valueOf(product.getuPrice())+ " €");
        amount.setText(String.valueOf(product.getAmount()));
        Double subPrice = product.getuPrice() * product.getAmount();
        subPriceView.setText(String.valueOf(subPrice)+ " €");

        return convertView;
    }
}
