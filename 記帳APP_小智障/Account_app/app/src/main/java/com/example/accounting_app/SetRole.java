package com.example.accounting_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.accounting_app.chatting_page.Voice_Assistant;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SetRole extends AppCompatActivity {
    public FirebaseAuth mAuth;
    private String uid;
    private FirebaseUser user;
    ImageButton backIMGBTN;

    ViewPager viewPager;//頁面内容
    TextView textView1,textView2,textView3;//頁面標題
    List<View> views;// 頁面列表
    private int currIndex = 0;// 當頁編號
    View view1,view2,view3;//各個頁面
    Button comfirmBtn; //確認鈕
    String str;//alertdialog字串

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_role);
        mAuth = FirebaseAuth.getInstance();
        InitTextView();
        InitViewPager();
        comfirmBtn = findViewById(R.id.confirm);
        comfirmBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //wirte to database
                //連接資料庫
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference("user_profile/" + uid );
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 讀取user角色資料 並設為SetRole主畫面
                        User user = dataSnapshot.getValue(User.class);
                        String role = user.getRole();
                        if(role.equals("媽媽")) alertdialog("媽媽");
                        else if(role.equals(("爸爸"))) alertdialog("爸爸");
                        else if(role.equals("理財專家")) alertdialog("理財專家");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
//                alertdialog("理財專家");  //-------------------------------------------------------------
            }
        });
        goBack();
    }

    //回上一頁
    private void goBack(){
        backIMGBTN=findViewById(R.id.backBtn2);
        backIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Main2Activity is the page to test.
            public void onClick(View v) {
//                Intent intent = new Intent(SetRole.this, setting.class);
//                startActivity(intent);
                SetRole.this.finish();
            }
        });
    }

    //初始化頁面
    private void InitViewPager() {
        viewPager=(ViewPager) findViewById(R.id.vPager);
        views=new ArrayList<View>();
        LayoutInflater inflater=getLayoutInflater();
        view1=inflater.inflate(R.layout.mom_layout, null);
        view2=inflater.inflate(R.layout.dad_layout, null);
        view3=inflater.inflate(R.layout.expert_layout, null);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        viewPager.setAdapter(new SetRole.MyViewPagerAdapter(views));

        //wirte to database
        //連接資料庫
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("user_profile/" + uid );
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 讀取user角色資料 並設為SetRole主畫面
                User user = dataSnapshot.getValue(User.class);
                String role = user.getRole();
                if(role.equals("媽媽")) viewPager.setCurrentItem(0);
                else if(role.equals(("爸爸"))) viewPager.setCurrentItem(1);
                else if(role.equals("理財專家")) viewPager.setCurrentItem(2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
//        viewPager.setCurrentItem(2);    //------------------------------------------------------------

        viewPager.setOnPageChangeListener(new SetRole.MyOnPageChangeListener());
    }


    //初始化標頭
    private void InitTextView() {
        textView1 = (TextView) findViewById(R.id.page_mom);
        textView2 = (TextView) findViewById(R.id.page_dad);
        textView3 = (TextView) findViewById(R.id.page_expert);
//        textView1.setBackgroundResource(R.drawable.role_seleted);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        //wirte to database
        //連接資料庫
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("user_profile/" + uid );
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 讀取user角色資料 並設為SetRole主畫面
                User user = dataSnapshot.getValue(User.class);
                String role = user.getRole();
                if(role.equals("媽媽")) textView1.setBackgroundResource(R.drawable.role_seleted);
                else if(role.equals("爸爸")) textView2.setBackgroundResource(R.drawable.role_seleted);
                else if(role.equals("理財專家")) textView3.setBackgroundResource(R.drawable.role_seleted);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        textView1.setOnClickListener(new SetRole.MyOnClickListener(0));
        textView2.setOnClickListener(new SetRole.MyOnClickListener(1));
        textView3.setOnClickListener(new SetRole.MyOnClickListener(2));
    }



    //標頭點選
    public class MyOnClickListener implements View.OnClickListener {
        //頁碼
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            textView1.setBackgroundResource(R.drawable.role_unseleted);
            textView2.setBackgroundResource(R.drawable.role_unseleted);
            textView3.setBackgroundResource(R.drawable.role_unseleted);
            viewPager.setCurrentItem(index);
            changecolor(index);

        }

    }
    //滑動頁面
    public class MyViewPagerAdapter extends PagerAdapter{
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }
        //將原本的頁面移除
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) 	{
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return  mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
            currIndex = arg0;
            switch (currIndex){
                case 0:
                    textView1.setBackgroundResource(R.drawable.role_seleted);
                    textView2.setBackgroundResource(R.drawable.role_unseleted);
                    textView3.setBackgroundResource(R.drawable.role_unseleted);
                    str = "媽媽";
                    break;
                case 1:
                    textView2.setBackgroundResource(R.drawable.role_seleted);
                    textView1.setBackgroundResource(R.drawable.role_unseleted);
                    textView3.setBackgroundResource(R.drawable.role_unseleted);
                    str = "爸爸";
                    break;
                case 2:
                    textView3.setBackgroundResource(R.drawable.role_seleted);
                    textView2.setBackgroundResource(R.drawable.role_unseleted);
                    textView1.setBackgroundResource(R.drawable.role_unseleted);
                    str = "理財專家";
                    break;
            }
            comfirmBtn = findViewById(R.id.confirm);
            comfirmBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    alertdialog(str);
                }
            });
        }
    }
    private void alertdialog(String str){
        new AlertDialog.Builder(SetRole.this)
                .setTitle("確定要選擇"+ str +"嗎？")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        uid = user.getUid();
                        //wirte to database
                        //連接資料庫
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = firebaseDatabase.getReference("user_profile");
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("role",str);
                        myRef.child(uid).updateChildren(childUpdates);

                        Toast.makeText(SetRole.this, str, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SetRole.this, Voice_Assistant.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("role",str);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", null).create().show();
    }
    //變換標頭顏色
    public void changecolor(int page){
        currIndex = page;
        switch (currIndex){
            case 0:
                textView1.setBackgroundResource(R.drawable.role_seleted);
                textView2.setBackgroundResource(R.drawable.role_unseleted);
                textView3.setBackgroundResource(R.drawable.role_unseleted);
                break;
            case 1:
                textView2.setBackgroundResource(R.drawable.role_seleted);
                textView1.setBackgroundResource(R.drawable.role_unseleted);
                textView3.setBackgroundResource(R.drawable.role_unseleted);
                break;
            case 2:
                textView3.setBackgroundResource(R.drawable.role_seleted);
                textView2.setBackgroundResource(R.drawable.role_unseleted);
                textView1.setBackgroundResource(R.drawable.role_unseleted);
                break;
        }
    }
}
