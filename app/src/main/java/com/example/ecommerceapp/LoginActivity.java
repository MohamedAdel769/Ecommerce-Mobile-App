package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth fAuth;
    private EditText email, password;

    CheckBox remember_me;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean login_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        Button login = findViewById(R.id.loginBtn);
        TextView forgetPass = findViewById(R.id.forgotPassTxt);
        TextView register = findViewById(R.id.registerBtn);
        email = findViewById(R.id.emailTxt_log);
        password = findViewById(R.id.passTxt_log);
        remember_me = findViewById(R.id.checkBox_rmb);
        sharedPreferences = getSharedPreferences("remember", MODE_PRIVATE);

        isLoggedIn();

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    private void Login_user(){
        String username = email.getText().toString();
        String pass = password.getText().toString();
        if(!Helper.handleExp_Edit(email)) return;
        if(!Helper.handleExp_Edit(password)) return;
//        try {
//            String userId = fAuth.getCurrentUser().getUid();
//        }catch (Exception e){
//            Toast.makeText(LoginActivity.this,"Please sign up first!",Toast.LENGTH_LONG).show();
//            return;
//        }


        fAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if (remember_me.isChecked())
                        keepLogin(username, pass);
                    Toast.makeText(LoginActivity.this,"Login Success!",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Failed to Login, check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void isLoggedIn(){
        login_stat = sharedPreferences.getBoolean("login",false);
        String email = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        if(login_stat){
            fAuth.signInWithEmailAndPassword(email, password);
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void keepLogin(String username , String pass){
        editor=sharedPreferences.edit();
        editor.putString("username",username) ;
        editor.putString("password",pass);
        editor.putBoolean("login",true);
        editor.apply();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                Login_user();
                break;
            case R.id.registerBtn:
                startActivity(new Intent( LoginActivity.this, RegisterActivity.class));
                finish();
                break;
            case R.id.forgotPassTxt:
                startActivity(new Intent( LoginActivity.this, ForgetPassActivity.class));
                break;
        }
    }
}