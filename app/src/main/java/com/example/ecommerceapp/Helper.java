package com.example.ecommerceapp;

import android.util.Patterns;
import android.widget.EditText;

import androidx.viewpager.widget.ViewPager;

import com.example.ecommerceapp.models.Order;
import com.example.ecommerceapp.models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Helper {
    public static String srch_text, srch_qr, srch_voice;
    public static int srch_method = -1;
    public static List<String> cart_products = new ArrayList<>();
    public static HashMap<String, Integer> products_qnt = new HashMap<>();
    public static HashMap<String, Integer> products_prices = new HashMap<>();
    public static int order_number = 1;
    public static ViewPager HomePager ;

    public static boolean handleExp_Edit(EditText input){
        String input_name = input.getHint().toString();
        String value = input.getText().toString();
        if(value.isEmpty()){
            input.setError(String.format("%s is required!", input_name));
            input.requestFocus();
            return false;
        }
        if(input_name.equals("Password") && value.length() < 6){
            input.setError(String.format("Min password length should be 6 characters!", input_name));
            input.requestFocus();
            return false;
        }
        if(input_name.equals("Email") && !Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            input.setError(String.format("Please enter a valid email!", input_name));
            input.requestFocus();
            return false;
        }
        return true;
    }

    public static void reset_cart(){
        cart_products.clear();
        products_qnt.clear();
        products_prices.clear();
    }

}
