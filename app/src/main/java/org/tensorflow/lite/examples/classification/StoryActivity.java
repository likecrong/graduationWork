package org.tensorflow.lite.examples.classification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StoryActivity extends AppCompatActivity {

    int[] state;


    setDB s;
    DataSet d;

    private SfxService sfxService;

    ImageView settingBtn; // 확성기
    int sigOfSound; // 배경음을 켤지 말지 저장하는 변수

    RelativeLayout sayingLayout;
    TextView conv, name;
    ImageView leftcharacter, rightcharacter, floatingCharacter;
    Handler mHandler;
    int sayingLength = 0;
    int i = 0;
    String sayingSub = "";
    String username = "d";
    String level = "";
    String[] sayingList;
    boolean isFin = false;

    String[] t_sayingList = new String[]{"아 오늘 할 일도 다 했다!!",
            "계속 농사를 짓다보면 언젠간 하늘을 날 수 있을까?",
            "농사도 좋지만 승무원이 돼서 하늘을 날고 싶은데..", "깜짝이야!!언제부터 있었어?", "넌 이름이 뭐야?",
            "@@! 저기 날 도와주지 않을래?", "승무원이 되려면 영어를 공부해야 하거든! 혼자 외로웠는데 같이 공부하자",
            "그럼 공부 방법을 알려줄게!"};
    String[] sayingList1 = new String[]{"들어와 @@! 우리 집에 놀러 온 친구는 네가 처음이야",
            "꽤 깔끔하지? 나 청소왕이거든!", "그럼 내가 소개시켜줄게~"};//스토리마다 여기에 기입
    String[] sayingList2 = new String[]{"우리집에 온 손님이니까 내가 맛있는 거 해줄게!", "@@! 지렁이 좋아해?",
            "뭐 싫어한다고? 이게 얼마나 맛있는데!", "흠..그래. 입맛은 다 다르니까..",
            "편하게 쉬고있어 @@! 내가 금방 맛있는 거 만들어줄게~"};
    String[] sayingList3 = new String[]{"@@! 우리 오늘 저녁은 파티하자~ 나 친구랑 파티 해보고싶었어.",
            "파티에는 맛있는 음식이 빠질 수 없지", "우리 마트가서 맛있는 음식 많이 사오자!"};
    String[] sayingList4 = new String[]{"아 친구랑 놀러나오니까 너무 좋다!", "어 저거 혹시..쳇바퀴!!!",
            "우리 저 공원 가자!! 얼른 따라와 @@!"};
    String[] sayingList5 = new String[]{"아 요즘 공부한다고 너무 앉아만 있었다..", "@@! 오늘은 운동하러 나갈까?",
            "공부도 중요하지만 체력도 중요하다고~! 얼른 준비해 @@!"};
    String[] sayingList6 = new String[]{"@@, 음악 좋아해?", "내가 옛날에 음악을 좀 했거든~ 음악 천재 소리 듣고 자랐지!",
            "피아노에 기타, 바이올린, 드럼까지 못 하는 게 없다고~!","오랜만에 연주해볼까? 신청곡 있으면 다 말해 @@!"};

    private View decorView;
    private int uiOption;
    private long time= 0;

    final String PREFERENCE = "isFirst";

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            decorView.setSystemUiVisibility(uiOption);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_activity);

        s = new setDB(getApplicationContext());
        d = new DataSet(StoryActivity.this);


        //인플레이션으로 겹치는 레이아웃을 깐다
        LayoutInflater inflater = (LayoutInflater) getSystemService(

                Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout conversation = (LinearLayout) inflater.inflate(R.layout.conversation, null);
        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(conversation, paramlinear);//이 부분이 레이아웃을 겹치는 부분

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        sayingLayout = findViewById(R.id.sayingLayout);
        conv = findViewById(R.id.conv);
        name = findViewById(R.id.name);
        leftcharacter = findViewById(R.id.leftcharacter);
        rightcharacter = findViewById(R.id.rightcharacter);
        floatingCharacter = findViewById(R.id.floatingCharacter);

        settingBtn = findViewById(R.id.setsound); //확성기

        sayingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sfxService.SfxPlay(0);

                if (i == 5 && (d.getTuto() == 0)) saveName();
                else if (i < sayingLength) {
                    startSaying(sayingList, i);
                    i++;
                } else {
                    //대화 종료
                    endActivity();
                }

            }
        });
        mHandler = new Handler();
        Runnable mTask = new Runnable() {
            @Override
            public void run() {

                sayingLayout.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                sayingLength = sayingList.length;
                startSaying(sayingList, i);

                name.setText("케빈");
                floatingCharacter.setVisibility(View.VISIBLE);
                i++;
            }
        };
        mHandler.postDelayed(mTask, 1500);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sfxService = new SfxService(getApplicationContext());
        sfxService.SfxLoad();
    }
    //onResume에 들어왔을 때, 처음 한 번 실행할 함수
    public void setFirstSoundOnOff(){
        if(sigOfSound == 1){ //db상으로 음악이 켜져있다면
            settingBtn.setImageResource(R.drawable.sound_on);
            Intent intent=  new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("index",3);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
        }
        else{  //db상으로 음악이 꺼져있다면
            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(StoryActivity.this, MusicService.class));
        }
    }
    //onResume에 들어왔을 때, 버튼에 의해 배경음을 전환할 함수
    public void setChangeSoundOnOff(){
        if(sigOfSound == 1){
            //현재 음악이 켜져있다면
            //음악을 끈다

            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(StoryActivity.this, MusicService.class));

            s.update(1,0, 2);
            s.update(2,0, 2);
            sigOfSound = 0;
        }
        else{
            //현재 음악이 꺼져있다면
            //음악을 켠다
            settingBtn.setImageResource(R.drawable.sound_on);
            Intent intent=  new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("index",3);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
            s.update(1,1, 2);
            s.update(2,1, 2);
            sigOfSound = 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        state = new int[s.dbHelper2.getDatabaseSize()];
        state = s.getDB2();
        sigOfSound = state[1];
        d.setTuto(state[3]); //튜토리얼 여부

        if (d.getTuto() == 0) { //튜토리얼 처음이면
            init();
        }
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

        //현재 수행할 스테이지 불러오기
        Intent h2intent = getIntent();
        if (h2intent.getStringExtra("tToS") != null) {
            level = h2intent.getStringExtra("tToS");
        }

        // 200420 출력할 문장들
        if (level.equals("st1") == true) sayingList = sayingList1;
        else if (level.equals("st2") == true) sayingList = sayingList2;
        else if (level.equals("st3") == true) sayingList = sayingList3;
        else if (level.equals("st4") == true) sayingList = sayingList4;
        else if (level.equals("st5") == true) sayingList = sayingList5;
        else if (level.equals("b") == true) sayingList = sayingList6;
        else sayingList = t_sayingList; //level에 아무것도 없을 때



        if (checkStory(level) == true) { //끝났다면
            Intent intent = new Intent(StoryActivity.this, h3_ImageViewMain.class);
            intent.putExtra("sToI", level);
            startActivity(intent);
            //20.08.24_yeaji
            overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(StoryActivity.this, MusicService.class));
        sigOfSound = 0;

    }

    private void endActivity() {
        // 200404 튜토리얼 화면 가는 부분
        SharedPreferences pref = getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);

        //스토리 진행 뒤, 해당 스테이지에 대한 이미지뷰로 이동
        Intent intent = new Intent(StoryActivity.this, h3_ImageViewMain.class);

        if(d.getTuto() != 0){ //!isFirst
            intent.putExtra("sToI", level);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(level, true);
            editor.commit();
        }

        startActivity(intent);
        //20.08.24_yeaji
        overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
        finish();
    }

    void startSaying(String[] sayinglist, int saying) {

        SharedPreferences pref = getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);
        conv.setText(sayinglist[saying].replace("@@",pref.getString("UserName", "dfdf")));

        for (int j = 1; j <= sayinglist[saying].length(); j++) {

            //conv.setText(sayinglist[saying]);
            sayingSub = sayinglist[saying].substring(0, j);
            mHandler = new Handler();

        }
        getWindow().getDecorView().setSystemUiVisibility(uiOption);
    }

    public void saveName() {
        sayingLayout.setClickable(false);
        settingBtn.setClickable(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(

                Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout inputName = (LinearLayout) inflater.inflate(R.layout.username, null);
        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(inputName, paramlinear);//이 부분이 레이아웃을 겹치는 부분

        EditText editText = (EditText) findViewById(R.id.username);
        Button save = findViewById(R.id.save_name);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sfxService.SfxPlay(1);

                String strSaveData = editText.getText().toString();

                if (strSaveData.equals("") == true) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("UserName", strSaveData);
                    editor.commit();

                    username = pref.getString("UserName", strSaveData);
                    ((ViewGroup) inputName.getParent()).removeView(inputName);

                    sayingLayout.setClickable(true);
                    settingBtn.setClickable(true);

                    startSaying(t_sayingList, i);
                    i++;

                }
            }
        });
    }
    public void init(){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("st1", false);
        editor.putBoolean("st2", false);
        editor.putBoolean("st3", false);
        editor.putBoolean("st4", false);
        editor.putBoolean("st5", false);
        editor.putBoolean("b", false);
        editor.commit();
    }
    public boolean checkStory(String level) {
        SharedPreferences pref = getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);
        isFin = pref.getBoolean(level, false);
        return isFin;
    }
}