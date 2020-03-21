package com.example.accounting_app;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounting_app.chatting_page.Voice_Assistant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText account;
    EditText password;
    Button loginBtn;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 設定 FirebaseAuth 介面
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) {
            setContentView(R.layout.activity_login);
            setContentView(R.layout.activity_login);
            account = findViewById(R.id.editText_account);
            password = findViewById(R.id.editText_password);

            loginBtn = findViewById(R.id.button_login);
            signup = findViewById(R.id.textView_signUp);

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String accountStr = account.getText().toString();
                    String passwordStr = password.getText().toString();
                    // 是否輸入帳號密碼
                    if (accountStr.equals("")) {
                        Toast.makeText(LoginActivity.this, R.string.account_empty, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (passwordStr.equals("")) {
                        Toast.makeText(LoginActivity.this, R.string.password_empty, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 驗證帳號密碼
                    mAuth.signInWithEmailAndPassword(accountStr, passwordStr)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(LoginActivity.this, Voice_Assistant.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            });

        }
        else {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, Voice_Assistant.class);
            startActivity(intent);
            finish();
        }
    }
}
