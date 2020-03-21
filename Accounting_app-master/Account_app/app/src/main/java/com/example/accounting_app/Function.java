package com.example.accounting_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Function extends AppCompatActivity {
    ImageButton backIMGBTN;
    ImageButton chartIMGBTN;
    ImageButton budgetIMGBTN;
    ImageButton reportIMGBTN;
    ImageButton historyIMGBTN;
    ImageButton settingIMGBTN;

    Button chartBTN;
    Button budgetBTN;
    Button reportBTN;
    Button historyBTN;
    Button settingBTN;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        goBack();
        goChart();
        goBudget();
        goReport();
        goHistory();
        goSetting();

    }

    private void goBack(){
        backIMGBTN=findViewById(R.id.backBtn);
        backIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
//                Intent intent = new Intent(Function.this, Main2Activity.class);
//                startActivity(intent);
                Function.this.finish();
            }
        });
    }
    private void goChart(){
//        chartIMGBTN = findViewById(R.id.chartImgBtn);
//        chartIMGBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Function.this, Main2Activity.class);
//                startActivity(intent);
//            }
//        });
//
//        chartBTN = findViewById(R.id.chartBtn);
//        chartBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Function.this, Main2Activity.class);
//                startActivity(intent);
//            }
//        });
    }
    private void goBudget(){
        budgetIMGBTN = findViewById(R.id.budgetImgBtn);
        budgetIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Function.this, Budget.class);
                startActivity(intent);
            }
        });

        budgetBTN = findViewById(R.id.budgetBtn);
        budgetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Function.this, Budget.class);
                startActivity(intent);
            }
        });
    }
    private void goReport(){
        reportIMGBTN = findViewById(R.id.reportImgBtn);
        reportIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Function.this, Report.class);
                startActivity(intent);
            }
        });

        reportBTN = findViewById(R.id.reportBtn);
        reportBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Function.this, Report.class);
                startActivity(intent);
            }
        });
    }
    private void goHistory(){
        historyIMGBTN = findViewById(R.id.historyImgBtn);
        historyIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Function.this, History.class);
                startActivity(intent);
            }
        });

        historyBTN = findViewById(R.id.historyBtn);
        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Function.this, History.class);
                startActivity(intent);
            }
        });
    }
    private void goSetting(){
        settingIMGBTN = findViewById(R.id.settingImgBtn);
        settingIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Function.this, setting.class);
                startActivity(intent);
            }
        });

        settingBTN = findViewById(R.id.settingBtn);
        settingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Function.this, setting.class);
                startActivity(intent);
            }
        });
    }
}
