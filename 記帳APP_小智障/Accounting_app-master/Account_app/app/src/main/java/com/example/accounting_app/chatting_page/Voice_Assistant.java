package com.example.accounting_app.chatting_page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.example.accounting_app.Accounting_c_icon;
import com.example.accounting_app.Article;
import com.example.accounting_app.CategoryRecord;
import com.example.accounting_app.Function;
import com.example.accounting_app.R;
import com.example.accounting_app.Report;
import com.example.accounting_app.User;
import com.example.accounting_app.accounting_day;
import com.example.accounting_app.accounting_month;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.api.gax.core.FixedCredentialsProvider;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.auth.oauth2.ServiceAccountCredentials;
//import com.google.cloud.dialogflow.v2beta1.QueryInput;
//import com.google.cloud.dialogflow.v2beta1.SessionName;
//import com.google.cloud.dialogflow.v2beta1.SessionsClient;
//import com.google.cloud.dialogflow.v2beta1.SessionsSettings;
//import com.google.cloud.dialogflow.v2beta1.TextInput;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
//Listen import
import ai.api.AIListener;
import ai.api.model.AIError;
import ai.api.model.Result;
import ai.api.android.AIService;

import android.Manifest;
import android.content.pm.PackageManager;
public class Voice_Assistant extends AppCompatActivity implements AIListener {
    private ImageView sendBtn;
    private RelativeLayout mic;
    private LinearLayout ac_text;
    private Button switch_btn_chat;
    private Button switch_btn_account;
    private ImageView backBtn;
    private LinearLayout account_expense;
    private LinearLayout account_income;
    private ImageView closeBtn_expense;
    private ImageView closeBtn_income;
    private Button button_income_Expense;
    private Button button_expense_Income;
    private ImageView set;
    private RecyclerView choose_expense;
    private RecyclerView choose_income;
    public int flag=2;
    public String send_category="";
    public String ex_or_in="支出";

    public int newDEx;
    public int newDIn;
    public int DEx;
    public int DIn;
    public int newBudget;
    public int monthEx;
    public int monthIn;

    //chatbox
    private static final String TAG = Voice_Assistant.class.getSimpleName();
    private static final int USER = 10001;
    private static final int BOT = 10002;

    private String uuid = UUID.randomUUID().toString();
    private LinearLayout chatLayout;
    private EditText queryEditText;

    // Android client
    private AIRequest aiRequest;
    private AIDataService aiDataService;
    private AIServiceContext customAIServiceContext;

    // Java V2
//    private SessionsClient sessionsClient;
//    private SessionName session;
    //Listen
    private AIService aiService;
    ImageButton listenButton;
    //firebase
    private FirebaseUser user;
    private String uid;
    //accounting
    private EditText money_ex;
    private EditText money_in;
    private EditText note_ex;
    private EditText note_in;
    private Button sendBtn_ex;
    private Button sendBtn_in;

    public Integer nowMonth;
    public Integer nowYear;
    public Integer nowDate;
    Calendar cal = Calendar.getInstance();
    public Integer childrenCount;

    private String first_talk;
    public String roleKey;

