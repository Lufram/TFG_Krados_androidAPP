package com.edix.krados.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edix.krados.R;
import com.edix.krados.entity.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context context, List<Product> objects) {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.product_list_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView name = (TextView) convertView.findViewById(R.id.product_name_text);
        TextView price = (TextView) convertView.findViewById(R.id.product_price_text);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageViewAdapter);

        // Lead actual.
        Product product = getItem(position);

        // Setup.
        name.setText(product.getName());
        price.setText(String.valueOf(product.getuPrice())+ " €");
        Glide.with(convertView).load(Uri.parse(product.getUrl())).into(image);

        return convertView;
    }
}
