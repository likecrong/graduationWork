package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import java.lang.reflect.Field;
import java.util.ArrayList;

public class h3_ImageViewMain extends AppCompatActivity {

    int sigOfOff; // 0이면 꺼짐, 1이면 켜짐

    final int numOfQuestion = 10;

    setDB s;
    DataSet d;

    private SfxService sfxService;
    ImageView settingBtn; // 확성기
    int sigOfSound; // 배경음을 켤지 말지 저장하는 변수

    LinearLayout imageviewLayout;

    int[] game;
    int[] state;
    int[] result;
    ArrayList<Integer> imageList = new ArrayList<>();
    int[] standardOfbonus = {1, 4, 6, 9}; //2, 3, 4, 5단계에서 보너스 문제 오픈 기준

    private static final int DP = 24;

    // 200910
    LinearLayout sayingFull;
    RelativeLayout sayingLayout;
    Button prev, next, ok;
    TextView conv, name;
    ImageView select, suc, gif, floatingCharacter, back;
    boolean isInflated = false;
    boolean isFloating = false;
    boolean isClicked = false;

    NonSwipeViewPager viewPager;

    int currentpoistion = 0; //현재 이미지 위치
    int answerposition = 0;
    int count = 0;
    int countOfAns = 0;

    int gifcount = 0;
    String direction = "right";

    String level = ""; //stage level

    int newStage = 0;
    private View decorView;
    private int uiOption;
    private long time= 0;

    ProgressBar myProgressBar;

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

