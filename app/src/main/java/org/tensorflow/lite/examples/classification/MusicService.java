package org.tensorflow.lite.examples.classification;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    MediaPlayer player;
    @Override
    public void onCreate() {
//        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // intent: startService() 호출 시 넘기는 intent 객체
        // flags: service start 요청에 대한 부가 정보. 0, START_FLAG_REDELIVERY, START_FLAG_RETRY
        // startId: start 요청을 나타내는 unique integer id
//        Log.d(TAG, "onStartCommand()");

        Integer param = intent.getIntExtra("index", 0);
        if(param == 1){
            player = MediaPlayer.create(this, R.raw.song1);
            player.setVolume(0.8f,0.8f);
        }
        else if(param == 2)player = MediaPlayer.create(this, R.raw.song2);//귀농 엔딩
        else if(param == 3)player = MediaPlayer.create(this, R.raw.song3);
        else if(param == 4)player = MediaPlayer.create(this, R.raw.song4);
        else if(param == 44)player = MediaPlayer.create(this, R.raw.song44);
        else if(param == 5)player = MediaPlayer.create(this, R.raw.song5);
        else if(param == 6)player = MediaPlayer.create(this, R.raw.song6);
        else if(param == 7)player = MediaPlayer.create(this, R.raw.song7);//우주비행사 엔딩
        else if(param == 8)player = MediaPlayer.create(this, R.raw.song8);//취준 엔딩
        else if(param == 9) {
            player = MediaPlayer.create(this, R.raw.rainysunday);//승무원 엔딩
            player.setVolume(0.8f,0.8f);
        } else {
            if (param == 10) player = MediaPlayer.create(this, R.raw.rainysunday);
            player.setVolume(0.8f,0.8f);
        }

        player.setLooping(true); // 반복재생 여부 설정
        player.start();

        return START_NOT_STICKY;
    }

    public void onDestroy() {

        player.stop();
        player.release();
        player = null;
    }

    // 아래 onBind 메소드가 없으면 어떻게 될까?
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
