package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ecommerceapp.ui.main.SectionsPagerAdapter;
import com.example.ecommerceapp.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        fAuth = FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences("remember", MODE_PRIVATE);

        boolean flag = getIntent().getBooleanExtra("cart", false);
        if (flag) {
            viewPager.setCurrentItem(2);
        }

        Helper.HomePager = viewPager;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewPager viewPager = findViewById(R.id.view_pager);
        boolean flag = getIntent().getBooleanExtra("cart", false);
        if (flag) {
            viewPager.setCurrentItem(2);
        }
        Helper.HomePager = viewPager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.reportBtn){
            startActivity(new Intent(HomeActivity.this, ReportActivity.class));
        }
        else {
            Logout();
        }
        return true;
    }

    private void Logout(){
        editor=sharedPreferences.edit();
        editor.putString("username",null);
        editor.putString("password",null);
        editor.putBoolean("login",false);
        editor.apply();

        fAuth.signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }
}