    public ImageView roleImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice__assistant);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        //連接資料庫
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://accounting-app-7c6d5.firebaseio.com/user_profile/"+uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                TextView role = findViewById(R.id.user_name);
                role.setText(user.getRole());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), " Database Error", Toast.LENGTH_SHORT).show();
            }
        });


        final ScrollView scrollview = findViewById(R.id.chatScrollView);
        scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));

        chatLayout = findViewById(R.id.chatLayout);

        money_ex = findViewById(R.id.editText_money_expense);
        money_in = findViewById(R.id.editText_money_income);
        note_ex = findViewById(R.id.editText_note_expense);
        note_in = findViewById(R.id.editText_note_income);

        sendBtn = findViewById(R.id.sendBtn);
        sendBtn_ex=findViewById(R.id.button_enter_expense);
        sendBtn_in=findViewById(R.id.button_enter_income);

        // 日期
        nowYear = cal.get(Calendar.YEAR);
        nowMonth =  cal.get(Calendar.MONTH) +1;
        nowDate =  cal.get(Calendar.DATE);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef_dayTotal = firebaseDatabase.getReference("accounting_record/" + uid + "/" + nowYear + "/" + nowMonth + "/" + nowDate);
        DatabaseReference myRef_monthTotal = firebaseDatabase.getReference("accounting_record/" + uid + "/" + nowYear + "/" + nowMonth);
        myRef_dayTotal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childrenCount = (int)dataSnapshot.getChildrenCount()-1;

                accounting_day accounting_day = dataSnapshot.getValue(accounting_day.class);
                DEx = accounting_day.getD_expend();
                DIn = accounting_day.getD_income();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        myRef_monthTotal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accounting_month accounting_month = dataSnapshot.getValue(accounting_month.class);
                newBudget = accounting_month.getM_budget();
                monthEx = accounting_month.getM_expend();
                monthIn = accounting_month.getM_income();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        sendBtn_ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(money_ex.getText().toString().equals("")) Toast.makeText(Voice_Assistant.this,"金錢不能為空",Toast.LENGTH_SHORT).show();
                else if(send_category.equals("")) Toast.makeText(Voice_Assistant.this,"請選擇一項類別",Toast.LENGTH_SHORT).show();
                else {
                    flag=0;
                    ex_or_in="支出";
                    // 取得index值(第幾筆帳)
                    myRef_dayTotal.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            childrenCount = (int)dataSnapshot.getChildrenCount()-1;

                            accounting_day accounting_day = dataSnapshot.getValue(accounting_day.class);
                            DEx = accounting_day.getD_expend();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    myRef_monthTotal.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            accounting_month accounting_month = dataSnapshot.getValue(accounting_month.class);
                            newBudget = accounting_month.getM_budget();
                            monthEx = accounting_month.getM_expend();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    CategoryRecord newRecord = new CategoryRecord(send_category, Integer.parseInt(money_ex.getText().toString()), note_ex.getText().toString());
                    // 新增資料到accounting_record中
                    myRef_dayTotal.child(childrenCount.toString()).setValue(newRecord);
                    // 累加支出
                    newDEx = Integer.parseInt(money_ex.getText().toString())+DEx;
//                    Toast.makeText(Voice_Assistant.this, Integer.toString(newDEx), Toast.LENGTH_SHORT).show();
                    // 更新每日總支出
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("d_expend",newDEx);
                    myRef_dayTotal.updateChildren(childUpdates);
                    Map<String, Object> childUpdates1 = new HashMap<>();
                    childUpdates1.put("EXorIN","expense");
                    myRef_dayTotal.child(childrenCount.toString()).updateChildren(childUpdates1);
                    Map<String, Object> month = new HashMap<>();
                    month.put("m_budget",newBudget-Integer.parseInt(money_ex.getText().toString()));
                    month.put("m_expend",monthEx+Integer.parseInt(money_ex.getText().toString()));
                    myRef_monthTotal.updateChildren(month);

                    sendMessage(sendBtn_ex);
                }
            }
        });
        sendBtn_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(money_in.getText().toString().equals("")) Toast.makeText(Voice_Assistant.this,"金錢不能為空",Toast.LENGTH_SHORT).show();
                else if(send_category.equals("")) Toast.makeText(Voice_Assistant.this,"請選擇一項類別",Toast.LENGTH_SHORT).show();
                else{
                    flag=1;
                    ex_or_in="收入";
                    // 取得index值(第幾筆帳)
                    myRef_dayTotal.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            childrenCount = (int)dataSnapshot.getChildrenCount()-1;

                            accounting_day accounting_day = dataSnapshot.getValue(accounting_day.class);
                            DIn = accounting_day.getD_income();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    myRef_monthTotal.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            accounting_month accounting_month = dataSnapshot.getValue(accounting_month.class);
                            monthIn = accounting_month.getM_income();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    CategoryRecord newRecord = new CategoryRecord(send_category, Integer.parseInt(money_in.getText().toString()), note_in.getText().toString());
                    // 新增資料到accounting_record中
                    myRef_dayTotal.child(childrenCount.toString()).setValue(newRecord);
                    // 累加收入
                    newDIn = Integer.parseInt(money_in.getText().toString())+DIn;
//                    Toast.makeText(Voice_Assistant.this, Integer.toString(newDIn), Toast.LENGTH_SHORT).show();
                    // 更新每日總收入
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("d_income",newDIn);
                    myRef_dayTotal.updateChildren(childUpdates);
                    Map<String, Object> childUpdates1 = new HashMap<>();
                    childUpdates1.put("EXorIN","income");
                    myRef_dayTotal.child(childrenCount.toString()).updateChildren(childUpdates1);
                    Map<String, Object> month = new HashMap<>();
                    month.put("m_income",monthIn+Integer.parseInt(money_in.getText().toString()));
                    myRef_monthTotal.updateChildren(month);

                    sendMessage(sendBtn_in);
                }
            }
        });

        sendBtn.setOnClickListener(this::sendMessage);

        queryEditText = findViewById(R.id.queryEditText);
        queryEditText.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                flag=2;
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        sendMessage(sendBtn);
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });
        mic = findViewById(R.id.mic);

        account_expense = findViewById(R.id.account_expense);
        account_income = findViewById(R.id.account_income);
        closeBtn_expense = findViewById(R.id.closeBtn_expense);
        closeBtn_income = findViewById(R.id.closeBtn_income);

        //切換至記帳or聊天
        switch_btn_chat = findViewById(R.id.switch_btn_chat);
        switch_btn_account = findViewById(R.id.switch_btn_account);

        set = findViewById(R.id.button_set);

        backBtn = findViewById(R.id.backBtn);
        ac_text = findViewById(R.id.ac_text);

        button_income_Expense = findViewById(R.id.button_income_Expense);
        button_expense_Income = findViewById(R.id.button_expense_Income);


        account_expense.setVisibility(View.GONE);
        account_income.setVisibility(View.GONE);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Voice_Assistant.this, Function.class);
                startActivity(intent);
            }
        });


        //按鈕切換至聊天
        switch_btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ac_text.setVisibility(View.VISIBLE);
                switch_btn_chat.setVisibility(View.GONE);
                switch_btn_account.setVisibility(View.GONE);
                account_expense.setVisibility(View.GONE);
                account_income.setVisibility(View.GONE);
                backBtn.setVisibility(View.VISIBLE);
                mic.setVisibility(View.VISIBLE);
                sendBtn.setVisibility(View.VISIBLE);
            }
        });

        backBtn.setVisibility(View.GONE);
        ac_text.setVisibility(View.GONE);
        mic.setVisibility(View.GONE);
        sendBtn.setVisibility(View.GONE);


        //從聊天切回至原本功能

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backBtn.setVisibility(View.GONE);
                switch_btn_account.setVisibility(View.VISIBLE);
                switch_btn_chat.setVisibility(View.VISIBLE);
                ac_text.setVisibility(View.GONE);
                mic.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
                account_expense.setVisibility(View.GONE);
            }
        });


        //按鈕切換至記帳
        switch_btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_expense.setVisibility(View.VISIBLE);
                account_income.setVisibility(View.GONE);
                ac_text.setVisibility(View.GONE);
                switch_btn_chat.setVisibility(View.GONE);
                switch_btn_account.setVisibility(View.GONE);
                mic.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
            }
        });


        //從記帳(支出)切回至原本功能

        closeBtn_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backBtn.setVisibility(View.GONE);
                switch_btn_account.setVisibility(View.VISIBLE);
                switch_btn_chat.setVisibility(View.VISIBLE);
                ac_text.setVisibility(View.GONE);
                mic.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
                account_expense.setVisibility(View.GONE);
                account_income.setVisibility(View.GONE);
            }
        });

        //從記帳(收入)切回至原本功能

        closeBtn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backBtn.setVisibility(View.GONE);
                switch_btn_account.setVisibility(View.VISIBLE);
                switch_btn_chat.setVisibility(View.VISIBLE);
                ac_text.setVisibility(View.GONE);
                mic.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
                account_expense.setVisibility(View.GONE);
                account_income.setVisibility(View.GONE);
            }
        });


