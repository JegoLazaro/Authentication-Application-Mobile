package com.mobdeve.jego.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_banner_regis;
    private Button btn_register;
    private EditText et_full_name_regis, et_age_regis, et_email_regis, et_pw_regis;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        tv_banner_regis = findViewById(R.id.tv_banner_regis);
        tv_banner_regis.setOnClickListener(this);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        et_age_regis = findViewById(R.id.et_Age_regis);
        et_email_regis = findViewById(R.id.et_email_regis);
        et_full_name_regis = findViewById(R.id.et_FullName);
        et_pw_regis = findViewById(R.id.et_Password_regis);

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_banner_regis:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.btn_register:
                registerUser();
                break;

        }
    }

    private void registerUser(){
        String email = et_email_regis.getText().toString().trim();
        String password = et_pw_regis.getText().toString().trim();
        String fullname = et_full_name_regis.getText().toString().trim();
        String age = et_age_regis.getText().toString().trim();

        if(fullname.isEmpty()){
            et_full_name_regis.setError("Full Name Field is blank.");
            et_full_name_regis.requestFocus();
            return;
        }
        if(age.isEmpty()){
            et_age_regis.setError("Age Field is blank.");
            et_age_regis.requestFocus();
            return;
        }
        if(email.isEmpty()){
            et_email_regis.setError("Email Field is blank.");
            et_email_regis.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email_regis.setError("Please provide valid email.");
            et_email_regis.requestFocus();
            return;
        }
        if(password.isEmpty()){
            et_pw_regis.setError("Password is required.");
            et_pw_regis.requestFocus();
            return;
        }
        if(password.length() < 6){
            et_pw_regis.setError("Minimum Password length should be 6 characters");
            et_pw_regis.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullname,age,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this,"User Registration Successful",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(RegisterUser.this,"User Registration FAILED! Try Again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                });

    }
}