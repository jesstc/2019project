package com.example.accounting_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Resetpassword extends AppCompatActivity {

    private FirebaseUser user;
    ImageButton backBTN;

    EditText oldPwEdit;
    private EditText checkPwEdit;
    private TextInputLayout oldPwLayout;
    private TextInputLayout newPwLayout;
    private TextInputLayout checkPwLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        goBack();
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("返回");
        initData();
        change();
    }

    //回到上一頁
    private void goBack() {
        backBTN = findViewById(R.id.backBtn);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
                Resetpassword.this.finish();
            }
        });
    }

    private void initData(){
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void change(){
        Button changePwBtn;
        changePwBtn = findViewById(R.id.btn_change_password);
        changePwBtn.setOnClickListener((View v) -> {
            // do something here
            oldPwEdit = findViewById(R.id.old_pw);
            checkPwEdit = findViewById(R.id.check_pw);
            oldPwLayout = findViewById(R.id.old_pw_layout);
            newPwLayout = findViewById(R.id.new_pw_layout);
            checkPwLayout = findViewById(R.id.check_pw_layout);
            String oldPassword = oldPwEdit.getText().toString();
            EditText displayName = findViewById(R.id.new_pw);
            String newPassword = displayName.getText().toString();
            String checkPassword = checkPwEdit.getText().toString();
            oldPwLayout.setErrorEnabled(true);
            newPwLayout.setErrorEnabled(true);
            checkPwLayout.setErrorEnabled(true);

            if(TextUtils.isEmpty(oldPassword)){
                oldPwLayout.setError("請輸入舊密碼");
                return;
            }else {oldPwLayout.setError(null);}
            if(TextUtils.isEmpty(newPassword)){
                newPwLayout.setError("請輸入新密碼");
                return;
            }else {newPwLayout.setError(null);}
            if(TextUtils.isEmpty(checkPassword)){
                checkPwLayout.setError("請再輸入一次新密碼");
                return;
            }else {checkPwLayout.setError(null);}
            if(newPassword.equals(checkPassword)){
                if(user!= null && user.getEmail() != null ){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), oldPassword);

                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //舊密碼驗證成功
                                        //Toast.makeText(MainActivity.this, "re-authenticate success", Toast.LENGTH_SHORT).show();
                                        user.updatePassword(newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Resetpassword.this, "Password 修改成功", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent();
                                                            intent.setClass(Resetpassword.this, setting.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else{
                                                            Toast.makeText(Resetpassword.this, "修改失敗: 新密碼格式錯誤", Toast.LENGTH_SHORT).show();
                                                            newPwLayout.setError("至少6位元以上");
                                                        }
                                                    }
                                                });
                                    } else{
                                        Toast.makeText(Resetpassword.this, "舊密碼輸入錯誤", Toast.LENGTH_SHORT).show();
                                        oldPwLayout.setError("舊密碼輸入錯誤");

                                    }
                                }
                            });
                }
            }else{
                Toast.makeText(Resetpassword.this, "password mismatching", Toast.LENGTH_SHORT).show();
                checkPwLayout.setError("與新密碼不相符");
            }
        });//end setOnClickListener
    }

    //返回上個fragment
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