//        在記帳(支出)按下收入按鈕
        button_income_Expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_expense.setVisibility(View.GONE);
                account_income.setVisibility(View.VISIBLE);
                ac_text.setVisibility(View.GONE);
                switch_btn_chat.setVisibility(View.GONE);
                switch_btn_account.setVisibility(View.GONE);
                mic.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
            }
        });


//        在記帳(收入)按下支出按鈕
        button_expense_Income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_expense.setVisibility(View.VISIBLE);
                account_income.setVisibility(View.GONE);
                ac_text.setVisibility(View.GONE);
                switch_btn_chat.setVisibility(View.GONE);
                switch_btn_account.setVisibility(View.GONE);
                mic.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
            }
        });

//        選擇支出類別
        //支出類別
        ArrayList<Accounting_c_icon> list_expense = new ArrayList<>();
        //連資料庫
        DatabaseReference C_expense= FirebaseDatabase.getInstance().getReferenceFromUrl("https://accounting-app-7c6d5.firebaseio.com/category/"+uid+"/c_expense");
        C_expense.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_expense.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String others=ds.getKey();
                    if (others.equals("other")) continue;
                    Accounting_c_icon c_expense = ds.getValue(Accounting_c_icon.class);
                    list_expense.add(new Accounting_c_icon(c_expense.getLogo_url(),c_expense.getName()));
                }
                DatabaseReference C_other= FirebaseDatabase.getInstance().getReferenceFromUrl("https://accounting-app-7c6d5.firebaseio.com/category/"+uid+"/c_expense/other");
                C_other.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Accounting_c_icon c_other = ds.getValue(Accounting_c_icon.class);
                            list_expense.add(new Accounting_c_icon(c_other.getLogo_url(), c_other.getName()));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("onCancelled",databaseError.toException());
                    }
                });
                choose_expense = findViewById(R.id.item_choose_expense);
                CategoryItem(choose_expense,list_expense);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });

