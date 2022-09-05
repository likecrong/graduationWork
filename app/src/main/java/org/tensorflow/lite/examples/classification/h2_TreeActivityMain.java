package org.tensorflow.lite.examples.classification;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class h2_TreeActivityMain extends AppCompatActivity {

    final int numOfStage = 6;
    final int standardOfStage = 7;

    setDB s; // 디비
    DataSet d; //소리및이미지

    private SfxService sfxService;

    ImageView Number1, Number2, Number3, Number4, Number5, bonus, back;
    Button ok; //뒤로가기, 튜토리얼
    TextView conv, name;
    // 200910
    LinearLayout sayingFull;
    RelativeLayout sayingLayout;
    ImageView floatingCharacter;
    TextView gold, silver, bronze;

    ImageView settingBtn; // 확성기
    int sigOfSound; // 배경음을 켤지 말지 저장하는 변수

    int[] state;
    int[] game;

    int[] countOfStage = new int[numOfStage];
    int newStage = 0; //사용자가 참여할 수 있는 최고 단계
    int numOfGold = 0, numOfSilver = 0, numOfBronze = 0;

    // 튜토리얼용 카운트
    int count = 0;

    private View decorView;
    private int uiOption;
    private long time= 0;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
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
        setContentView(R.layout.h2_tree_activity);

        s = new setDB(h2_TreeActivityMain.this);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        Number1 = findViewById(R.id.number1); // 레이아웃에 설정된 뷰 가져옴
        Number2 = findViewById(R.id.number2);
        Number3 = findViewById(R.id.number3);
        Number4 = findViewById(R.id.number4);
        Number5 = findViewById(R.id.number5);
        bonus = findViewById(R.id.bonus);

        back = findViewById(R.id.back);

        gold = findViewById(R.id.gold);
        silver = findViewById(R.id.silver);
        bronze = findViewById(R.id.bronze);

        d = new DataSet(h2_TreeActivityMain.this);

        settingBtn = findViewById(R.id.setsound); //확성기

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfxService.SfxPlay(0);
                Intent i = new Intent(h2_TreeActivityMain.this, h1_StartMain.class);
                startActivity(i);
                //20.08.24_yeaji
                overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                finish();
            }
        });
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
            intent.putExtra("index",44);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
        }
        else{  //db상으로 음악이 꺼져있다면
            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(h2_TreeActivityMain.this, MusicService.class));
        }
    }
    //onResume에 들어왔을 때, 버튼에 의해 배경음을 전환할 함수
    public void setChangeSoundOnOff(){
        if(sigOfSound == 1){
            //현재 음악이 켜져있다면
            //음악을 끈다

            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(h2_TreeActivityMain.this, MusicService.class));

            s.update(1,0, 2);
            s.update(2,0, 2);
            sigOfSound = 0;
        }
        else{
            //현재 음악이 꺼져있다면
            //음악을 켠다
            settingBtn.setImageResource(R.drawable.sound_on);
            Intent intent=  new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("index",44);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
            s.update(1,1, 2);
            s.update(2,1, 2);
            sigOfSound = 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        state = s.getDB2();
        sigOfSound = state[1];
        d.setTuto(state[3]); //튜토리얼 여부
        s.printDB(state,s.dbHelper2.getDatabaseSize(),2);

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
        tutorial();

        int count = 0;

        newStage = state[0];
        game = s.getDB1();

        //금은동 상관없이 맞추기만 상황을 계산하는 부분
        //맞추면 1 못 맞추면 0으로 취급하여 계산
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                if (game[i * 10 + j] == 1) {
                    numOfGold += 1;
                    count++;
                }
                else if (game[i * 10 + j] == 2) {
                    numOfSilver += 1;
                    count++;
                }
                else if (game[i * 10 + j] == 3) {
                    numOfBronze += 1;
                    count++;
                }
            }
            countOfStage[i] = count;
            count = 0;
        }

        gold.setText(Integer.toString(numOfGold));
        silver.setText(Integer.toString(numOfSilver));
        bronze.setText(Integer.toString(numOfBronze));

        //레벨 판단하는 부분

        //5단계 끝나고 엔딩하는 부분
        //엔딩 생기면 들어감

        /* 알고리즘 설명 : 보너스 스테이지 결과는 레벨에 영향을 미치지 않음.
            단, 엔딩에 영향을 미칠 예정 */
        //5단계 오픈, b 10까지
        if (countOfStage[4] >= standardOfStage) {
            Number5.setImageResource(R.drawable.st5_f);
            Number4.setImageResource(R.drawable.st4_f);
            Number3.setImageResource(R.drawable.st3_f);
            Number2.setImageResource(R.drawable.st2_f);
            bonus.setImageResource(R.drawable.b_in_f);
            newStage = 5;
        }
        //4단계 오픈, b 7까지
        else if (countOfStage[3] >= standardOfStage) {
            Number5.setImageResource(R.drawable.st5_b);
            Number4.setImageResource(R.drawable.st4_f);
            Number3.setImageResource(R.drawable.st3_f);
            Number2.setImageResource(R.drawable.st2_f);
            bonus.setImageResource(R.drawable.b_in_f);
            newStage = 4;
        }
        //3단계 오픈, b 5까지
        else if (countOfStage[2] >= standardOfStage) {
            Number5.setImageResource(R.drawable.st5_b);
            Number4.setImageResource(R.drawable.st4_b);
            Number3.setImageResource(R.drawable.st3_f);
            Number2.setImageResource(R.drawable.st2_f);
            bonus.setImageResource(R.drawable.b_in_f);
            newStage = 3;
        }
        //2단계 오픈, b 2까지
        else if (countOfStage[1] >= standardOfStage) {
            Number5.setImageResource(R.drawable.st5_b);
            Number4.setImageResource(R.drawable.st4_b);
            Number3.setImageResource(R.drawable.st3_b);
            Number2.setImageResource(R.drawable.st2_f);
            bonus.setImageResource(R.drawable.b_in_f);
            newStage = 2;
        }
        //1단계 오픈
        else {
            Number5.setImageResource(R.drawable.st5_b);
            Number4.setImageResource(R.drawable.st4_b);
            Number3.setImageResource(R.drawable.st3_b);
            Number2.setImageResource(R.drawable.st2_b);
            bonus.setImageResource(R.drawable.b_in_b);
            newStage = 1;
        }

        s.update(0, newStage, 2);

    }

    public void onClick(View view) {

        //두 가지 경우를 판단하는 알고리즘이 없음 - 07.07
        //해당 단계에 처음 접속 시 StoryActivity 이동
        Intent intent = new Intent(this, StoryActivity.class);
        //해당 단계에 처음 접속이 아닌 경우 h3_ImageViewMain 이동
        //Intent intent = new Intent(this, h3_ImageViewMain.class);

        switch (view.getId()) {
            case R.id.number1:
                sfxService.SfxPlay(0);
                if (newStage >= 1) {

                    intent.putExtra("tToS", "st1");
                    startActivity(intent);
                    //20.08.24_yeaji
                    overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                    finish();
                }
                break;
            case R.id.number2:
                sfxService.SfxPlay(0);
                if (newStage >= 2) {
                    intent.putExtra("tToS", "st2");
                    startActivity(intent);
                    //20.08.24_yeaji
                    overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                    finish();
                } else Toast.makeText(getApplicationContext(), "문제를 조금 더 풀어볼까요?", Toast.LENGTH_SHORT).show();

                break;
            case R.id.number3:
                sfxService.SfxPlay(0);
                if (newStage >= 3) {
                    intent.putExtra("tToS", "st3");
                    startActivity(intent);
                    //20.08.24_yeaji
                    overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                    finish();
                } else Toast.makeText(getApplicationContext(), "문제를 조금 더 풀어볼까요?", Toast.LENGTH_SHORT).show();

                break;

            case R.id.number4:
                sfxService.SfxPlay(0);
                if (newStage >= 4) {
                    intent.putExtra("tToS", "st4");
                    startActivity(intent);
                    //20.08.24_yeaji
                    overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                    finish();
                } else Toast.makeText(getApplicationContext(), "문제를 조금 더 풀어볼까요?", Toast.LENGTH_SHORT).show();

                break;

            case R.id.number5:
                sfxService.SfxPlay(0);
                if (newStage >= 5) {
                    intent.putExtra("tToS", "st5");
                    startActivity(intent);
                    //20.08.24_yeaji
                    overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                    finish();
                } else Toast.makeText(getApplicationContext(), "문제를 조금 더 풀어볼까요?", Toast.LENGTH_SHORT).show();

                break;

            //보너스
            case R.id.bonus:
                sfxService.SfxPlay(0);
                if (newStage >= 2) {
                    intent.putExtra("tToS", "b");
                    startActivity(intent);
                    //20.08.24_yeaji
                    overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                    finish();
                } else Toast.makeText(getApplicationContext(), "문제를 조금 더 풀어볼까요?", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void tutorial() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout t_linear = (LinearLayout) inflater.inflate(R.layout.conversation, null);
        LinearLayout.LayoutParams t_paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(t_linear, t_paramlinear);//이 부분이 레이아웃을 겹치는 부분

        if (d.getTuto() == 0) { //튜토리얼을 수행하고 온 경우


            sayingFull = findViewById(R.id.sayingFull);
            sayingLayout = findViewById(R.id.sayingLayout);
            sayingFull.setBackgroundColor(Color.parseColor("#99000000"));
            sayingFull.setVisibility(View.VISIBLE);
            sayingFull.setClickable(true);
            sayingLayout.setVisibility(View.VISIBLE);
            ok = findViewById(R.id.conv_ok);
            conv = findViewById(R.id.conv);
            name = findViewById(R.id.name);

            ok.setVisibility(View.VISIBLE);

            floatingCharacter = findViewById(R.id.floatingCharacter);
            floatingCharacter.setVisibility(View.VISIBLE);

            name.setText("케빈");
            conv.setText("잘했어! 자, 이제 진짜 시작해보자!");

            d.setTuto(1);
            s.update(3,1, 2);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sfxService.SfxPlay(0);

                    if (count == 0) {
                        conv.setText("일단 우리 집으로 가볼까?");
                    } else {
                        sayingFull.setBackgroundColor(Color.parseColor("#00ff0000"));
                        sayingFull.setVisibility(View.GONE);

                        ok.setVisibility(View.GONE);
                        sayingLayout.setVisibility(View.GONE);
                        floatingCharacter.setVisibility(View.GONE);
                    }
                    count++;
                }
            });
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(h2_TreeActivityMain.this, MusicService.class));
        sigOfSound = 0;
    }
}
