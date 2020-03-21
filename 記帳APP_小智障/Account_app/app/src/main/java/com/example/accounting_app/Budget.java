package com.example.accounting_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class Budget extends AppCompatActivity {
    public FirebaseAuth mAuth;
    public Integer nowMonth;
    public Integer nowYear;
    private String uid;
    private FirebaseUser user;
    private Integer budget;
    public Integer monthEx;

    Calendar cal = Calendar.getInstance();

    ImageButton backBTN;
    Button confirmBTN;
    EditText inputEdT;
    String confirmStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        mAuth = FirebaseAuth.getInstance();

        inputEdT = findViewById(R.id.inputET);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        nowYear = cal.get(Calendar.YEAR);
        nowMonth =  cal.get(Calendar.MONTH) +1;

        //wirte to database
        //連接資料庫
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("accounting_record/" + uid + "/" + nowYear.toString() + "/" + nowMonth.toString());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accounting_month accounting_month = dataSnapshot.getValue(accounting_month.class);
                budget = accounting_month.getM_budget();
                monthEx = accounting_month.getM_expend();
                inputEdT.setHint(budget.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        goBack();
        confirm();
    }
    //回到上一頁
    private void goBack(){
        backBTN = (ImageButton)findViewById(R.id.backBtn);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
                Budget.this.finish();
            }
        });
    }
    //確認
    private void confirm(){
        confirmBTN = (Button)findViewById(R.id.confirmBtn);
        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEdT = findViewById(R.id.inputET);
                if("".equals(inputEdT.getText().toString().trim())){
                    new AlertDialog.Builder(Budget.this)
                            .setTitle("請輸入金額")
                            .setPositiveButton("確定", null).create().show();
                    Toast.makeText(Budget.this, nowYear, Toast.LENGTH_SHORT).show();
                }
                else{
                    budgetAlertDialog();
                }
            }
        });
    }

    private void budgetAlertDialog(){

        new AlertDialog.Builder(Budget.this)
                .setTitle("確定要輸入預算嗎")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmStr = "已輸入金額"+inputEdT.getText().toString()+"元";

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myRef_update = firebaseDatabase.getReference("accounting_record");
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("m_budget",Integer.parseInt(inputEdT.getText().toString())-monthEx);
                        myRef_update.child(uid).child(nowYear.toString()).child(nowMonth.toString()).updateChildren(childUpdates);

                        Toast.makeText(Budget.this,confirmStr, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Budget.this, Function.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", null).create().show();
    }

}
