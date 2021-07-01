package com.example.ecommerceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ecommerceapp.models.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.rpc.Help;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Cart extends Fragment {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView cartList;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter adapter;
    Button checkout;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        //setHasOptionsMenu(true);

        cartList = view.findViewById(R.id.cartList);
        cartList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        cartList.setLayoutManager(layoutManager);

        checkout = view.findViewById(R.id.chkoutBtn);
        fStore = FirebaseFirestore.getInstance();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helper.cart_products.size() == 0){
                    Toast.makeText(getContext(), "Please Add items first!", Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(getActivity(), OrderActivity.class));
                getActivity().finish();
            }
        });

        displayCartItems();

        ViewPager mviewPager = getActivity().findViewById(R.id.view_pager);
        mviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    displayCartItems();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void displayCartItems(){
        List<String> temp = new ArrayList<>();
        temp.add("");
        if(Helper.cart_products.size()>0){
            temp = Helper.cart_products;
        }
        Query query = fStore.collection("item").whereIn("name", temp);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class).build();

        adapter = new FirestoreRecyclerAdapter<Product, cartViewHolder>(options) {
            @Override
            public void onBindViewHolder(cartViewHolder holder, int position, Product model) {
                holder.pName.setText(model.getName());
                holder.pCategory.setText("Category: " + model.getCategory());
                holder.pPrice.setText("Price = " + model.getPrice() + "$");
                holder.quantity.setText(String.valueOf(Helper.products_qnt.get(model.getName())));
                Picasso.get().load(model.getImage()).into(holder.pImage);
            }

            @Override
            public cartViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.cart_card_layout, group, false);

                return new cartViewHolder(view);
            }
        };

        cartList.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        displayCartItems();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }
}
