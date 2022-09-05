package org.tensorflow.lite.examples.classification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class EndingMain extends AppCompatActivity {
    setDB s; // 디비
    DataSet d; //소리및이미지

    private SfxService sfxService;

    ImageView settingBtn; // 확성기
    int sigOfSound; // 배경음을 켤지 말지 저장하는 변수

    ImageView back;

    LinearLayout pic_vegis;

    ImageView i1, i2, i3;
    LinearLayout l1, l2, l3, l4;

    int[] state;
    int[] game;

    private View decorView;
    private int uiOption;
    private long time= 0;

    String result;

    int bgmIndex = 0;

    Boolean isCreditFinished = false;

    TextView conv; // 대화창
    Button ok; // 확인버튼
    RelativeLayout sayingLayout; // 대화 레이아웃

    LinearLayout storyLayout; // 배경 레이아웃

    // 크레딧 레이아웃
    LinearLayout creditLayout;
    ScrollView leftScroll, rightScroll;
    RelativeLayout leftCredit, rightCredit;
    RelativeLayout creditLocation;

    String username = "";
    final String PREFERENCE = "isFirst";

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);
        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            d.callExit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onStart() {
        super.onStart();
        sfxService = new SfxService(getApplicationContext());
        sfxService.SfxLoad();
        //if DB상 효과음이 off면 sfxService.volume = 0.0f;
    }
    public void setOnCreate(String result) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(result.equals("1")) {
            setContentView(R.layout.ending1_lay);
            bgmIndex = 7;
        }
        else if(result.equals("2")) {
            setContentView(R.layout.ending2_lay);
            bgmIndex = 9;
        }
        else if(result.equals("3")) {
            setContentView(R.layout.ending3_lay);
            bgmIndex = 2;
        }
        else {
            setContentView(R.layout.ending4_lay);
            bgmIndex = 8;
        }

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // inflater
        LayoutInflater inflater = (LayoutInflater) getSystemService(

                Context.LAYOUT_INFLATER_SERVICE);

        // 야채 바구니
        LinearLayout vegis = (LinearLayout)inflater.inflate(R.layout.ending3_vegis, null);
        LinearLayout.LayoutParams paramlinear4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(vegis, paramlinear4);

        LinearLayout conversation = (LinearLayout) inflater.inflate(R.layout.conversation, null);
        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(conversation, paramlinear);//이 부분이 레이아웃을 겹치는 부분

        // 200828 크레딧
        LinearLayout credit = (LinearLayout) inflater.inflate(R.layout.credit, null);
        LinearLayout.LayoutParams paramlinear3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(credit, paramlinear3);//이 부분이 레이아웃을 겹치는 부분

        //뒤로가기
        back = findViewById(R.id.quitbtn);

        //야채
        pic_vegis = findViewById(R.id.ending3_vegis);

        // 대화창 레이아웃
        sayingLayout = findViewById(R.id.sayingLayout);
        conv = findViewById(R.id.conv);
        ok = findViewById(R.id.conv_ok);
        storyLayout = findViewById(R.id.storyLayout);

        // 크레딧 레이아웃
        creditLayout = findViewById(R.id.creditLayout);
        leftScroll = findViewById(R.id.leftScroll);
        rightScroll = findViewById(R.id.rightScroll);
        leftCredit = findViewById(R.id.leftCredit);
        rightCredit = findViewById(R.id.rightCredit);

        leftScroll.setOnTouchListener(new OnTouch());
        rightScroll.setOnTouchListener(new OnTouch());

        //소리 레이아웃
        settingBtn = findViewById(R.id.setsound);

        //뒤로가기 리스너
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfxService.SfxPlay(0);
                Thread.interrupted();
                Intent i = new Intent(EndingMain.this, EndingBook.class);
                startActivity(i);
                //20.08.24_yeaji
                overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                finish();

            }
        });

        // 유저 이름 가져오기
        SharedPreferences pref = getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);
        username = pref.getString("UserName", "aa");

        s = new setDB(EndingMain.this);
        d = new DataSet(EndingMain.this);
    }

    //onResume에 들어왔을 때, 처음 한 번 실행할 함수
    public void setFirstSoundOnOff(){
        if(sigOfSound == 1){ //db상으로 음악이 켜져있다면
            settingBtn.setImageResource(R.drawable.sound_on);
            Intent intent=  new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("index",bgmIndex);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
        }
        else{  //db상으로 음악이 꺼져있다면
            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(EndingMain.this, MusicService.class));
        }
    }
    //onResume에 들어왔을 때, 버튼에 의해 배경음을 전환할 함수
    public void setChangeSoundOnOff(){
        if(sigOfSound == 1){
            //현재 음악이 켜져있다면
            //음악을 끈다

            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(EndingMain.this, MusicService.class));

            s.update(1,0, 2);
            s.update(2,0, 2);
            sigOfSound = 0;
        }
        else{
            //현재 음악이 꺼져있다면
            //음악을 켠다
            settingBtn.setImageResource(R.drawable.sound_on);
            Intent intent=  new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("index",bgmIndex);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
            s.update(1,1, 2);
            s.update(2,1, 2);
            sigOfSound = 0;
        }
    }

    public void onResume(){
        super.onResume();

        Intent sig = getIntent();
        Intent sig2 = getIntent();
        if (sig.getStringExtra("signalOfEnding")!= null) {
            result = sig.getStringExtra("signalOfEnding");
        }

        else if (sig2.getStringExtra("signalOfEnding2")!= null) {
            result = sig2.getStringExtra("signalOfEnding2");

        }

        setOnCreate(result);

        game = s.getDB1();
        state = s.getDB2();

        sigOfSound = state[1];
        d.setTuto(state[3]); //튜토리얼 여부

        setFirstSoundOnOff();

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //누를때마다 디비 읽기
                state = s.getDB2();
                sigOfSound = state[1];
                s.printDB(state,s.dbHelper2.getDatabaseSize(),2);

                sfxService.SfxPlay(0);
                setChangeSoundOnOff();
            }
        });

        if(result.equals("1")) {
            i1 = (ImageView) findViewById(R.id.k1);
            l1 = findViewById(R.id.ending1_layout);
            EndingSeting es = new EndingSeting(getApplicationContext(), result, i1, l1, sayingLayout,
                    creditLayout, ok, conv, leftCredit, rightCredit, creditLocation, username);
            es.move();
        }
        else if(result.equals("2")) {
            i2 = (ImageView) findViewById(R.id.k2);
            l2 = findViewById(R.id.ending2_layout);
            EndingSeting es = new EndingSeting(getApplicationContext(), result, i2, l2, sayingLayout,
                    creditLayout, ok, conv, leftCredit, rightCredit, creditLocation, username);
            es.move();
        }
        else if(result.equals("3")) {
            i3 = (ImageView) findViewById(R.id.k3);
            l3 = findViewById(R.id.ending3_layout);
            EndingSeting es = new EndingSeting(getApplicationContext(), result, i3, l3, sayingLayout,
                    creditLayout, ok, conv, leftCredit, rightCredit, creditLocation, username, pic_vegis);
            es.move();
        }
        else {
            l4 = findViewById(R.id.ending4_layout);
            EndingSeting es = new EndingSeting(getApplicationContext(), result, l4, sayingLayout,
                    creditLayout, ok, conv, leftCredit, rightCredit, creditLocation, username);
            es.move();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(EndingMain.this, MusicService.class));
        d.setMusicOn(false);
    }

    private class OnTouch implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }
}