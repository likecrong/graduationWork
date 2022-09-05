package org.tensorflow.lite.examples.classification;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class h1_StartMain extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_RECORD = Manifest.permission.RECORD_AUDIO;


    int[] game;
    int[] state;
    setDB s;
    DataSet d;

    private SfxService sfxService;

    Boolean isSetting = false;
    LinearLayout settingLayout, clearLayout;
    Button start,  clearData, clearYes, clearNo, sc, exitButton;
    ImageView settingBtn;
    ImageView endingBook;
    RadioButton bm1, bm2;
    RadioGroup bm;

    View decorView;
    int uiOption;

    private long time= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h1_start_activity);

//        sfxService = new SfxService(getApplicationContext());
//        sfxService.function();

        exitButton = (Button) findViewById(R.id.Button_End);
        endingBook = findViewById(R.id.endingBook);

        s = new setDB(getApplicationContext());
        d = new DataSet(h1_StartMain.this, exitButton);



        //인플레이션으로 겹치는 레이아웃을 깐다
        LayoutInflater inflater = (LayoutInflater) getSystemService(

                Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout setting = (LinearLayout) inflater.inflate(R.layout.h1_setting, null);
        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(setting, paramlinear);//이 부분이 레이아웃을 겹치는 부분

        LinearLayout clear = (LinearLayout) inflater.inflate(R.layout.h1_clear_data, null);
        LinearLayout.LayoutParams paramlinear2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(clear, paramlinear2);//이 부분이 레이아웃을 겹치는 부분


        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        start = findViewById(R.id.Button_Start);
        StartListenerClass startbuttonListener = new StartListenerClass();
        start.setOnClickListener(startbuttonListener);

        settingBtn = findViewById(R.id.Button_Setting);
        SettingListenerClass setbuttonListener = new SettingListenerClass();
        settingBtn.setOnClickListener(setbuttonListener);
        settingLayout = findViewById(R.id.settingLayout);
        clearLayout = findViewById(R.id.clearLayout);

        //setMusic = findViewById(R.id.setMusic);
        //setSfx = findViewById(R.id.setSfx);

        clearData = findViewById(R.id.clearData);
        clearYes = findViewById(R.id.clearYes);
        clearNo = findViewById(R.id.clearNo);

        sc = findViewById(R.id.settingCheck);

        //라디오 그룹 설정
        bm = (RadioGroup) findViewById(R.id.backMusic);
        bm.setOnCheckedChangeListener(radioGroupButtonChangeListener1);

        //라디오 버튼 설정
        bm1 = (RadioButton) findViewById(R.id.bm1);
        bm2 = (RadioButton) findViewById(R.id.bm2);

        /* if문 안으로 들어갈려면, 카메라 권한이 허용되지 않아야 함
            hasPermission()이 0 false 여야함
         */
        if (!hasPermission()) {
            requestPermission();
        }


        d.setExit();

        //데이터 베이스 관련 버튼
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sfxService.SfxPlay(2);

                clearLayout.setVisibility(View.VISIBLE);

                bm1.setClickable(false);
                bm2.setClickable(false);
                sc.setClickable(false);

                settingBtn.setClickable(false);

            }
        });
        clearYes.setOnClickListener(new View.OnClickListener() {//세이브 데이터 초기화 코딩하기
            @Override
            public void onClick(View view) {

                sfxService.SfxPlay(1);

                clearLayout.setVisibility(View.GONE);
                bm1.setClickable(true);
                bm2.setClickable(true);
                settingBtn.setClickable(true);
                sc.setClickable(true);
                s.reset();

                SharedPreferences album = getSharedPreferences("ALBUM",MODE_PRIVATE);
                SharedPreferences checkno1_1 = getSharedPreferences("checkNo1",MODE_PRIVATE);
                SharedPreferences endingcheck_1 = getSharedPreferences("EndingCheck",MODE_PRIVATE);
                SharedPreferences isFirst = getSharedPreferences("isFirst",MODE_PRIVATE);

                SharedPreferences.Editor e_a_new = album.edit();
                e_a_new.putString("NEW","0");
                e_a_new.commit();

                SharedPreferences.Editor e_c_no1 = checkno1_1.edit();
                e_c_no1.putString("no1","0");
                e_c_no1.commit();

                SharedPreferences.Editor e_e = endingcheck_1.edit();
                e_e.putString("e1","0");
                e_e.putString("e2","0");
                e_e.putString("e3","0");
                e_e.putString("e4","0");
                e_e.commit();

                SharedPreferences.Editor e_i = isFirst.edit();
                e_i.putString("st1","false");
                e_i.putString("st2","false");
                e_i.putString("st3","false");
                e_i.putString("st4","false");
                e_i.putString("st5","false");
                e_i.putString("b","false");
                e_i.putString("UserName","");
                e_i.commit();

                Toast.makeText(getApplicationContext(),"데이터 초기화 완료",Toast.LENGTH_SHORT).show();
                Intent gofirst = new Intent(h1_StartMain.this,h0_LogoMain.class);
                startActivity(gofirst);
                overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                finish();
            }
        });
        clearNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sfxService.SfxPlay(1);

                clearLayout.setVisibility(View.GONE);
                bm1.setClickable(true);
                bm2.setClickable(true);
                settingBtn.setClickable(true);
                sc.setClickable(true);
            }
        });
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sfxService.SfxPlay(1);

                settingLayout.setVisibility(View.GONE);
                start.setClickable(true);
                exitButton.setClickable(true);
                endingBook.setClickable(true);
                isSetting = false;
            }
        });

        bm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfxService.SfxPlay(0);
            }
        });
        bm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfxService.SfxPlay(0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sfxService = new SfxService(getApplicationContext());
        sfxService.SfxLoad();
    }

    //배경음악 라디오 그룹 클릭 리스너
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.bm1&&!d.getMusicOn()) {
                Intent intent=  new Intent(getApplicationContext(), MusicService.class);
                intent.putExtra("index",1);//몇번째 노래를 재생할 것인지 MusicService에 전달
                startService(intent);

                //디비2
                s.update(1,1, 2);

                //배경음악 온
                d.setBackMusic(1);
                d.setMusicOn(true);

                //효과음 온
                s.update(2,1, 2);
                d.setSfx(1);
            }
            else if (i == R.id.bm2&&d.getMusicOn()) {
                stopService(new Intent(h1_StartMain.this, MusicService.class));

                //디비2
                s.update(1,0, 2);

                //배경음악 오프
                d.setBackMusic(0);
                d.setMusicOn(false);

                //효과음 오프
                s.update(2,0, 2);
                d.setSfx(0);
            }

        }
    };



    @Override
    protected void onResume() {
        super.onResume();

        game = s.getDB1();
        state = s.getDB2();
        s.printDB(state,s.dbHelper2.getDatabaseSize(),2);

        d.setBackMusic(state[1]); //배경음악
        d.setSfx(state[2]); //효과음
        d.setTuto(state[3]); //튜토리얼 여부

        if(d.getBackMusic() == 1){//db상으로 음악이 켜져있다면
            bm.check(R.id.bm1);
            Intent intent=  new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("index",1);//몇번째 노래를 재생할 것인지 MusicService에 전달
            startService(intent);
            s.update(1,1, 2);
            d.setMusicOn(true);

        }else{
            bm.check(R.id.bm2);
            d.setMusicOn(false);
            s.update(1,0, 2);
        }
        int currentLevelOfEndingshot = getPermissionForEndingMain(); //현재 레벨
        int dbLevelOfEndingshot = state[4]; // 이전 레벨

        //앨범 아이콘이 생겼을 때, 최초 앨범 접속 여부
        SharedPreferences sf = getSharedPreferences("ALBUM",MODE_PRIVATE);
        String sig = sf.getString("NEW","1");

        if (sig.equals("1")|| (currentLevelOfEndingshot != dbLevelOfEndingshot) ) {  //변경 사항 있음(new 활성화)
            // 1. 앨범을 처음 봤을 때
            // 2. 새로운 엔딩이 생겼을 때
            endingBook.setImageResource(R.drawable.endingbook_new);
            SharedPreferences sharedPreferences = getSharedPreferences("ALBUM",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("NEW","0");
            editor.commit();
            s.update(4, currentLevelOfEndingshot, 2);
        }
        else {  // sig가 0 이면 변경 사항 없음
            endingBook.setImageResource(R.drawable.endingbook);
        }

        endingBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메인페스트 잊지 않기
                //20.08.24_yeaji

                sfxService.SfxPlay(1);


                Intent endingbook = new Intent(h1_StartMain.this,EndingBook.class);
                startActivity(endingbook);
                overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                finish();
            }
        });
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
    class StartListenerClass implements View.OnClickListener {
        public void onClick(View view) {
            sfxService.SfxPlay(3);
            if (d.getTuto()==0) { // 튜토리얼이 처음이라면
                Intent intent = new Intent(h1_StartMain.this, StoryActivity.class); // 튜토리얼 화면으로
                startActivity(intent);
                //20.08.24_yeaji
                overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                finish();
            } else {
                Intent intent = new Intent(h1_StartMain.this, h2_TreeActivityMain.class);
                startActivity(intent);
                //20.08.24_yeaji
                overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                finish();
            }
        }
    }
    class SettingListenerClass implements View.OnClickListener {
        public void onClick(View view) {
            sfxService.SfxPlay(0);
            if (isSetting) {
                //화면 없애는 부분
                settingLayout.setVisibility(View.GONE);
                start.setClickable(true);
                exitButton.setClickable(true);
                endingBook.setClickable(true);
                isSetting = false;
            } else {
                isSetting = true;
                start.setClickable(false);
                exitButton.setClickable(false);
                endingBook.setClickable(false);

                settingLayout.setVisibility(View.VISIBLE);
            }
        }
    }
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
    protected void onPause() {
        super.onPause();
        stopService(new Intent(h1_StartMain.this, MusicService.class));
        d.setMusicOn(false);
    }
    public boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
        //android.permission.CAMERA
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /*
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {

            }*/
            requestPermissions(new String[] {PERMISSION_CAMERA, PERMISSION_RECORD}, PERMISSIONS_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(
            final int requestCode, final String[] permissions, final int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (allPermissionsGranted(grantResults)) {
                //setFragment();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(h1_StartMain.this, R.style.MyAlertDialogStyle);
                builder.setMessage("카메라 권한을 거부하시면 서비스를 이용할 수 없습니다.\n\n권한 설정 방법\n[설정 -> 애플리케이션 -> Kevin의 영어공부 -> 권한 -> 카메라 ON]");
                builder.setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                finishAffinity();
                                System.runFinalization();
                                System.exit(0);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
    private static boolean allPermissionsGranted(final int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}