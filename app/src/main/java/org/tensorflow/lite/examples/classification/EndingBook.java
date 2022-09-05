package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//20.08.24_yeaji
public class EndingBook extends AppCompatActivity {


    setDB s; // 디비
    DataSet d; //소리및이미지

    private SfxService sfxService;
    ImageView settingBtn; // 확성기
    int sigOfSound; // 배경음을 켤지 말지 저장하는 변수


    Button e1, e2, e3, e4; //엔딩 1(우주선) 2(승무원) 3(귀농) 4(취준)
    ImageView back;

    int[] state;
    int[] game;

    private View decorView;
    private int uiOption;
    private long time= 0;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.endingbook_lay);

        e1 = findViewById(R.id.ending1);
        e2 = findViewById(R.id.ending2);
        e3 = findViewById(R.id.ending3);
        e4 = findViewById(R.id.ending4);
        back = findViewById(R.id.back);
        settingBtn = findViewById(R.id.setsound); //확성기

        s = new setDB(EndingBook.this);
        d = new DataSet(EndingBook.this);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfxService.SfxPlay(0);
                Intent i = new Intent(EndingBook.this, h1_StartMain.class);
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
            intent.putExtra("index",4);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
        }
        else{  //db상으로 음악이 꺼져있다면
            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(EndingBook.this, MusicService.class));
        }
    }
    //onResume에 들어왔을 때, 버튼에 의해 배경음을 전환할 함수
    public void setChangeSoundOnOff(){
        if(sigOfSound == 1){
            //현재 음악이 켜져있다면
            //음악을 끈다

            settingBtn.setImageResource(R.drawable.sound_off);
            stopService(new Intent(EndingBook.this, MusicService.class));

            s.update(1,0, 2);
            s.update(2,0, 2);
            sigOfSound = 0;
        }
        else{
            //현재 음악이 꺼져있다면
            //음악을 켠다
            settingBtn.setImageResource(R.drawable.sound_on);
            Intent intent=  new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("index",4);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
            s.update(1,1, 2);
            s.update(2,1, 2);
            sigOfSound = 0;
        }
    }

    protected void onResume() {
        super.onResume();
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
        setPermissionForEndingMain();
    }
    public void onClick (View view) {
        Intent ending1 = new Intent(EndingBook.this,EndingMain.class);
        switch (view.getId()) {
            case R.id.ending1:
                ending1.putExtra("signalOfEnding","1");
                break;
            case R.id.ending2:
                ending1.putExtra("signalOfEnding","2");
                break;
            case R.id.ending3:
                ending1.putExtra("signalOfEnding","3");
                break;
            case R.id.ending4:
                ending1.putExtra("signalOfEnding","4");
                break;
        }
        startActivity(ending1);
        overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(EndingBook.this, MusicService.class));
        sigOfSound = 0;
    }

    public void setPermissionForEndingMain () {

        int result = state[4]; // 오픈 가능한 샷
        s.printDB(state,s.dbHelper2.getDatabaseSize(),2);

        if(result == 1) { //우주선 가능
            e1.setVisibility(View.VISIBLE);
            e2.setVisibility(View.VISIBLE);
            e3.setVisibility(View.VISIBLE);
            e4.setVisibility(View.VISIBLE);
        }
        else if(result == 2) { //승무원 가능
            e1.setVisibility(View.INVISIBLE);
            e2.setVisibility(View.VISIBLE);
            e3.setVisibility(View.VISIBLE);
            e4.setVisibility(View.VISIBLE);
        }
        else if(result == 3) { //귀농 가능
            e1.setVisibility(View.INVISIBLE);
            e2.setVisibility(View.INVISIBLE);
            e3.setVisibility(View.VISIBLE);
            e4.setVisibility(View.VISIBLE);
        }
        else if(result == 4) { //취준 가능
            e1.setVisibility(View.INVISIBLE);
            e2.setVisibility(View.INVISIBLE);
            e3.setVisibility(View.INVISIBLE);
            e4.setVisibility(View.VISIBLE);
        }
        else { //없음
            e1.setVisibility(View.INVISIBLE);
            e2.setVisibility(View.INVISIBLE);
            e3.setVisibility(View.INVISIBLE);
            e4.setVisibility(View.INVISIBLE);
        }
    }
}

