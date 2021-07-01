package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener
{
    private TextView back_login;
    private EditText username, password, name, calender_date_txt, address;
    private Button signup;
    private RadioButton gender_m, gender_f;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        signup = (Button) findViewById(R.id.regBtn);
        username = (EditText) findViewById(R.id.emailTxt_reg);
        password = (EditText) findViewById(R.id.passTxt_reg);
        address = (EditText) findViewById(R.id.addrTxt);
        name = (EditText) findViewById(R.id.nameTxt_reg);
        gender_m = (RadioButton) findViewById(R.id.maleBtn);
        gender_f = (RadioButton) findViewById(R.id.femBtn);
        back_login = (TextView) findViewById(R.id.loginBtn_reg);
        calender_date_txt = (EditText) findViewById(R.id.birthTxt);
        gender_m.toggle();

        back_login.setOnClickListener(this);
        calender_date_txt.setOnClickListener(this);
        signup.setOnClickListener(this);

        calender_date_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    datepicker dialogFragment=new datepicker();
                    dialogFragment.show(getSupportFragmentManager(),"Date Picker");
                }
            }
        });
    }

    private void new_account()
    {
        String new_name = name.getText().toString();
        String new_password = password.getText().toString();
        String new_username = username.getText().toString();
        String new_birthdate = calender_date_txt.getText().toString();
        String new_add = address.getText().toString();
        String new_gender = "male";
        if(!Helper.handleExp_Edit(username)) return;
        if(!Helper.handleExp_Edit(password)) return;
        if(!Helper.handleExp_Edit(name)) return;
        if(!Helper.handleExp_Edit(address)) return;
        if(!Helper.handleExp_Edit(calender_date_txt)) return;
        if(gender_f.isChecked()) new_gender = "female";

        add_user(new_name, new_password, new_gender, new_username, new_birthdate, new_add);
    }

    private void add_user (String n, String pass, String gender, String email, String date, String addr)
    {
        HashMap<String,Object> hash_map = new HashMap<>();
        hash_map.put("name",n);
        hash_map.put("email",email);
        hash_map.put("password",pass);
        hash_map.put("gender",gender);
        hash_map.put("birthdate",date);
        hash_map.put("address",addr);
        fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String user_id = fAuth.getCurrentUser().getUid();
                    String doc_path = String.format("user/%s", user_id);
                    DocumentReference mDocRef = fStore.document(doc_path);
                    mDocRef.set(hash_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            name.setText("");
                            username.setText("");
                            password.setText("");
                            calender_date_txt.setText("");
                            address.setText("");
                            Toast.makeText(RegisterActivity.this,"User registered successfully!", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this,"Failed to register, Try Again!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Failed to register. Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String CurrentDate= DateFormat.getDateInstance().format(calendar.getTime());
        calender_date_txt.setText(CurrentDate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn_reg:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.birthTxt:
                datepicker dialogFragment=new datepicker();
                dialogFragment.show(getSupportFragmentManager(),"Date Picker");
                break;
            case R.id.regBtn:
                new_account();
                break;
        }
    }
}