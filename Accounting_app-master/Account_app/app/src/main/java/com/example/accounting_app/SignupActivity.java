package com.example.accounting_app;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    private String uid;
    private FirebaseUser user;
    private EditText accountEdit;
    private EditText passwordEdit;
    private EditText passwordEditEnsure;
    Button signupBtn;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        accountEdit = findViewById(R.id.editText_account);
        passwordEdit = findViewById(R.id.editText_password);
        passwordEditEnsure = findViewById(R.id.editText_ensure_password);

        signupBtn = findViewById(R.id.button_signUp);
        loginBtn = findViewById(R.id.button_login_again);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String password_ensure = passwordEditEnsure.getText().toString();

                if(TextUtils.isEmpty(account)){
                    Toast.makeText(SignupActivity.this, R.string.account_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignupActivity.this, R.string.password_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(password_ensure)){
                    Toast.makeText(SignupActivity.this, R.string.password_ensure, Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(account, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            uid = user.getUid();
                            //wirte to database
                            //連接資料庫
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                            // user setting
                            DatabaseReference myRef_user = firebaseDatabase.getReference("user_profile");
                            User user = new User("媽媽");     // 預設角色為媽媽
                            myRef_user.child(uid).setValue(user);

                            // accounting_record setting
                            DatabaseReference myRef_accounting = firebaseDatabase.getReference("accounting_record");
                            accounting_month accounting_month = new accounting_month(0, 0, 0);
                            accounting_day accounting_day = new accounting_day(0, 0);

                            for(Integer month=1; month<=3; month++) {
                                if(month == 1) {   // 11月
                                    myRef_accounting.child(uid).child("2019").child("11").setValue(accounting_month);
                                    for(Integer day=1; day<=30; day++) {
                                        String sDay = Integer.toString(day);
                                        myRef_accounting.child(uid).child("2019").child("11").child(sDay).setValue(accounting_day);
                                    }
                                }
                                if(month == 2) {   // 12月
                                    myRef_accounting.child(uid).child("2019").child("12").setValue(accounting_month);
                                    for(Integer day=1; day<=31; day++) {
                                        String sDay = Integer.toString(day);
                                        myRef_accounting.child(uid).child("2019").child("12").child(sDay).setValue(accounting_day);
                                    }
                                }
                                if(month == 3) {   // 1月
                                    myRef_accounting.child(uid).child("2020").child("1").setValue(accounting_month);
                                    for(Integer day=1; day<=30; day++) {
                                        String sDay = Integer.toString(day);
                                        myRef_accounting.child(uid).child("2020").child("1").child(sDay).setValue(accounting_day);
                                    }
                                }
                            }

                            // category setting
                            DatabaseReference myRef_category = firebaseDatabase.getReference("category/" + uid);
                            // expense
                            myRef_category.child("c_expense").child("breakfast").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/breakfast_icon.png?alt=media&token=c8dc7153-a69e-493e-847f-fcf033fc3dc8", "早餐"));
                            myRef_category.child("c_expense").child("lunch").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/lunch_icon.png?alt=media&token=95f98f5b-d0a6-468d-8b0c-9bb9a7dd185f", "午餐"));
                            myRef_category.child("c_expense").child("dinner").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/dinner_icon.png?alt=media&token=3de8aa07-64d2-4fe5-9d57-8b800eb866a6", "晚餐"));
                            myRef_category.child("c_expense").child("dessert").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/dessert_icon.png?alt=media&token=400500ae-707b-4202-a014-445b679b3767", "點心"));
                            myRef_category.child("c_expense").child("drug").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/drug_icon.png?alt=media&token=003bea9c-72cd-41cc-bc67-e06150fdd38f", "醫療"));
                            myRef_category.child("c_expense").child("drink").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/drink_icon.png?alt=media&token=461a2311-32f9-461d-85a4-3929c9d32aea", "飲料"));
                            myRef_category.child("c_expense").child("entertainment").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/entertainment_icon.png?alt=media&token=ebb5af41-19ad-492c-85d9-66a899b76a7e", "娛樂"));
                            myRef_category.child("c_expense").child("exercise").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/exercise_icon.png?alt=media&token=e296ef82-90d7-41ad-827e-679a65c3a630", "運動"));
                            myRef_category.child("c_expense").child("housing").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/housing_icon.png?alt=media&token=51454bf5-4067-4eed-bfbc-f93435447513", "居家"));
                            myRef_category.child("c_expense").child("phone").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/phone_icon.png?alt=media&token=7663fe00-0ead-49a3-9943-1e38d8b49137", "電話費"));
                            myRef_category.child("c_expense").child("rent").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/rent_icon.png?alt=media&token=443102d5-8109-4f82-b982-54322a000b1d", "房租/貸"));
                            myRef_category.child("c_expense").child("salon").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/salon_icon.png?alt=media&token=f07828fc-b327-4ab0-92aa-64181dfe3e70", "美髮"));
                            myRef_category.child("c_expense").child("shopping").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/shopping_icon.png?alt=media&token=49a3e867-8644-4c82-bc73-253521cb7bd6", "購物"));
                            myRef_category.child("c_expense").child("study").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/study_icon.png?alt=media&token=b9c94569-f50e-497f-8d92-1d6a7f3aa9dd", "課業"));
                            myRef_category.child("c_expense").child("tec").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/tec_icon.png?alt=media&token=a8c25736-d383-413a-8fdb-7c8aa0b35e67", "3C"));
                            myRef_category.child("c_expense").child("traffic").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/traffic_icon.png?alt=media&token=fd39621c-d6af-49b5-a750-0ed9a024fa31", "交通"));
                            // income
                            myRef_category.child("c_income").child("allowance").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/pocket_icon.png?alt=media&token=66b78f2f-64b7-41f8-9efc-e826b583a9ac", "零用金"));
                            myRef_category.child("c_income").child("bonus").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/gift_icon.png?alt=media&token=dd6bf766-a4de-4ce8-be64-3b18c4e41b22", "獎金"));
                            myRef_category.child("c_income").child("salary").setValue(new Accounting_c_icon("https://firebasestorage.googleapis.com/v0/b/accounting-app-7c6d5.appspot.com/o/salary_icon.png?alt=media&token=d1af38f5-97e0-43dd-b282-136bddfcda44", "薪水"));

                            Intent intent = new Intent();
                            intent.setClass(SignupActivity.this, ChooseRole.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