//        選擇收入類別
        //收入類別
        ArrayList<Accounting_c_icon> list_income = new ArrayList<>();
        //連資料庫
        DatabaseReference C_income= FirebaseDatabase.getInstance().getReferenceFromUrl("https://accounting-app-7c6d5.firebaseio.com/category/"+uid+"/c_income");
        C_income.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_income.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String others=ds.getKey();
                    if (others.equals("other")) continue;
                    Accounting_c_icon c_income = ds.getValue(Accounting_c_icon.class);
                    list_income.add(new Accounting_c_icon(c_income.getLogo_url(),c_income.getName()));
                }
                DatabaseReference C_other= FirebaseDatabase.getInstance().getReferenceFromUrl("https://accounting-app-7c6d5.firebaseio.com/category/"+uid+"/c_income/other");
                C_other.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Accounting_c_icon c_other = ds.getValue(Accounting_c_icon.class);
                            list_income.add(new Accounting_c_icon(c_other.getLogo_url(), c_other.getName()));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("onCancelled",databaseError.toException());
                    }
                });

                choose_income = findViewById(R.id.item_choose_income);
                CategoryItem(choose_income,list_income);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });



        //權限
//        int permission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.RECORD_AUDIO);

//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            makeRequest();
//        }

//         Android client
        initChatbot();

        //選完角色第一次進入
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            first_talk = bundle.getString("first_pick");
            roleKey = bundle.getString("role");
            if(first_talk!=null) showTextView(first_talk, BOT);
        }

        // Java V2
//        initV2Chatbot();
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }

