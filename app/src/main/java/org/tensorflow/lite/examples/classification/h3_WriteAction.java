/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.speech.tts.TextToSpeech;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class h3_WriteAction extends AppCompatActivity {

    int[] state;
    int[] game;

    ImageView settingBtn; // 확성기
    int sigOfSound; // 배경음을 켤지 말지 저장하는 변수

    TextView blankspace;

    ImageView speaker;
    ImageView act_view; // 이미지
    Button ok, imageOk;
    ImageView check, eraser;
    ImageView back, shadowBtn;
    TextView conv, name;
    // 200910
    LinearLayout sayingFull;
    RelativeLayout sayingLayout;
    ImageView floatingCharacter;

    LinearLayout resultLayout;

    //결과창 인플레이터 관련 변수
    TextView resulttime1, resulttime2, resultcheck1, resultcheck2;
    ImageView resultmedal;
    Button resultOK;

    LinearLayout clear;

    // 200806 녹음 레이아웃
    boolean isClear = false; // 클리어 스레드 끝났는지
    boolean isRec = false; // 녹음 끝났는지
    boolean isRecordingOn = false; // 녹음하는 중에 중지시키는지
    boolean isRecordFinished = false; // 첫 녹음인지
    MediaRecorder mRecorder; // 녹음
    MediaPlayer mPlayer; // 녹음파일 재생
    String fileName = ""; // 녹음파일 저장이름

    // 200812
    ImageView btnRecord;
    ImageView btnRecordEnd;
    FrameLayout fl;
    FrameLayout fl_2;
    FrameLayout fl_3;

    long cameratime;

    int medal = 0;
    int count = 0;
    int currentpoistion = 0;
    int wrongCount = 0;
    String level = "";
    String question = "";
    String answer = "";
    boolean isShadow = true;
    ArrayList<Integer> imageList;
    boolean isInflated = false;

    Boolean isReady = false;

    //speaking, writing, medal
    int stateOfActivity = 0;

    String[] nameList1 = new String[]{"mouse", "wallet","clock",
            "pen", "tissue", "vase", "shoes",
            "pillow", "chair", "eraser"};

    String[] nameList2 = new String[]{"bowl", "coffeepot", "cup",
            "frying pan", "ladle", "plate",
            "frige", "spatula", "toaster", "wok"};

    String[] nameList3 = new String[]{"banana", "broccoli", "crab",
            "cucumber", "lemon", "orange",
            "pineapple", "pizza", "cart", "strawberry"};

    String[] nameList4 = new String[]{"balloon", "bench", "bus",
            "butterfly", "cat", "dog",
            "dragonfly", "street sign", "swing", "traffic light"};

    String[] nameList5 = new String[]{"soccer", "basketball", "golf",
            "rugby", "volleyball", "tennis",
            "racket", "swim cap", "whistle", "scoreboard"};

    String[] nameList6 = new String[]{"piano", "guitar", "violin"
            , "drum", "flute", "ocarina"
            , "harmonica", "harp", "accordion", "trombone"};

    setDB s;

    CustomView mview;
    CustomView mview2;

    MyView myView;
    ImageView newimage;
    TextView guidetext;
    LinearLayout pictureLayout;

    Bitmap bitmap;

    Canvas canvas;

    private View decorView;
    private int uiOption;
    private long time = 0;

    int clearCount = 0;
    int resultCount = 0;
    int recordCount = 0;


    DataSet d;

    private SfxService sfxService;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) {
            d.callExit();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h3_action);

        blankspace = findViewById(R.id.blankspace);

        back = findViewById(R.id.endingOfGame);
        check = findViewById(R.id.check);
        eraser = findViewById(R.id.eraser);
        speaker = findViewById(R.id.speaker);

        guidetext = findViewById(R.id.guidetext);
        myView = (MyView) findViewById(R.id.myView);

        act_view = findViewById(R.id.act_image);

        act_view.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.UNSPECIFIED);


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

        LinearLayout camera_picure = (LinearLayout) inflater.inflate(R.layout.camera_picture, null);//처음에 찾아야될 물체의 사진을 보여주는 부분
        LinearLayout.LayoutParams paramlinear1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(camera_picure, paramlinear1);

        newimage = findViewById(R.id.newimage);
        pictureLayout = findViewById(R.id.startLayout);
        imageOk = findViewById(R.id.startBtn);
        shadowBtn = (ImageView) findViewById(R.id.shadowBtn);

        imageList = new ArrayList<Integer>();
        imageOk.setText("확인");//textList도 만들어서 넣기

        s = new setDB(getApplicationContext());
        d = new DataSet(h3_WriteAction.this, imageList);

        //인플레이션으로 겹치는 레이아웃을 깐다
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout result = (LinearLayout) inflater.inflate(R.layout.h3_resultview, null);
        LinearLayout.LayoutParams paramlinear3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(result, paramlinear3);//이 부분이 레이아웃을 겹치는 부분

        LinearLayout checkClear = (LinearLayout) inflater.inflate(R.layout.clear, null);
        LinearLayout.LayoutParams paramlinear4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(checkClear, paramlinear4);

        LinearLayout record = (LinearLayout) inflater.inflate(R.layout.record, null);
        LinearLayout.LayoutParams paramlinear5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(record, paramlinear5);

        LinearLayout t_linear = (LinearLayout) inflater.inflate(R.layout.conversation, null);
        LinearLayout.LayoutParams t_paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(t_linear, t_paramlinear);

        clear = (LinearLayout) findViewById(R.id.clear);

        mRecorder = new MediaRecorder();

        //200812
        btnRecord = findViewById(R.id.record);
        btnRecordEnd = findViewById(R.id.btnRecordEnd);

        btnRecord.setAlpha(100);

        resultLayout = findViewById(R.id.resultLayout);
        resulttime1 = findViewById(R.id.resulttime1);
        resulttime2 = findViewById(R.id.resulttime2);
        resultcheck1 = findViewById(R.id.resultcheck1);
        resultcheck2 = findViewById(R.id.resultcheck2);
        resultmedal = findViewById(R.id.resultmedal);
        resultOK = findViewById(R.id.resultOK);

        settingBtn = findViewById(R.id.setsound); //확성기

        stateOfActivity = 0;

        blankspace.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                blankspace.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (level.equals("t") == true) {
                    mview = new CustomView(fl.getContext(), "sock", false, blankspace.getWidth(), blankspace.getHeight());
                    mview2 = new CustomView(fl.getContext(), "sock", true, blankspace.getWidth(), blankspace.getHeight());
                    question = "sock";
                } else if (level.equals("st1") == true) {
                    mview = new CustomView(fl.getContext(), nameList1[currentpoistion], false, blankspace.getWidth(), blankspace.getHeight());
                    mview2 = new CustomView(fl.getContext(), nameList1[currentpoistion], true, blankspace.getWidth(), blankspace.getHeight());
                    question = nameList1[currentpoistion];
                } else if (level.equals("st2") == true) {
                    mview = new CustomView(fl.getContext(), nameList2[currentpoistion], false, blankspace.getWidth(), blankspace.getHeight());
                    mview2 = new CustomView(fl.getContext(), nameList2[currentpoistion], true, blankspace.getWidth(), blankspace.getHeight());
                    question = nameList2[currentpoistion];
                } else if (level.equals("st3") == true) {
                    mview = new CustomView(fl.getContext(), nameList3[currentpoistion], false, blankspace.getWidth(), blankspace.getHeight());
                    mview2 = new CustomView(fl.getContext(), nameList3[currentpoistion], true, blankspace.getWidth(), blankspace.getHeight());
                    question = nameList3[currentpoistion];
                } else if (level.equals("st4") == true) {
                    mview = new CustomView(fl.getContext(), nameList4[currentpoistion], false, blankspace.getWidth(), blankspace.getHeight());
                    mview2 = new CustomView(fl.getContext(), nameList4[currentpoistion], true, blankspace.getWidth(), blankspace.getHeight());
                    question = nameList4[currentpoistion];
                } else if (level.equals("st5") == true) {
                    mview = new CustomView(fl.getContext(), nameList5[currentpoistion], false, blankspace.getWidth(), blankspace.getHeight());
                    mview2 = new CustomView(fl.getContext(), nameList5[currentpoistion], true, blankspace.getWidth(), blankspace.getHeight());
                    question = nameList5[currentpoistion];
                } else if (level.equals("b") == true) {
                    mview = new CustomView(fl.getContext(), nameList6[currentpoistion], false, blankspace.getWidth(), blankspace.getHeight());
                    mview2 = new CustomView(fl.getContext(), nameList6[currentpoistion], true, blankspace.getWidth(), blankspace.getHeight());
                    question = nameList6[currentpoistion];
                }
                // 200812
                Rec rec = new Rec();
                rec.start();


                fl.addView(mview);
                fl_3.addView(mview2);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sfxService = new SfxService(getApplicationContext());
        sfxService.SfxLoad();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sfxService = new SfxService(getApplicationContext());
        sfxService.SfxLoad();
        if (stateOfActivity == 0) {
            back.setClickable(false);
            shadowBtn.setClickable(false);
            settingBtn.setClickable(false);
            check.setClickable(false);
            eraser.setClickable(false);
            speaker.setClickable(false);
        } else if (stateOfActivity == 1) {
            setFirstSoundOnOff();
        }
    }

    //onResume에 들어왔을 때, 처음 한 번 실행할 함수
    public void setFirstSoundOnOff() {
        if (sigOfSound == 1) { //db상으로 음악이 켜져있다면
            settingBtn.setImageResource(R.drawable.sound_on);
            Intent intent = new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("index", 5);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
        } else {  //db상으로 음악이 꺼져있다면
            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(h3_WriteAction.this, MusicService.class));
        }
    }

    //onResume에 들어왔을 때, 버튼에 의해 배경음을 전환할 함수
    public void setChangeSoundOnOff() {
        if (sigOfSound == 1) {
            //현재 음악이 켜져있다면
            //음악을 끈다

            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(h3_WriteAction.this, MusicService.class));

            s.update(1, 0, 2);
            s.update(2, 0, 2);
            sigOfSound = 0;
        } else {
            //현재 음악이 꺼져있다면
            //음악을 켠다
            settingBtn.setImageResource(R.drawable.sound_on);
            Intent intent = new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("index", 5);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
            s.update(1, 1, 2);
            s.update(2, 1, 2);
            sigOfSound = 0;
        }
    }

    public void onResume() {
        super.onResume();

        game = new int[s.dbHelper1.getDatabaseSize()];
        state = new int[s.dbHelper2.getDatabaseSize()];

        game = s.getDB1();
        state = s.getDB2();

        sigOfSound = state[1];
        d.setTuto(state[3]); //튜토리얼 여부


        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //누를때마다 디비 읽기
                state = s.getDB2();
                sigOfSound = state[1];
                s.printDB(state, s.dbHelper2.getDatabaseSize(), 2);

                sfxService.SfxPlay(0);
                setChangeSoundOnOff();
            }
        });

        if (d.getTuto() == 0) {
            back.setVisibility(View.INVISIBLE);
        }
        if (stateOfActivity == 0) {
            settingBtn.setVisibility(View.INVISIBLE);
        }

        Intent i = getIntent();
        currentpoistion = i.getIntExtra("cToW1", 0);
        level = i.getStringExtra("cToW2");
        cameratime = i.getLongExtra("time", -1);

        fl = (FrameLayout) findViewById(R.id.suggetion);
        fl_2 = (FrameLayout) findViewById(R.id.drawingArea);
        fl_3 = (FrameLayout) findViewById(R.id.shadowView);

        if (d.getTuto() == 0 && !isInflated) {
            level = "t";
            sayingFull = findViewById(R.id.sayingFull);
            sayingLayout = findViewById(R.id.sayingLayout);
            sayingLayout.setVisibility(View.VISIBLE);
            sayingFull.setBackgroundColor(Color.parseColor("#99000000"));
            ok = findViewById(R.id.conv_ok);
            conv = findViewById(R.id.conv);
            name = findViewById(R.id.name);

            ok.setVisibility(View.VISIBLE);

            floatingCharacter = findViewById(R.id.floatingCharacter);
            floatingCharacter.setVisibility(View.VISIBLE);

            btnRecord.setClickable(false);
            speaker.setClickable(false);
            back.setClickable(false);
            settingBtn.setClickable(false);


            sayingFull.setClickable(true);

            name.setText("케빈");
            conv.setText("음성을 듣고 마이크 버튼을 눌러서 녹음해봐!");

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sfxService.SfxPlay(0);
                    sayingFull.setBackgroundColor(Color.parseColor("#00ff0000"));

                    btnRecord.setClickable(false);
                    back.setClickable(true);
                    speaker.setClickable(true);

                    ok.setVisibility(View.GONE);
                    sayingFull.setVisibility(View.GONE);
                    sayingLayout.setVisibility(View.GONE);
                    floatingCharacter.setVisibility(View.GONE);

                    isReady = true;
                    isInflated = true;
                }
            });
        }else{
            isInflated = true;
        }

        d.initializeData(level);
        act_view.setImageResource(imageList.get(currentpoistion));
        newimage.setImageResource(imageList.get(currentpoistion));

        // 200902 수용 - 각 문제에 맞는 가이드 음성 로드하는 부분
        sfxService.VoiceLoad(level, currentpoistion + 1);

        fl_2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fl_2.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                bitmap = Bitmap.createBitmap(fl_2.getWidth(), fl_2.getHeight(), Bitmap.Config.ARGB_8888);//나중에 핸드폰의 가로세로 받아오기
                canvas = new Canvas(bitmap);
            }
        });


        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfxService.SfxPlay(100);
            }
        });

        imageOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sfxService.SfxPlay(0);

                shadowBtn.setClickable(true);
                settingBtn.setClickable(true);
                fl_2.setClickable(true);
                fl_2.setContextClickable(true);
                fl_2.setFocusable(true);
                fl_2.setEnabled(true);
                check.setClickable(true);
                eraser.setClickable(true);

                pictureLayout.setVisibility(View.GONE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sfxService.SfxPlay(0);

                d.imageList.clear();
                Intent intent = new Intent(h3_WriteAction.this, h3_ImageViewMain.class);//이미지 뷰로 가는 부분
                intent.putExtra("wToI1", level);
                intent.putExtra("wToI4", currentpoistion);

                startActivity(intent);

                overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                finish();
            }
        });

        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sfxService.SfxPlay(12);

                myView.path.reset();
                myView.invalidate();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myView.draw(canvas);
                canvas.drawPath(myView.path, myView.paint);
                myView.invalidate();
                runTextRecognition(bitmap);
            }
        });
        shadowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isShadow) {
                    sfxService.SfxPlay(0);
                    isShadow = false;
                    fl_3.setVisibility(View.INVISIBLE);
                    shadowBtn.setImageResource(R.drawable.bulboff);
                } else {
                    sfxService.SfxPlay(1);
                    isShadow = true;
                    fl_3.setVisibility(View.VISIBLE);
                    shadowBtn.setImageResource(R.drawable.bulbon);
                }
            }
        });


        btnRecordEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfxService.SfxPlay(3);

                stateOfActivity = 1;

                settingBtn.setClickable(true);
                btnRecord.setVisibility(View.INVISIBLE);
                btnRecordEnd.setVisibility(View.INVISIBLE);
                check.setVisibility(View.VISIBLE);
                myView.setVisibility(View.VISIBLE);
                eraser.setVisibility(View.VISIBLE);
                shadowBtn.setVisibility(View.VISIBLE);
                fl_3.setVisibility(View.VISIBLE);

                guidetext.setText("글씨를 따라 써봐요!");
                isRecordFinished = true;
                settingBtn.setVisibility(View.VISIBLE);
                setFirstSoundOnOff();

                if (d.getTuto() == 0) {

                    sayingFull.setVisibility(View.VISIBLE);
                    sayingFull.setBackgroundColor(Color.parseColor("#99000000"));
                    sayingLayout.setVisibility(View.VISIBLE);
                    ok = findViewById(R.id.conv_ok);
                    conv = findViewById(R.id.conv);
                    name = findViewById(R.id.name);

                    ok.setVisibility(View.VISIBLE);

                    floatingCharacter = findViewById(R.id.floatingCharacter);
                    floatingCharacter.setVisibility(View.VISIBLE);

                    back.setClickable(false);
                    speaker.setClickable(false);
                    shadowBtn.setClickable(false);
                    eraser.setClickable(false);
                    check.setClickable(false);

                    name.setText("케빈");
                    conv.setText("이제 글씨로 써보자! 다 쓰고 확인 버튼을 누르면 돼.");

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            sfxService.SfxPlay(0);
                            sayingFull.setBackgroundColor(Color.parseColor("#00ff0000"));


                            back.setClickable(true);
                            speaker.setClickable(true);
                            shadowBtn.setClickable(true);
                            eraser.setClickable(true);
                            check.setClickable(true);

                            ok.setVisibility(View.GONE);
                            sayingLayout.setVisibility(View.GONE);
                            sayingFull.setVisibility(View.GONE);
                            floatingCharacter.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isRec) {//녹음 시작

                    sfxService.SfxPlay(1);//삐~소리 내기
                    btnRecord.setClickable(false);
                    btnRecordEnd.setVisibility(View.INVISIBLE);//넘어가기 버튼 숨기기
                    btnRecordEnd.setClickable(false);
                    isRec = true;

                    back.setClickable(false);
                    speaker.setClickable(false);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (isRec) {
                                btnRecord.setImageResource(R.drawable.recstop);//중지 버튼으로 변경
                                btnRecord.setAlpha(150);
                                isRecordingOn = true;
                                initAudioRecorder();
                                mRecorder.start(); // 녹음 시작
                                guidetext.setText("녹음 중");
                            }


                        }
                    }, 500);//0.5초 후 녹음 시작
                    Handler handler2 = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (isRec) {
                                btnRecord.setAlpha(255);
                                btnRecord.setClickable(true);

                            }


                        }
                    }, 1500);//1.5초 후부터 중지 가능
                } else {//녹음 중에 중지

                    isRec = false;
                    sfxService.SfxPlay(0);

                    btnRecord.setImageResource(R.drawable.record);//재생 중 버튼으로 변경

                    btnRecord.setClickable(false);
                    btnRecordEnd.setClickable(false);
                    back.setClickable(false);
                    speaker.setClickable(false);

                    mRecorder.stop(); // 녹음 끝
                    guidetext.setText("재생 중");

                    mPlayer = new MediaPlayer();

                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) { // 재생 끝나면

                            sfxService.SfxPlay(11);
                            btnRecord.setClickable(true);

                            btnRecord.setImageResource(R.drawable.re_record);//다시하기 버튼으로 변경
                            btnRecordEnd.setVisibility(View.VISIBLE);
                            btnRecordEnd.setClickable(true);
                            back.setClickable(true);
                            speaker.setClickable(true);

                            guidetext.setText("다시 녹음해볼까요?");

                            //해제하기
                            mPlayer.release();
                            mPlayer = null;

                            if ((d.getTuto() == 0) && (count == 0)) {
                                sayingFull.setBackgroundColor(Color.parseColor("#99000000"));
                                sayingLayout = findViewById(R.id.sayingLayout);
                                sayingLayout.setVisibility(View.VISIBLE);
                                sayingFull.setVisibility(View.VISIBLE);
                                ok = findViewById(R.id.conv_ok);
                                conv = findViewById(R.id.conv);
                                name = findViewById(R.id.name);

                                ok.setVisibility(View.VISIBLE);

                                floatingCharacter = findViewById(R.id.floatingCharacter);
                                floatingCharacter.setVisibility(View.VISIBLE);

                                btnRecord.setClickable(false);
                                btnRecordEnd.setClickable(false);
                                back.setClickable(false);
                                speaker.setClickable(false);

                                name.setText("케빈");
                                conv.setText("마이크 버튼을 누르면 다시 녹음할 수 있어.");

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        sfxService.SfxPlay(0);

                                        if (count == 0) {
                                            conv.setText("녹음이 끝났으면 넘어가기 버튼을 눌러!");
                                        } else {
                                            sayingFull.setBackgroundColor(Color.parseColor("#00ff0000"));

                                            btnRecord.setClickable(true);

                                            back.setClickable(true);
                                            speaker.setClickable(true);
                                            btnRecordEnd.setClickable(true);

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
                    });
                    try {
                        mPlayer.setDataSource(fileName);
                        mPlayer.prepare();
                        mPlayer.start(); // 파일 재생
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    private void runTextRecognition(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        recognizer.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {
                                processTextRecognitionResult(texts);

                                answer = texts.getText();

                                checkAnswer(answer);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                e.printStackTrace();
                            }
                        });
    }

    private void checkAnswer(String answer) {
        MeasureMedal();
        answer = answer.toLowerCase();
        if (answer.equals(question)) {
            if (level.equals("st1")) {
                String tmp = "1" + currentpoistion;
                int newtmp = Integer.parseInt(tmp);
                if (game[newtmp] == 0) { // 첫 저장
                    s.update(newtmp, medal, 1);
                } else {
                    if (medal <= game[newtmp]) {
                        s.update(newtmp, medal, 1);
                    }
                }
            } else if (level.equals("st2")) {
                String tmp = "2" + currentpoistion;
                int newtmp = Integer.parseInt(tmp);
                if (game[newtmp] == 0) { // 첫 저장
                    s.update(newtmp, medal, 1);
                } else {
                    if (medal <= game[newtmp]) {
                        s.update(newtmp, medal, 1);
                    }
                }
            } else if (level.equals("st3")) {
                String tmp = "3" + currentpoistion;
                int newtmp = Integer.parseInt(tmp);
                if (game[newtmp] == 0) { // 첫 저장
                    s.update(newtmp, medal, 1);
                } else {
                    if (medal <= game[newtmp]) {
                        s.update(newtmp, medal, 1);
                    }
                }
            } else if (level.equals("st4")) {
                String tmp = "4" + currentpoistion;
                int newtmp = Integer.parseInt(tmp);
                if (game[newtmp] == 0) { // 첫 저장
                    s.update(newtmp, medal, 1);
                } else {
                    if (medal <= game[newtmp]) {
                        s.update(newtmp, medal, 1);
                    }
                }
            } else if (level.equals("st5")) {
                String tmp = "5" + currentpoistion;
                int newtmp = Integer.parseInt(tmp);
                if (game[newtmp] == 0) { // 첫 저장
                    s.update(newtmp, medal, 1);
                } else {
                    if (medal <= game[newtmp]) {
                        s.update(newtmp, medal, 1);
                    }
                }
            } else if (level.equals("b")) {
                String tmp = "0" + currentpoistion;
                int newtmp = Integer.parseInt(tmp);
                if (game[newtmp] == 0) { // 첫 저장
                    s.update(newtmp, medal, 1);
                } else {
                    if (medal <= game[newtmp]) {
                        s.update(newtmp, medal, 1);
                    }
                }
            }
            d.imageList.clear();

            myView.invalidate();

            bitmap.eraseColor(Color.WHITE);

            stateOfActivity = 2;
            ShowClear showClear = new ShowClear();
            showClear.start();

            ShowResult showResult = new ShowResult();
            showResult.start();

        } else {

            sfxService.SfxPlay(13);

            wrongCount++;
            if (wrongCount == 3) {
                Toast.makeText(getApplicationContext(), "가이드를 따라 천천히 써봐요!", Toast.LENGTH_SHORT).show();
            } else if (wrongCount == 10000) {
                wrongCount = 9999;
            }
        }
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            return;
        }
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(h3_WriteAction.this, MusicService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 200806 clear 띄우기
    public class ShowClear extends Thread {
        @Override
        public void run() {
            while (clearCount < 2) {
                (h3_WriteAction.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (clearCount == 0) {
                            clear.setVisibility(View.VISIBLE);
                        } else if (clearCount == 1) {
                            clear.setVisibility(View.GONE);
                            isClear = true;
                        }
                        clearCount++;
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class ShowResult extends Thread {//시작 전

        @Override
        public void run() {

            while (resultCount < 6) {
                (h3_WriteAction.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DateFormat df = new SimpleDateFormat("mm:ss"); // HH=24h, hh=12h
                        String str = df.format(cameratime);
                        if (resultCount == 0) {
                            stopService(new Intent(h3_WriteAction.this, MusicService.class));
                            sfxService.SfxPlay(9);


                            back.setClickable(false);
                            shadowBtn.setClickable(false);
                            settingBtn.setClickable(false);
                            check.setClickable(false);
                            eraser.setClickable(false);
                            speaker.setClickable(false);

                            clear.setVisibility(View.VISIBLE);

                        } else if (resultCount == 1) {
                            clear.setVisibility(View.GONE);

                        } else if (resultCount == 2) {
                            sfxService.SfxPlay(5);
                            resultLayout.setVisibility(View.VISIBLE);
                        } else if (resultCount == 3) {
                            resulttime2.setText(str);
                            resulttime1.setVisibility(View.VISIBLE);
                            resulttime2.setVisibility(View.VISIBLE);
                        } else if (resultCount == 4) {
                            resultcheck2.setText(wrongCount + 1 + "번");
                            resultcheck1.setVisibility(View.VISIBLE);
                            resultcheck2.setVisibility(View.VISIBLE);
                        } else if (resultCount == 5) {
                            switch (medal) {
                                case 1:
                                    sfxService.SfxPlay(8);
                                    sfxService.SfxPlay(6);
                                    break;
                                case 2:
                                    sfxService.SfxPlay(6);
                                    break;
                                case 3:
                                    sfxService.SfxPlay(7);
                                    break;
                            }
                            resultmedal.setVisibility(View.VISIBLE);
                            resultOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    sfxService.SfxPlay(1);

                                    Intent intent2 = new Intent(h3_WriteAction.this, h3_ImageViewMain.class);//이미지 뷰로 가는 부분
                                    intent2.putExtra("wToI2", currentpoistion);
                                    intent2.putExtra("wToI3", level);
                                    if (d.getTuto() == 0) {
                                        d.imageList.clear();
                                        Intent t_intent = new Intent(h3_WriteAction.this, h2_TreeActivityMain.class);//트리뷰로 가는 부분
                                        startActivity(t_intent);
                                        //20.08.24_yeaji
                                        overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                                        finish();
                                    } else {
                                        startActivity(intent2); //이미지 뷰
                                        //20.08.24_yeaji
                                        overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                                        finish();
                                    }
                                }
                            });
                            resultOK.setVisibility(View.VISIBLE);
                        }
                        resultCount++;
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //15초 30초 그 이상
    //2회 3회 그 이상
    private void MeasureMedal() {

        int grade = 0;

        //카메라 인식 시간
        if (cameratime < 30000) {
            grade += 1;
        } else if (cameratime < 60000) {
            grade += 2;
        } else {
            grade += 3;
        }

        //손글씨 시도 횟수
        if (wrongCount <= 3) {
            grade += 1;
        } else if (wrongCount <= 5) {
            grade += 2;
        } else if (wrongCount <= 10) {
            grade += 3;
        } else {
            grade += 4;
        }

        //채점
        if (grade <= 2) {
            resultmedal.setImageResource(R.drawable.medal_gold);
            medal = 1;
        } else if (grade <= 4) {
            resultmedal.setImageResource(R.drawable.medal_silver);
            medal = 2;
        } else {
            resultmedal.setImageResource(R.drawable.medal_bronze);
            medal = 3;
        }
    }


    public class Rec extends Thread {//시작 전

        @Override
        public void run() {


            while (recordCount < 9) {
                (h3_WriteAction.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 200906 튜토리얼에서 대화창 닫기 전에 소리 들림 --> if문 쓰면 튜토리얼 후 진행에서 효과음 안 들림
                        if ((d.getTuto() == 1) || ((d.getTuto() == 0) && (isReady))) {
                            if (recordCount == 0) {

                                stopService(new Intent(h3_WriteAction.this, MusicService.class));

                                btnRecord.setClickable(false);

                                speaker.setClickable(false);
                            } else if (recordCount == 1) {//"따라해보세요"

                                sfxService.SfxPlay(101);

                            } else if (recordCount == 4) {//단어 재생1

                                sfxService.SfxPlay(100);

                            } else if (recordCount == 6) {//단어 재생2

                                sfxService.SfxPlay(100);


                            } else if (recordCount == 8) {//녹음 버튼 보여주기 + 효과음 뿅 + 온클릭 리스너 등록

                                btnRecord.setAlpha(255);
                                btnRecord.setClickable(true);
                                speaker.setClickable(true);

                                sfxService.SfxPlay(11);


                                guidetext.setText("마이크 버튼을 눌러 녹음해보아요!");


                            }
                            recordCount++;
                        }
                    }

                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void initAudioRecorder() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        File file = new File(getFilesDir(), "record.aac");
        fileName = file.getAbsolutePath();  // 파일 위치 가져옴
//        Toast.makeText(getApplicationContext(), "파일 위치:" + fileName, Toast.LENGTH_SHORT).show();

        mRecorder.setOutputFile(fileName);

        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
