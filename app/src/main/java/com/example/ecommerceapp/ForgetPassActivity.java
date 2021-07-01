package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassActivity extends AppCompatActivity {

    private EditText emailEdit;
    private Button reset;
    private ProgressBar progressBar;
    private FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        emailEdit = (EditText) findViewById(R.id.emailTxt_forg);
        reset = (Button) findViewById(R.id.resetPassBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fauth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String email = emailEdit.getText().toString().trim();
        if(!Helper.handleExp_Edit(emailEdit)) return;

        progressBar.setVisibility(View.VISIBLE);
        fauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPassActivity.this, "Check your email to reset you password.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgetPassActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(ForgetPassActivity.this, "An Error occurred, Try Again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}