    public void initializeView()
    {
        myProgressBar = (ProgressBar)findViewById(R.id.h_progressbar);
        select = findViewById(R.id.sel);
        back = findViewById(R.id.back);
        suc = findViewById(R.id.suc);
        viewPager = findViewById(R.id.viewPager);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        gif = (ImageView) findViewById(R.id.gif_image);

        settingBtn = findViewById(R.id.setsound); //확성기

        imageviewLayout = findViewById(R.id.imageView_main);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sfxService.SfxPlay(0);

                Intent intent = new Intent(h3_ImageViewMain.this, h2_TreeActivityMain.class);//트리로 가는 부분
                startActivity(intent);
                //20.08.24_yeaji
                overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h3_imageview_main);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        LayoutInflater inflater = (LayoutInflater) getSystemService(

                Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout linear = (LinearLayout) inflater.inflate(R.layout.gif, null);
        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(linear, paramlinear);//이 부분이 레이아웃을 겹치는 부분
        //add는 기존의 레이아웃에 겹쳐서 배치하라는 뜻이다.

        d = new DataSet(h3_ImageViewMain.this,imageList);
        s = new setDB(h3_ImageViewMain.this);

        sigOfOff = 1;

        initializeView();
    }
    @Override
    protected void onStart() {
        super.onStart();
        sfxService = new SfxService(getApplicationContext());
        sfxService.SfxLoad();
        //if DB상 효과음이 off면 sfxService.volume = 0.0f;
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
            stopService(new Intent(h3_ImageViewMain.this, MusicService.class));
        }
    }
    //onResume에 들어왔을 때, 버튼에 의해 배경음을 전환할 함수
    public void setChangeSoundOnOff(){
        if(sigOfSound == 1){
            //현재 음악이 켜져있다면
            //음악을 끈다
            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(h3_ImageViewMain.this, MusicService.class));
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
    public void onResume() {
        super.onResume();


        int sigOfWriteAndBack = 0; //쓰기까지 완료 하고 온 경우를 확인하는 변수

        game = new int[s.dbHelper1.getDatabaseSize()];
        state = new int[s.dbHelper2.getDatabaseSize()];
        result = new int[numOfQuestion];

        game = s.getDB1();
        state = s.getDB2();

        sigOfSound = state[1];
        d.setTuto(state[3]); //튜토리얼 여부

        s.printDB(game,s.dbHelper1.getDatabaseSize(),1);
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

        Interpolator sInterpolator = new AccelerateInterpolator();

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ScrollerCustomDuration scroller = new ScrollerCustomDuration(viewPager.getContext(), sInterpolator);
            mScroller.set(viewPager, scroller);

        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        // 200404 튜토리얼 대화창
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout t_linear = (LinearLayout) inflater.inflate(R.layout.conversation, null);
        LinearLayout.LayoutParams t_paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //처음, 재 접속인 경우
        Intent h2intent = getIntent();
        if (h2intent.getStringExtra("sToI") != null) {
            level = h2intent.getStringExtra("sToI");
        }

        //게임 중 뒤로가기 하고 온 경우 (from 카메라)
        Intent intent = getIntent();
        if (intent.getStringExtra("cToW4") != null && intent.getIntExtra("cToW3", -1) != -1) {
            level = intent.getStringExtra("cToW4");
            answerposition = intent.getIntExtra("cToW3", -1);
        }

        //게임 중 뒤로가기 하고 온 경우 (from 쓰기)
        Intent intent2 = getIntent();
        if (intent2.getStringExtra("wToI1") != null && intent2.getIntExtra("wToI4", -1) != -1) {
            level = intent2.getStringExtra("wToI1");
            answerposition = intent2.getIntExtra("wToI4", -1);
        }

        //게임하고 온 경우 (from 쓰기)
        Intent h2intent2 = getIntent();
        if (h2intent2.getStringExtra("wToI3") != null && h2intent2.getIntExtra("wToI2", -1) != -1) {
            answerposition = h2intent2.getIntExtra("wToI2", -1);
            level = h2intent2.getStringExtra("wToI3");
            sigOfWriteAndBack = 1;
        }

        setResult(level);

        countOfMedalsAndStage(game,level);

        MyAsyncTask asyncTask = new MyAsyncTask(countOfAns, myProgressBar);
        asyncTask.execute();

        viewPager.setClipToPadding(false);
        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin / 2);
        viewPager.setBackgroundColor(Color.argb(00,255,00,00));



        if (level.equals("st1") == true) {
            d.initializeData(level);
            viewPager.setAdapter(new h3_ViewPagerAdapter(h3_ImageViewMain.this, imageList, result));
            imageviewLayout.setBackground(getResources().getDrawable(R.drawable.st1_house));
        } else if (level.equals("st2") == true) {
            d.initializeData(level);
            viewPager.setAdapter(new h3_ViewPagerAdapter(h3_ImageViewMain.this, imageList, result));
            imageviewLayout.setBackground(getResources().getDrawable(R.drawable.st2_kitchen));
        } else if (level.equals("st3") == true) {
            d.initializeData(level);
            viewPager.setAdapter(new h3_ViewPagerAdapter(h3_ImageViewMain.this, imageList, result));
            imageviewLayout.setBackground(getResources().getDrawable(R.drawable.st3_mart));
        } else if (level.equals("st4") == true) {
            d.initializeData(level);
            viewPager.setAdapter(new h3_ViewPagerAdapter(h3_ImageViewMain.this, imageList, result));
            imageviewLayout.setBackground(getResources().getDrawable(R.drawable.st4_park));
        } else if (level.equals("st5") == true) {
            d.initializeData(level);
            viewPager.setAdapter(new h3_ViewPagerAdapter(h3_ImageViewMain.this, imageList, result));
            imageviewLayout.setBackground(getResources().getDrawable(R.drawable.st5_sport));
        } else if (level.equals("b") == true) {
            d.initializeData(level);
            viewPager.setAdapter(new h3_ViewPagerAdapter(h3_ImageViewMain.this, imageList, result));
            imageviewLayout.setBackground(getResources().getDrawable(R.drawable.b_music));
        } else{ //튜토리얼
            d.initializeData("t");
            viewPager.setAdapter(new h3_ViewPagerAdapter(this, imageList, null));
            // 양말 나오는 이미지 뷰의 배경 필요 - 07.07
            imageviewLayout.setBackground(getResources().getDrawable(R.drawable.t_background));
        }



        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked) {
                    isClicked = true;
                    settingBtn.setClickable(false);
                    back.setClickable(false);
                    next.setClickable(false);
                    prev.setClickable(false);
                    select.setClickable(false);
                }

                sfxService.SfxPlay(10);

                if (level.equals("b")) { //보너스 스테이지
                    int check = 0;
                    if (newStage == 2) {
                        if ((currentpoistion - 1) <= standardOfbonus[0]) check = 1;
                    } else if (newStage == 3) {
                        if ((currentpoistion - 1) <= standardOfbonus[1]) check = 1;
                    } else if (newStage == 4) {
                        if ((currentpoistion - 1) <= standardOfbonus[2]) check = 1;
                    } else if (newStage == 5) {
                        if ((currentpoistion - 1) <= standardOfbonus[3]) check = 1;
                    }
                    if (check == 1) {
                        viewPager.setCurrentItem(currentpoistion - 1);

                        check = 0;
                    }
                } else { // 나머지 스테이지
                    viewPager.setCurrentItem(currentpoistion - 1);

                }
                direction = "left";
                KevinMove kevinMove = new KevinMove();
                kevinMove.start();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isClicked) {
                    isClicked = true;
                    settingBtn.setClickable(false);
                    back.setClickable(false);
                    next.setClickable(false);
                    prev.setClickable(false);
                    select.setClickable(false);
                }

                sfxService.SfxPlay(10);

                if (level.equals("b")) { //보너스 일 경우
                    int check = 0;
                    if (newStage == 2) {
                        if ((currentpoistion + 1) <= standardOfbonus[0]) check = 1;
                        else
                            Toast.makeText(getApplicationContext(), "지금은 갈 수 없어요.", Toast.LENGTH_SHORT).show();
                    } else if (newStage == 3) {
                        if ((currentpoistion + 1) <= standardOfbonus[1]) check = 1;
                        else
                            Toast.makeText(getApplicationContext(), "지금은 갈 수 없어요.", Toast.LENGTH_SHORT).show();
                    } else if (newStage == 4) {
                        if ((currentpoistion + 1) <= standardOfbonus[2]) check = 1;
                        else
                            Toast.makeText(getApplicationContext(), "지금은 갈 수 없어요.", Toast.LENGTH_SHORT).show();
                    } else if (newStage == 5) {
                        if ((currentpoistion + 1) <= standardOfbonus[3]) check = 1;
                        else
                            Toast.makeText(getApplicationContext(), "지금은 갈 수 없어요.", Toast.LENGTH_SHORT).show();
                    }
                    settingBtn.setClickable(true);
                    back.setClickable(true);
                    next.setClickable(true);
                    prev.setClickable(true);
                    select.setClickable(true);
                    if (check == 1) {
                        viewPager.setCurrentItem(currentpoistion + 1);
                        check = 0;
                    }
                } else { //b가 아닌 나머지 단계
                    viewPager.setCurrentItem(currentpoistion + 1);

                }
                if ((d.getTuto()==0) && count == 2) { //튜토리얼 부분 (상의필요)
                    sayingFull.setBackgroundColor(Color.parseColor("#99000000"));
                    sayingLayout.setVisibility(View.VISIBLE);
                    conv.setText("잘했어!! 이제 사진기 버튼을 눌러 물건을 찾아보자.");
                    count++;

                    next.setClickable(false);

                }
                direction = "right";
                KevinMove kevinMove = new KevinMove();
                kevinMove.start();
            }
        });

        if ((d.getTuto() == 0) && !isInflated) { //튜토리얼 부분
            level = "t";
            t_linear.setBackgroundColor(Color.parseColor("#99000000")); // 배경 어둡게

            //인플레이션으로 겹치는 레이아웃을 깐다
            addContentView(t_linear, t_paramlinear);//이 부분이 레이아웃을 겹치는 부분
            isInflated = true;
            isFloating = true;

            sayingFull = findViewById(R.id.sayingFull);
            sayingLayout = findViewById(R.id.sayingLayout);
            ok = findViewById(R.id.conv_ok);
            conv = findViewById(R.id.conv);
            name = findViewById(R.id.name);
            floatingCharacter = findViewById(R.id.floatingCharacter);

            sayingFull.setBackgroundColor(Color.parseColor("#99000000"));
            sayingLayout.setVisibility(View.VISIBLE);
            ok.setVisibility(View.VISIBLE);
            floatingCharacter.setVisibility(View.VISIBLE);

            //인플레이터 뒷부분 다 클릭 안되게 하는 부분
            prev.setClickable(false);
            next.setClickable(false);
            back.setClickable(false);
            select.setClickable(false);
            settingBtn.setClickable(false);
            gif.setVisibility(View.GONE);

            back.setVisibility(View.GONE);
            settingBtn.setVisibility(View.GONE);

            name.setText("케빈");
            conv.setText("시작해볼까?");


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sfxService.SfxPlay(0);

                    if (count == 0) {
                        conv.setText("화살표 버튼을 눌러서 사진을 바꿀 수 있어.");
                        prev.setClickable(false);
                        next.setClickable(false);
                        select.setClickable(false);
                    } else if (count == 3) {
                        isFloating = false;
                        prev.setClickable(true);
                        next.setClickable(true);
                        sayingLayout.setVisibility(View.GONE);
                        sayingFull.setBackgroundColor(Color.parseColor("#00ff0000"));


                    } else {//원래 화면 터치되게
                        sayingLayout.setVisibility(View.GONE);
                        sayingFull.setBackgroundColor(Color.parseColor("#00ff0000"));
                        prev.setClickable(true);
                        next.setClickable(true);
                        if(count>3){
                            select.setClickable(true);
                        }
                        gif.setVisibility(View.VISIBLE);//할까말까
                    }
                    count++;
                }
            });
        }



        if (answerposition != -1 && sigOfOff == 1) { //게임하고 온 경우 (쓰기)
            viewPager.setCurrentItem(answerposition);
            currentpoistion = answerposition;
            if (answerposition == 0) { // 첫번째 문제 해결
                prev.setVisibility(View.INVISIBLE);
            }
            else if (answerposition == imageList.size() - 1) { // 마지막 문제 해결
                next.setVisibility(View.INVISIBLE);
            }
            currentpoistion = answerposition;
        }
        else if (sigOfOff == 1){ //첫 접속
            prev.setVisibility(View.INVISIBLE);
            currentpoistion = answerposition;
        }

        if(sigOfOff == 0) {
            viewPager.setCurrentItem(currentpoistion);
            if (currentpoistion == 0) { // 첫번째 문제 해결
                prev.setVisibility(View.INVISIBLE);
            }
            else if (currentpoistion == imageList.size() - 1) { // 마지막 문제 해결
                next.setVisibility(View.INVISIBLE);
            }
            sigOfOff = 1;
        }

        if (d.getTuto() != 0) {
            for(int i = 0; i < result.length; i ++) {
//                Log.d("qqwwe","result " + i + " : " + result[i]);
            }
            if (result[currentpoistion] == 1) {
                suc.setImageResource(R.drawable.medal_gold);
                suc.setVisibility(View.VISIBLE);
            }
            else if (result[currentpoistion] == 2) {
                suc.setImageResource(R.drawable.medal_silver);
                suc.setVisibility(View.VISIBLE);
            }
            else if (result[currentpoistion] == 3) {
                suc.setImageResource(R.drawable.medal_bronze);
                suc.setVisibility(View.VISIBLE);
            }
            else {
                suc.setVisibility(View.INVISIBLE);
            }
        }

        viewPager.addOnPageChangeListener(new NonSwipeViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("ITPANGPANG","onPageScrolled : "+position);
            }

            @Override
            public void onPageSelected(int position) {
                currentpoistion = position;

                if(d.getTuto() != 0) {
                    if (result[currentpoistion] == 1) {
                        suc.setImageResource(R.drawable.medal_gold);
                        suc.setVisibility(View.VISIBLE);
                    }
                    else if (result[currentpoistion] == 2) {
                        suc.setImageResource(R.drawable.medal_silver);
                        suc.setVisibility(View.VISIBLE);
                    }
                    else if (result[currentpoistion] == 3) {
                        suc.setImageResource(R.drawable.medal_bronze);
                        suc.setVisibility(View.VISIBLE);
                    }
                    else {
                        suc.setVisibility(View.INVISIBLE);
                    }
                }

                if (currentpoistion == 0) prev.setVisibility(View.INVISIBLE);
                else prev.setVisibility(View.VISIBLE);
                if (currentpoistion == imageList.size() - 1) next.setVisibility(View.INVISIBLE);
                else next.setVisibility(View.VISIBLE);


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == 0) { // 스크롤 멈췄을 때
                    isClicked = false;
                    settingBtn.setClickable(true);
                    back.setClickable(true);
                    next.setClickable(true);
                    prev.setClickable(true);
                    select.setClickable(true);
                }
            }
        });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfxService.SfxPlay(4);

                settingBtn.setClickable(false);
                back.setClickable(false);
                next.setClickable(false);
                prev.setClickable(false);
                select.setClickable(false);

                if(!isFloating){
                    //중복 방지
                    imageList.clear();
                    for (int i = 0; i < 60; i++) {
                        game[i] = 0;
                    }
                    // 카메라 인식 안하고 넘길 때
                    Intent intent = new Intent(h3_ImageViewMain.this, ClassifierActivity.class);//글쓰기로 가는 부분
                    intent.putExtra("cToW1", currentpoistion);
                    intent.putExtra("cToW2", level);
                    startActivity(intent);
                    //20.08.24_yeaji
                    overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                    finish();
                }
            }
        });


        //엔딩 판별 기준 코딩 필요 -- 20.08.02
        if(sigOfWriteAndBack == 1) {
            // 문제를 풀고 다시 돌아운 경우라면
            //write - > image
            int checkOfGoEnding = getPermissionForEndingMain(); //엔딩 몇 단계를 가는 지 저장하는 변수
            if(checkOfGoEnding != 0) { //엔딩을 볼 수 있는 기준이 된다면
                //앨범 아이콘이 생겼을 때, 최초 앨범 접속 여부
                SharedPreferences sp = getSharedPreferences("EndingCheck",MODE_PRIVATE);
                String e1 = sp.getString("e1","0");
                String e2 = sp.getString("e2","0");
                String e3 = sp.getString("e3","0");
                String e4 = sp.getString("e4","0");

                if(checkOfGoEnding == 1 && e1.equals("0")) {// 엔딩을 안 본 상태라면
                    setEnding(checkOfGoEnding, "e1");
                }
                else if(checkOfGoEnding == 2 && e2.equals("0")) {// 엔딩을 안 본 상태라면
                    setEnding(checkOfGoEnding, "e2");
                }
                else if(checkOfGoEnding == 3 && e3.equals("0")) {// 엔딩을 안 본 상태라면
                    setEnding(checkOfGoEnding, "e3");
                }
                else if(checkOfGoEnding == 4 && e4.equals("0")) {// 엔딩을 안 본 상태라면
                    setEnding(checkOfGoEnding, "e4");
                }
            }
        }
    }
    public void setEnding(int checkOfGoEnding, String e) { //엔딩으로 갈 인텐트 처리하는 함수
        Thread.interrupted();

        SharedPreferences sharedPreferences = getSharedPreferences("EndingCheck",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(e,"1");
        editor.commit();
        s.update(4, checkOfGoEnding, 2);

        Intent end = new Intent(h3_ImageViewMain.this, EndingMain.class);//엔딩으로 가는 부분
        end.putExtra("signalOfEnding2", checkOfGoEnding+"");
        startActivity(end);
        //20.08.24_yeaji
        overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
        finish();
    }

    public int getPermissionForEndingMain () {
        // 엔딩기준 필요 - 문제 수, 메달, 보너스 고려
        int result = 0; // 실제 평가 단계
        int total = 0; // 실제 평가 점수
        // 5단계 7문제 풀었는지
        int sub_5_total = 0;
        for (int i=s.dbHelper1.getDatabaseSize()-10; i<s.dbHelper1.getDatabaseSize();i++) {
            sub_5_total += game[i];
        }
        if (sub_5_total >= 7) {
            for (int j=0; j<s.dbHelper1.getDatabaseSize();j++) {
                int tmp = game[j];
                if(tmp == 1) { // 금
                    total += 5;
                }
                else if(tmp == 2) { // 은
                    total += 3;
                }
                else if(tmp == 3) { // 동
                    total += 1;
                }
            }
            if (total >= 220) {result = 1;}
            else if (total >= 150) {result = 2;}
            else if (total >= 100) {result = 3;}
            else if (total >= 35) {result = 4;}
            else {result = 0;}
        }
        else {result = 0;}
        return result;
    }

    /*
    20.01.10 다같이 모여서 의논을 하였으나
    예지의 핸드폰과 태블릿에서 오류가 발생하였음
    카메라로 인텐트 후 onActivityResult로 돌아오는 도중
    imageFilePath가 null값으로 바뀌어 앱이 강제 종료됨
    여러가지 실험을 해본 결과, 안드로이드 버전 9(pie)에서만 그런 것일 것이라는 주장이 가장 유력함
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//카메라를 켜고 확인 버튼을 눌러 돌아왔을 때 실행되는 함수임. 나중에 글씨 쓰기로 들어갔을 때의 예외처리 필요할 수 있음
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setResult(String level) {
        if (level.equals("st1")) {
            for (int i = 10; i < 20; i++) {
                result[i-10] = game[i];
            }
        } else if (level.equals("st2")) {
            for (int i = 20; i < 30; i++) {
                result[i-20] = game[i];
            }
        } else if (level.equals("st3")) {
            for (int i = 30; i < 40; i++) {
                result[i-30] = game[i];
            }
        } else if (level.equals("st4")) {
            for (int i = 40; i < 50; i++) {
                result[i-40] = game[i];
            }
        } else if (level.equals("st5")) {
            for (int i = 50; i < 60; i++) {
                result[i-50] = game[i];
            }
        } else if (level.equals("b")) {
            for (int i = 0; i < 10; i++) {
                result[i] = game[i];
            }
            newStage = state[0];

        }
    }
    public void checkOfMedals (int [] d, int i) {
        if (d[i] == 1 || d[i] == 2 || d[i] == 3) {
            countOfAns += 1;
        }
    }

    //20.08.24_yeaji
    public void countOfMedalsAndStage (int[] d, String stage) {
        if (stage.equals("st1")) {
            for (int i = 10; i< 20; i ++) {
                checkOfMedals(d,i);
            }
        }
        else if (stage.equals("st2")) {
            for (int i = 20; i< 30; i ++) {
                checkOfMedals(d,i);
            }
        }
        else if (stage.equals("st3")) {
            for (int i = 30; i< 40; i ++) {
                checkOfMedals(d,i);
            }
        }
        else if (stage.equals("st4")) {
            for (int i = 40; i< 50; i ++) {
                checkOfMedals(d,i);
            }
        }
        else if (stage.equals("st5")) {
            for (int i = 50; i< 60; i ++) {
                checkOfMedals(d,i);
            }
        }
        else if (stage.equals("b")){
            for (int i = 0; i< 10; i ++) {
                checkOfMedals(d,i);
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(h3_ImageViewMain.this, MusicService.class));
        sigOfSound = 0;
        sigOfOff = 0;
    }

    public float calcWidth(Drawable d){
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        float height = dm.heightPixels;
        float h_weight = 0.7f; // height weight

        return d.getIntrinsicWidth() * height * h_weight / d.getIntrinsicHeight();
    }

    public class KevinMove extends Thread{//시작 전
        @Override
        public void run(){
            while (gifcount<6){
                (h3_ImageViewMain.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(direction.equals("left")){
                            if(gifcount == 0) {
                                gif.setImageResource(R.drawable.kevin_moveleft1);
                            }else if(gifcount ==1){
                                gif.setImageResource(R.drawable.kevin_moveleft2);
                            }else if(gifcount ==2){
                                gif.setImageResource(R.drawable.kevin_moveleft1);
                            }else if(gifcount ==3){
                                gif.setImageResource(R.drawable.kevin_moveleft2);
                            }else if(gifcount ==4){
                                gif.setImageResource(R.drawable.kevin_moveleft1);
                            }else if(gifcount ==5){
                                gif.setImageResource(R.drawable.kevin_moveleft2);
                            }
                        }else if(direction.equals("right")){
                            if(gifcount == 0) {
                                gif.setImageResource(R.drawable.kevin_moveright1);
                            }else if(gifcount ==1){
                                gif.setImageResource(R.drawable.kevin_moveright2);
                            }else if(gifcount ==2){
                                gif.setImageResource(R.drawable.kevin_moveright1);
                            }else if(gifcount ==3){
                                gif.setImageResource(R.drawable.kevin_moveright2);
                            }else if(gifcount ==4){
                                gif.setImageResource(R.drawable.kevin_moveright1);
                            }else if(gifcount ==5){
                                gif.setImageResource(R.drawable.kevin_moveright2);
                            }
                        }
                        gifcount++;
                    }
                });
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gifcount = 0;
        }
    }
}