package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SfxService {
    SoundPool sfx;
    int sfx_click, sfx_ching, sfx_beep, sfx_startHigh, sfx_startLow, sfx_medalbuildup, sfx_medal, sfx_vibraslap, sfx_applause, sfx_clear, sfx_bbangbbang, sfx_bbock, sfx_eraser, sfx_fail;
    int sfx_voice, clear, follow;
    Context context;

    float volume = 1.0f;
    SfxService (Context con){
        this.context = con;
    }


    void SfxLoad() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sfx = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(10).build();

        sfx_click = sfx.load(this.context, R.raw.sfx_click, 1);
        sfx_ching = sfx.load(this.context, R.raw.sfx_ching, 1);
        sfx_beep = sfx.load(this.context, R.raw.sfx_beep, 1);
        sfx_startHigh = sfx.load(this.context, R.raw.sfx_starthigh, 1);
        sfx_startLow = sfx.load(this.context, R.raw.sfx_startlow, 1);
        sfx_medalbuildup = sfx.load(this.context, R.raw.sfx_medalbuildup, 1);
        sfx_medal = sfx.load(this.context, R.raw.sfx_medal, 1);
        sfx_vibraslap = sfx.load(this.context, R.raw.sfx_vibraslap, 1);
        sfx_applause = sfx.load(this.context, R.raw.sfx_applause, 1);
        sfx_clear = sfx.load(this.context, R.raw.sfx_clear, 1);
        sfx_bbangbbang = sfx.load(this.context, R.raw.sfx_bbangbbang, 1);
        sfx_bbock = sfx.load(this.context, R.raw.sfx_bbock, 1);
        sfx_eraser = sfx.load(this.context, R.raw.sfx_eraser, 1);
        sfx_fail = sfx.load(this.context, R.raw.sfx_fail, 1);
    }
    void VoiceLoad(String level, int index) {
        clear = sfx.load(this.context, R.raw.clear, 1);//sock 음성 넣기
        follow = sfx.load(this.context, R.raw.follow, 1);//sock 음성 넣기
        switch (level){
            case "t":
                sfx_voice = sfx.load(this.context, R.raw.t_1, 1);//sock 음성 넣기
                break;
            case "st1":
                switch (index){
                    case 1:
                        sfx_voice = sfx.load(this.context, R.raw.st1_1, 1);
                        break;
                    case 2:
                        sfx_voice = sfx.load(this.context, R.raw.st1_2, 1);
                        break;
                    case 3:
                        sfx_voice = sfx.load(this.context, R.raw.st1_3, 1);
                        break;
                    case 4:
                        sfx_voice = sfx.load(this.context, R.raw.st1_4, 1);
                        break;
                    case 5:
                        sfx_voice = sfx.load(this.context, R.raw.st1_5, 1);
                        break;
                    case 6:
                        sfx_voice = sfx.load(this.context, R.raw.st1_6, 1);
                        break;
                    case 7:
                        sfx_voice = sfx.load(this.context, R.raw.st1_7, 1);
                        break;
                    case 8:
                        sfx_voice = sfx.load(this.context, R.raw.st1_8, 1);
                        break;
                    case 9:
                        sfx_voice = sfx.load(this.context, R.raw.st1_9, 1);
                        break;
                    case 10:
                        sfx_voice = sfx.load(this.context, R.raw.st1_10, 1);
                        break;
                }

                break;
            case "st2":
                switch (index){
                    case 1:
                        sfx_voice = sfx.load(this.context, R.raw.st2_1, 1);
                        break;
                    case 2:
                        sfx_voice = sfx.load(this.context, R.raw.st2_2, 1);
                        break;
                    case 3:
                        sfx_voice = sfx.load(this.context, R.raw.st2_3, 1);
                        break;
                    case 4:
                        sfx_voice = sfx.load(this.context, R.raw.st2_4, 1);
                        break;
                    case 5:
                        sfx_voice = sfx.load(this.context, R.raw.st2_5, 1);
                        break;
                    case 6:
                        sfx_voice = sfx.load(this.context, R.raw.st2_6, 1);
                        break;
                    case 7:
                        sfx_voice = sfx.load(this.context, R.raw.st2_7, 1);
                        break;
                    case 8:
                        sfx_voice = sfx.load(this.context, R.raw.st2_8, 1);
                        break;
                    case 9:
                        sfx_voice = sfx.load(this.context, R.raw.st2_9, 1);
                        break;
                    case 10:
                        sfx_voice = sfx.load(this.context, R.raw.st2_10, 1);
                        break;
                }

                break;
            case "st3":
                switch (index){
                    case 1:
                        sfx_voice = sfx.load(this.context, R.raw.st3_1, 1);
                        break;
                    case 2:
                        sfx_voice = sfx.load(this.context, R.raw.st3_2, 1);
                        break;
                    case 3:
                        sfx_voice = sfx.load(this.context, R.raw.st3_3, 1);
                        break;
                    case 4:
                        sfx_voice = sfx.load(this.context, R.raw.st3_4, 1);
                        break;
                    case 5:
                        sfx_voice = sfx.load(this.context, R.raw.st3_5, 1);
                        break;
                    case 6:
                        sfx_voice = sfx.load(this.context, R.raw.st3_6, 1);
                        break;
                    case 7:
                        sfx_voice = sfx.load(this.context, R.raw.st3_7, 1);
                        break;
                    case 8:
                        sfx_voice = sfx.load(this.context, R.raw.st3_8, 1);
                        break;
                    case 9:
                        sfx_voice = sfx.load(this.context, R.raw.st3_9, 1);
                        break;
                    case 10:
                        sfx_voice = sfx.load(this.context, R.raw.st3_10, 1);
                        break;
                }

                break;
            case "st4":
                switch (index){
                    case 1:
                        sfx_voice = sfx.load(this.context, R.raw.st4_1, 1);
                        break;
                    case 2:
                        sfx_voice = sfx.load(this.context, R.raw.st4_2, 1);
                        break;
                    case 3:
                        sfx_voice = sfx.load(this.context, R.raw.st4_3, 1);
                        break;
                    case 4:
                        sfx_voice = sfx.load(this.context, R.raw.st4_4, 1);
                        break;
                    case 5:
                        sfx_voice = sfx.load(this.context, R.raw.st4_5, 1);
                        break;
                    case 6:
                        sfx_voice = sfx.load(this.context, R.raw.st4_6, 1);
                        break;
                    case 7:
                        sfx_voice = sfx.load(this.context, R.raw.st4_7, 1);
                        break;
                    case 8:
                        sfx_voice = sfx.load(this.context, R.raw.st4_8, 1);
                        break;
                    case 9:
                        sfx_voice = sfx.load(this.context, R.raw.st4_9, 1);
                        break;
                    case 10:
                        sfx_voice = sfx.load(this.context, R.raw.st4_10, 1);
                        break;
                }

                break;
            case "st5":
                switch (index){
                    case 1:
                        sfx_voice = sfx.load(this.context, R.raw.st5_1, 1);
                        break;
                    case 2:
                        sfx_voice = sfx.load(this.context, R.raw.st5_2, 1);
                        break;
                    case 3:
                        sfx_voice = sfx.load(this.context, R.raw.st5_3, 1);
                        break;
                    case 4:
                        sfx_voice = sfx.load(this.context, R.raw.st5_4, 1);
                        break;
                    case 5:
                        sfx_voice = sfx.load(this.context, R.raw.st5_5, 1);
                        break;
                    case 6:
                        sfx_voice = sfx.load(this.context, R.raw.st5_6, 1);
                        break;
                    case 7:
                        sfx_voice = sfx.load(this.context, R.raw.st5_7, 1);
                        break;
                    case 8:
                        sfx_voice = sfx.load(this.context, R.raw.st5_8, 1);
                        break;
                    case 9:
                        sfx_voice = sfx.load(this.context, R.raw.st5_9, 1);
                        break;
                    case 10:
                        sfx_voice = sfx.load(this.context, R.raw.st5_10, 1);
                        break;
                }

                break;
            case "b":
                switch (index){
                    case 1:
                        sfx_voice = sfx.load(this.context, R.raw.st6_1, 1);
                        break;
                    case 2:
                        sfx_voice = sfx.load(this.context, R.raw.st6_2, 1);
                        break;
                    case 3:
                        sfx_voice = sfx.load(this.context, R.raw.st6_3, 1);
                        break;
                    case 4:
                        sfx_voice = sfx.load(this.context, R.raw.st6_4, 1);
                        break;
                    case 5:
                        sfx_voice = sfx.load(this.context, R.raw.st6_5, 1);
                        break;
                    case 6:
                        sfx_voice = sfx.load(this.context, R.raw.st6_6, 1);
                        break;
                    case 7:
                        sfx_voice = sfx.load(this.context, R.raw.st6_7, 1);
                        break;
                    case 8:
                        sfx_voice = sfx.load(this.context, R.raw.st6_8, 1);
                        break;
                    case 9:
                        sfx_voice = sfx.load(this.context, R.raw.st6_9, 1);
                        break;
                    case 10:
                        sfx_voice = sfx.load(this.context, R.raw.st6_10, 1);
                        break;
                }

                break;
        }
    }
    void SfxPlay(int i) {
        switch (i){
            case 0:
                sfx.play(sfx_click, volume, volume, 0, 0, 1f);
                break;
            case 1:
                sfx.play(sfx_ching, volume, volume, 0, 0, 1f);
                break;
            case 2:
                sfx.play(sfx_beep, volume, volume, 0, 0, 1f);
                break;
            case 3:
                sfx.play(sfx_startHigh, volume, volume, 0, 0, 1f);
                break;
            case 4:
                sfx.play(sfx_startLow, volume, volume, 0, 0, 1f);
                break;
            case 5:
                sfx.play(sfx_medalbuildup, volume, volume, 0, 0, 1f);
                break;
            case 6:
                sfx.play(sfx_medal, volume, volume, 0, 0, 1f);
                break;
            case 7:
                sfx.play(sfx_vibraslap, volume, volume, 0, 0, 1f);
                break;
            case 8:
                sfx.play(sfx_applause, volume, volume, 0, 0, 1f);
                break;
            case 9:
                sfx.play(sfx_clear, volume, volume, 0, 0, 1f);
                break;
            case 10:
                sfx.play(sfx_bbangbbang, volume, volume, 0, 0, 1f);
                break;
            case 11:
                sfx.play(sfx_bbock, volume, volume, 0, 0, 1f);
                break;
            case 12:
                sfx.play(sfx_eraser, volume, volume, 0, 0, 1f);
                break;
            case 13:
                sfx.play(sfx_fail, volume, volume, 0, 0, 1f);
                break;
            case 100:
                sfx.play(sfx_voice, volume, volume, 0, 0, 1f);
                break;
            case 101:
                sfx.play(follow, volume, volume, 0, 0, 1f);//따라해보세요
                break;
            case 102:
                sfx.play(clear, volume, volume, 0, 0, 1f);//clear!
                break;
        }
    }
}