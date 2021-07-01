package com.example.ecommerceapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.Help;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Products extends Fragment {

    RecyclerView products_list;
    RecyclerView.LayoutManager layoutManager;
    Spinner products_box;
    Button clear_srch;
    private FirebaseFirestore fStore;
    FirestoreRecyclerAdapter adapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_products, container, false);
        //setHasOptionsMenu(true);

        products_list = view.findViewById(R.id.productsList);
        products_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        products_list.setLayoutManager(layoutManager);

        products_box = view.findViewById(R.id.catBox);
        clear_srch = view.findViewById(R.id.clearBtn);
        fStore = FirebaseFirestore.getInstance();

        products_box.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cat_name = products_box.getSelectedItem().toString();
                filterByCategory(cat_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clear_srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products_box.setSelection(0);
                filterByCategory("All");
                Helper.srch_text = "";
                Helper.srch_method = -1;
                Helper.srch_voice = "";
                Helper.srch_qr = "";
            }
        });

        ViewPager mviewPager = getActivity().findViewById(R.id.view_pager);
        mviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    if(Helper.srch_method != -1)
                        filterBySearch();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void filterByCategory(String category){
        Query query ;
        if(category.equals("All")) {
            query = fStore.collection("item");
        }
        else{
            query = fStore.collection("item").whereEqualTo("Category", category);
        }
        displayProducts(query);
    }

    private void filterBySearch(){

        if(Helper.srch_method == 1){
            String srch_value = Helper.srch_qr;
            Log.d("DocSnippets", "HEEEEY ");
            Query query = fStore.collection("item").whereEqualTo("image", srch_value);
            displayProducts(query);
        }
        else{
            String srch_value;
            if(Helper.srch_method == 0)
                srch_value = Helper.srch_text;
            else
                srch_value = Helper.srch_voice;
            Query query = fStore.collection("item").orderBy("name").startAt(srch_value).endAt(srch_value+'\uf8ff');
            displayProducts(query);
        }
    }

    private void displayProducts(Query query){
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class).build();

        adapter = new FirestoreRecyclerAdapter<Product, productViewHolder>(options) {
            @Override
            public void onBindViewHolder(productViewHolder holder, int position, Product model) {
                holder.pName.setText(model.getName());
                holder.pCategory.setText("Category: " + model.getCategory());
                holder.pPrice.setText("Price = " + model.getPrice() + "$");
                String img = model.getImage();
                Picasso.get().load(img).into(holder.pImage);
            }

            @Override
            public productViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.product_card_layout, group, false);

                return new productViewHolder(view);
            }
        };

        products_list.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Helper.srch_method == -1)
        {
            Query query = fStore.collection("item");
            displayProducts(query);
        }
        else
            filterBySearch();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