//    選擇類別的recycleview
    private void CategoryItem(RecyclerView r, ArrayList listData){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        r.setLayoutManager(linearLayoutManager);
        r.setAdapter(new CategoryAdapter(this, listData ,new CategoryAdapter.OnItemClickListener(){
            @Override
            public void onClick(int pos) {
                Accounting_c_icon data = (Accounting_c_icon) listData.get(pos);
                String cate_name=data.getName();
                send_category = cate_name;
                Toast.makeText(Voice_Assistant.this,"你選擇"+cate_name,Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void initChatbot() {
        final AIConfiguration config = new AIConfiguration("f37ab19dce164cf68256557b4cb05129",
                AIConfiguration.SupportedLanguages.ChineseTaiwan,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        aiDataService = new AIDataService(Voice_Assistant.this, config);
        customAIServiceContext = AIServiceContextBuilder.buildFromSessionId(uuid);// helps to create new session whenever app restarts
        aiRequest = new AIRequest();
    }


    private void sendMessage(View view) {
        String msg = queryEditText.getText().toString();
        String msg_ex = money_ex.getText().toString();
        String msg_in = money_in.getText().toString();

        //送出支出訊息
        if(flag==0) {
            msg_ex = ex_or_in + ": " + send_category + " " + msg_ex + "元";
            send_category="";
            if (msg_ex.trim().isEmpty()) {
                Toast.makeText(Voice_Assistant.this, "Please enter your query!", Toast.LENGTH_LONG).show();
            } else {
                showTextView(msg_ex, USER);
                money_ex.setText("");
                note_ex.setText("");
                //             Android client
                aiRequest.setQuery(msg_ex);
                RequestTask requestTask = new RequestTask(Voice_Assistant.this, aiDataService, customAIServiceContext);
                requestTask.execute(aiRequest);
                //設回預設flag
                flag=2;
            }
        }
        //送出收入訊息
        else if (flag==1) {
            msg_in = ex_or_in + ": " + send_category + " " + msg_in + "元";
            send_category="";
            if (msg_in.trim().isEmpty()) {
                Toast.makeText(Voice_Assistant.this, "Please enter your query!", Toast.LENGTH_LONG).show();
            } else {
                showTextView(msg_in, USER);
                money_in.setText("");
                note_in.setText("");
                //             Android client
                aiRequest.setQuery(msg_in);
                RequestTask requestTask = new RequestTask(Voice_Assistant.this, aiDataService, customAIServiceContext);
                requestTask.execute(aiRequest);
                //設回預設flag
                flag=2;
            }
        }
        //送出聊天訊息
        else {
            if (msg.trim().isEmpty()) {
                Toast.makeText(Voice_Assistant.this, "Please enter your query!", Toast.LENGTH_LONG).show();
            } else {
                showTextView(msg, USER);
                queryEditText.setText("");
                //             Android client
                aiRequest.setQuery(msg);
                RequestTask requestTask = new RequestTask(Voice_Assistant.this, aiDataService, customAIServiceContext);
                requestTask.execute(aiRequest);
            }
        }
    }

    public void callback(AIResponse aiResponse) {
        final Result result = aiResponse.getResult();
        if (result != null) {
            // process aiResponse here
            String botReply = aiResponse.getResult().getFulfillment().getSpeech();
            Log.d(TAG, "Bot Reply: " + botReply);
            showTextView(botReply, BOT);
        } else {
            Log.d(TAG, "Bot Reply: Null");
            showTextView("There was some communication issue. Please Try again!", BOT);
        }
    }

    public void callbackV2(DetectIntentResponse response) {
        if (response != null) {
            // process aiResponse here
            String botReply = response.getQueryResult().getFulfillmentText();
            Log.d(TAG, "V2 Bot Reply: " + botReply);
            showTextView(botReply, BOT);
        } else {
            Log.d(TAG, "Bot Reply: Null");
            showTextView("There was some communication issue. Please Try again!", BOT);
        }
    }

    private void showTextView(String message, int type) {
        FrameLayout layout;
        switch (type) {
            case USER:
                layout = getUserLayout();
                break;
            case BOT:
                layout = getBotLayout();
                break;
            default:
                layout = getBotLayout();
                break;
        }


        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間

        String str = formatter.format(curDate);


        layout.setFocusableInTouchMode(true);
        chatLayout.addView(layout); // move focus to text view to automatically make it scroll up if softfocus
        TextView tv = layout.findViewById(R.id.chatMsg);
        tv.setText(message);
        TextView time = layout.findViewById(R.id.text_message_time);
        time.setText(str);
        layout.requestFocus();
        queryEditText.requestFocus(); // change focus back to edit text to continue typing
    }

    FrameLayout getUserLayout() {
        LayoutInflater inflater = LayoutInflater.from(Voice_Assistant.this);
        return (FrameLayout) inflater.inflate(R.layout.user_msg_layout, null);
    }

    FrameLayout getBotLayout() {
        LayoutInflater inflater = LayoutInflater.from(Voice_Assistant.this);
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.bot_msg_layout, null);
        // 設定聊天機器人頭像
        roleImg = v.findViewById(R.id.left_img);
        if(roleKey!=null) {
            if (roleKey.equals("爸爸")) roleImg.setImageResource(R.drawable.dad);
            else if (roleKey.equals("媽媽")) roleImg.setImageResource(R.drawable.mother);
            else if (roleKey.equals("理財專家")) roleImg.setImageResource(R.drawable.expert);
        }
        return v;
    }

    @Override
    public void onResult(final AIResponse response) {
        //listen result
        Log.d(TAG, response.toString());
        final Result result = response.getResult();
        showTextView(result.getResolvedQuery(), USER);
        //response
        final String botReply = result.getFulfillment().getSpeech();
        Log.d(TAG, "Bot Reply: " + botReply);
        showTextView(botReply, BOT);
    }

    @Override
    public void onError(final AIError error) {

    }

    @Override
    public void onAudioLevel(final float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}

class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private ArrayList listData;
    private Accounting_c_icon data;
    private Context mContext;
    private CategoryAdapter.OnItemClickListener mlistener;

    public CategoryAdapter(Context context, ArrayList list, CategoryAdapter.OnItemClickListener listener){
        this.mContext = context;
        this.listData = list;
        this.mlistener = listener;
    }

    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout,parent,false));
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryViewHolder viewHolder, final int position) {
        data = (Accounting_c_icon) listData.get(position);
        Glide.with(mContext)
                .load(data.getLogo_url())
                .into(viewHolder.photo);
        viewHolder.name.setText(data.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    class CategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private ImageView photo;

        public CategoryViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.icon_name);
            photo = itemView.findViewById(R.id.icon_img);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}