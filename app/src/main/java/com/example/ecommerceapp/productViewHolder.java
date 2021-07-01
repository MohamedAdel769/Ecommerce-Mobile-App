package com.example.ecommerceapp;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.models.Product;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class productViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView pName, pPrice, pCategory;
    public Button addBtn;
    public ImageView pImage;

    public productViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        pName = itemView.findViewById(R.id.product_name);
        pPrice = itemView.findViewById(R.id.product_price);
        pCategory = itemView.findViewById(R.id.product_cat);
        addBtn = itemView.findViewById(R.id.addBtn_pro);
        pImage = itemView.findViewById(R.id.product_img);

        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = pName.getText().toString();
        String[] temp = pPrice.getText().toString().split(" ");
        String price = temp[2].substring(0, temp[2].length()-1);
        Helper.cart_products.add(name);
        Helper.products_qnt.put(name, 1);
        Helper.products_prices.put(name, Integer.valueOf(price));
        Toast.makeText(v.getContext(), "Item Added to Cart", Toast.LENGTH_SHORT).show();
    }
}
