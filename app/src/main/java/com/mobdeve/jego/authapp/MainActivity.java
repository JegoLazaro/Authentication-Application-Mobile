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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register, forgot_pw;
    private EditText et_email, et_password;
    private Button btn_login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.tv_register);
        register.setOnClickListener(this);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        et_email = (EditText) findViewById(R.id.et_email_signup);
        et_email = (EditText)findViewById(R.id.et_password_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.btn_login:
                //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                loginUser();
        }
    }

    private void loginUser(){
        String login_email = et_email.getText().toString().trim();
        String login_password = et_password.getText().toString().trim();

        if(login_email.isEmpty()){
            et_email.setError("Enter Email Address Here.");
            et_email.requestFocus();
            return;
        }
        //IF INVALID
        if(!Patterns.EMAIL_ADDRESS.matcher(login_email).matches()){
            et_email.setError("Enter Valid Email Address.");
            et_email.requestFocus();
            return;
        }
        if(login_password.isEmpty()){
            et_password.setError("Enter Password Here.");
            et_password.requestFocus();
            return;
        }
        if(login_password.length() < 6){
            et_password.setError("Minimum Password length should be 6 characters");
            et_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(login_email,login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
                else{
                    Toast.makeText(MainActivity.this,"Login FAILED. Invalid Email or Password",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}