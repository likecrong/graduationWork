package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class h0_LogoMain extends AppCompatActivity {
    // 음악만 조절하기

    int[] state;
    int bgm = 0, se = 0; //배경음악, 효과음
    //int tuto, n1; //튜토리얼, 최초 접속

    ImageView logo;
    SoundPool sfx;
    int logoSfx;

    private View    decorView;
    private int   uiOption;

    setDB s;
    DataSet d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h0_logo_activity);

        s = new setDB(getApplicationContext());
        d = new DataSet(getApplicationContext());

        // 사용자 게임 환경
        state = new int[s.dbHelper2.getDatabaseSize()];

        SharedPreferences sf = getSharedPreferences("checkNo1",MODE_PRIVATE);
        String no1 = sf.getString("no1","0");

        if (no1.equals("0")) {//처음 접속이라면
            SharedPreferences sharedPreferences = getSharedPreferences("checkNo1",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("no1","1");
            editor.commit();

            s.reset_first();//초기화 + 삽입
        }
        //s.reset_first();//초기화 + 삽입
        state = s.getDB2(); // 사용자 게임 환경 참조

        //내용 출력
        s.printDB(state, s.dbHelper2.getDatabaseSize(), 2);

        //배경음과 효과음에 대한 코드 내용이 없음 - 07.07
        bgm = state[1]; //배경음악
        se = state[2]; //효과음
        d.setTuto(state[3]); //튜토리얼 여부

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        logo = findViewById(R.id.logo);
        sfx = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        logoSfx = sfx.load(this, R.raw.logo, 1);

        Animation fadein, fadeout;
        fadein = AnimationUtils.loadAnimation(this, R.anim.long_fadein);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        logo.setImageAlpha(0);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                logo.setImageAlpha(255);
                logo.startAnimation(fadein);

            }
        }, 1000);


        fadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                    }
                }, 500);
                if(bgm==1){//로고 효과음을 환경설정과 연동
                    sfx.play(logoSfx, 1.0f, 1.0f, 0, 0, 1);
                }
            }
            @Override
            public void onAnimationEnd(Animation animation) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {


                        logo.startAnimation(fadeout);
                    }
                }, 3000);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setImageAlpha(0);
                Intent intent = new Intent(h0_LogoMain.this, h1_StartMain.class);
                startActivity(intent);
                overridePendingTransition(R.anim.short_fadein, R.anim.fadeout);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);
        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }

}