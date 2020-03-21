package com.example.accounting_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Report extends AppCompatActivity {

    ImageButton backBTN;
    TextView title;
    TextView source;
    TextView author;
    TextView Content;

    public FirebaseAuth mAuth;
    private Integer randomN;
    private String articlePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Random ran = new Random();
        randomN = ran.nextInt(3);
        if(randomN == 1) articlePath = "/article1";
        else if(randomN == 2) articlePath = "/article2";
        else articlePath = "/article3";
//        Toast.makeText(Report.this, articlePath, Toast.LENGTH_SHORT).show();

        //wirte to database
        //連接資料庫
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://accounting-app-7c6d5.firebaseio.com/finance_article"+articlePath);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title = findViewById(R.id.title);
                source = findViewById(R.id.source);
                author = findViewById(R.id.author);
                Content = findViewById(R.id.content);

                Article article = dataSnapshot.getValue(Article.class);
                title.setText(article.getTitle());
                source.setText(article.getPublisher());
                author.setText(article.getAuthor());
                // 內文
                StringBuffer content = new StringBuffer(article.getPreface());
                content.append("\n\n\n");
                for (int i=1; i<=dataSnapshot.child("article").getChildrenCount(); i++) {
                    content.append(dataSnapshot.child("article").child("part"+i).getValue()+"\n\n");
                }
                Content.setText(content);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        goBack();
    }

    //回到上一頁
    private void goBack() {
        backBTN = (ImageButton) findViewById(R.id.backBtn);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
                Report.this.finish();
            }
        });
    }
}
