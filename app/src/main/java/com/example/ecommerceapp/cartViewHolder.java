package com.example.ecommerceapp;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.rpc.Help;

import org.jetbrains.annotations.NotNull;

public class cartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView pName, pPrice, pCategory, quantity;
    public Button addBtn, remBtn;
    public ImageView pImage;

    public cartViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        pName = itemView.findViewById(R.id.product_name);
        pPrice = itemView.findViewById(R.id.product_price);
        pCategory = itemView.findViewById(R.id.product_cat);
        quantity = itemView.findViewById(R.id.quantity_cart);
        addBtn = itemView.findViewById(R.id.addBtn_cart);
        remBtn = itemView.findViewById(R.id.remBtn_cart);
        pImage = itemView.findViewById(R.id.product_img);

        addBtn.setOnClickListener(this);
        remBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = pName.getText().toString();
        int old_qnt = Helper.products_qnt.get(name);
        int op = -1 ;
        switch (v.getId()){
            case R.id.addBtn_cart:
                op = 0;
                break;
            case R.id.remBtn_cart:
                op = 1;
                break;
        }
        update_quantity(name, old_qnt, op);
    }

    private void update_quantity(String name, int old, int op){
        int new_qnt = (op==0? old+1 : old-1);
        if(new_qnt == 0){
            Helper.products_qnt.remove(name);
            Helper.cart_products.remove(name);
            Helper.products_prices.remove(name);
            quantity.setText("0");
            if(Helper.HomePager != null){
                Helper.HomePager.setCurrentItem(1);
                Helper.HomePager.setCurrentItem(2);
            }
        }
        else{
            Helper.products_qnt.put(name, new_qnt);
            quantity.setText(String.valueOf(new_qnt));
        }
    }
}
