package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener{

    TextView total, n_items;
    Button place, back;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        total = findViewById(R.id.totalTxt);
        n_items = findViewById(R.id.nItemsTxt);
        place = findViewById(R.id.placeOrderBtn);
        back = findViewById(R.id.backCartBtn);

        calculate_receit();

        place.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.placeOrderBtn:
                placeOrder();
                break;
            case R.id.backCartBtn:
                Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("cart", true);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void calculate_receit(){
        int n = 0, sum = 0;
        for (String item : Helper.cart_products) {
            int price = Integer.valueOf(Helper.products_prices.get(item));
            int qnt = Integer.valueOf(Helper.products_qnt.get(item));
            n += qnt;
            sum += qnt * price;
        }
        total.setText(sum + "$");
        n_items.setText(n + " Items");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void placeOrder(){
        String date = java.time.LocalDate.now().toString();
        String Total = total.getText().toString();
        String uID = fAuth.getCurrentUser().getUid();
        HashMap<String,Object> order_details = new HashMap<>();
        order_details.put("date", date);
        order_details.put("total", Total);
        order_details.put("uID", uID);
        order_details.put("items", Helper.cart_products);
        DocumentReference docR = fStore.collection("order").document();
        docR.set(order_details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(OrderActivity.this,"Order Placed successfully!", Toast.LENGTH_LONG).show();
                // send email
                Helper.reset_cart();
                Helper.order_number += 1;
                startActivity(new Intent(OrderActivity.this, HomeActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(OrderActivity.this,"Failed to place order. Try Again!", Toast.LENGTH_LONG).show();
            }
        });
    }
}