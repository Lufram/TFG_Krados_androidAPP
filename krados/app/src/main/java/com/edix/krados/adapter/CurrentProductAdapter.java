package com.edix.krados.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edix.krados.R;
import com.edix.krados.entity.Product;

import java.util.List;

public class CurrentProductAdapter extends ArrayAdapter<Product> {
    public CurrentProductAdapter(Context context, List<Product> objects) {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
//        if (null == convertView) {
//            convertView = inflater.inflate(
//                    R.layout.product_content,
//                    parent,
//                    false);
//        }

        // Referencias UI.
        TextView name = (TextView) convertView.findViewById(R.id.current_product_name_text);
        TextView price = (TextView) convertView.findViewById(R.id.current_product_price_text);
        TextView info = (TextView) convertView.findViewById(R.id.current_product_info_text);

        // Lead actual.
        Product product = getItem(position);

        // Setup.
        System.out.print(product.getName());
        System.out.print(product.getName());
        name.setText(product.getName());
        price.setText(String.valueOf(product.getuPrice()));
        info.setText(product.getInfo());

        return convertView;
    }
}
