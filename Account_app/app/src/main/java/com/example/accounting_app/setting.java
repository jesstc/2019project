package com.example.accounting_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class setting extends AppCompatActivity {
    ImageButton backIMGBTN;
    ImageButton categoryIMGBTN;
    ImageButton roleIMGBTN;
    ImageButton passwordIMGBTN;
    ImageButton logoutIMGBTN;

    Button categoryBTN;
    Button roleBTN;
    Button passwordBTN;
    Button logoutBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        goBack();
        goCategory();
        goRole();
        goResetPassword();
        goLogout();
    }
    private void goBack(){
        backIMGBTN=findViewById(R.id.backBtn);
        backIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
//                Intent intent = new Intent(setting.this, Main2Activity.class);
//                startActivity(intent);
                setting.this.finish();
            }
        });
    }
    private void goCategory(){
        categoryIMGBTN=findViewById(R.id.categorImgBtn);
        categoryIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, Category.class);
                startActivity(intent);
            }
        });
        categoryBTN = findViewById(R.id.categoryBtn);
        categoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, Category.class);
                startActivity(intent);
            }
        });
    }

    private void goRole(){
        roleIMGBTN=findViewById(R.id.roleImgBtn);
        roleIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, SetRole.class);
                startActivity(intent);
            }
        });
        roleBTN = findViewById(R.id.roleBtn);
        roleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, SetRole.class);
                startActivity(intent);
            }
        });
    }
    private void goResetPassword(){
        passwordIMGBTN=findViewById(R.id.passwordImgBtn);
        passwordIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, Resetpassword.class);
                startActivity(intent);
            }
        });
        passwordBTN = findViewById(R.id.passwordBtn);
        passwordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, Resetpassword.class);
                startActivity(intent);
            }
        });
    }
    private void goLogout(){
        logoutIMGBTN=findViewById(R.id.logoutImgBtn);
        logoutIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(setting.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        logoutBTN = findViewById(R.id.logoutBtn);
        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(setting.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}