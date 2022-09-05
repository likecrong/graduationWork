package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class DataSet extends AppCompatActivity {

    ArrayList<Integer> imageList = new ArrayList<>();
    Button exit;
    Context c;

    Boolean isMusicOn = true; //셋팅

    setDB s;

    int countAdd = 0;

    int backMusic, sfx;
    int tuto;//튜토리얼


    public DataSet(Context c) {
        this.c = c;
    }

    public DataSet(ArrayList<Integer> imageList) {
        this.imageList = imageList;
    }

    public DataSet(Context c, ArrayList<Integer> imageList) {
        this.c = c;
        this.imageList = imageList;
    }

    public DataSet(Context c, Button exit) {
        this.c = c;
        this.exit = exit;
    }

    public DataSet(Context c, ArrayList<Integer> imageList, Button exit) {
        this.imageList = imageList;
        this.exit = exit;
        this.c = c;
    }
    public int getTuto() {
        return tuto;
    }

    public void setTuto(int tuto) {
        this.tuto = tuto;
    }

    public Boolean getMusicOn() {
        return isMusicOn;
    }

    public void setMusicOn(Boolean musicOn) {
        isMusicOn = musicOn;
    }

    public int getBackMusic() {
        return backMusic;
    }

    public void setBackMusic(int backMusic) {
        this.backMusic = backMusic;
    }

    public void setSfx(int sfx) {
        this.sfx = sfx;
    }

    public void setExit() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callExit();
            }
        });
    }

    public void callExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(c, R.style.MyAlertDialogStyle);
        builder.setMessage("케빈의 꿈을 종료할까요?");
        builder.setCancelable(false)
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        finapp();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void finapp() {
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }


    public void initializeData(String level) {
        if (countAdd == 0) {
            if (level.equals("st1")) {
                imageList.add(R.drawable.st1_mouse);
                imageList.add(R.drawable.st1_wallet);
                imageList.add(R.drawable.st1_clock);
                imageList.add(R.drawable.st1_pen);
                imageList.add(R.drawable.st1_tissue);

                imageList.add(R.drawable.st1_vase);
                imageList.add(R.drawable.st1_shoe);
                imageList.add(R.drawable.st1_pillow);
                imageList.add(R.drawable.st1_chair);
                imageList.add(R.drawable.st1_eraser);

                 //wallet
            } else if (level.equals("st2")) {
                imageList.add(R.drawable.st2_bowl);
                imageList.add(R.drawable.st2_coffeepot);
                imageList.add(R.drawable.st2_cup);
                imageList.add(R.drawable.st2_fryingpan);
                imageList.add(R.drawable.st2_ladle);

                imageList.add(R.drawable.st2_plate);
                imageList.add(R.drawable.st2_refrigerator);
                imageList.add(R.drawable.st2_spatula);
                imageList.add(R.drawable.st2_toaster);
                imageList.add(R.drawable.st2_wok);
            } else if (level.equals("st3")) {
                imageList.add(R.drawable.st3_banana);
                imageList.add(R.drawable.st3_brocoli);
                imageList.add(R.drawable.st3_crab);
                imageList.add(R.drawable.st3_cucumber);
                imageList.add(R.drawable.st3_lemon);

                imageList.add(R.drawable.st3_orange);
                imageList.add(R.drawable.st3_pineapple);
                imageList.add(R.drawable.st3_pizza);
                imageList.add(R.drawable.st3_shoppingcart);
                imageList.add(R.drawable.st3_strawberry);
            } else if (level.equals("st4")) {
                imageList.add(R.drawable.st4_baloon);
                imageList.add(R.drawable.st4_bench);
                imageList.add(R.drawable.st4_bus);
                imageList.add(R.drawable.st4_butterfly);
                imageList.add(R.drawable.st4_cat);

                imageList.add(R.drawable.st4_dog);
                imageList.add(R.drawable.st4_dragonfly);
                imageList.add(R.drawable.st4_streetsign);
                imageList.add(R.drawable.st4_swing);
                imageList.add(R.drawable.st4_trafficlight);
            } else if (level.equals("st5")) {
                imageList.add(R.drawable.st5_soccer);
                imageList.add(R.drawable.st5_basketball);
                imageList.add(R.drawable.st5_golf);
                imageList.add(R.drawable.st5_rugby);
                imageList.add(R.drawable.st5_volleyball);

                imageList.add(R.drawable.st5_tennis);
                imageList.add(R.drawable.st5_racket);
                imageList.add(R.drawable.st5_swimcap);
                imageList.add(R.drawable.st5_whistle);
                imageList.add(R.drawable.st5_scoreboard);
            } else if (level.equals("b")) {
                imageList.add(R.drawable.bonus_piano);
                imageList.add(R.drawable.bonus_guitar);
                imageList.add(R.drawable.bonus_violin);
                imageList.add(R.drawable.bonus_drum);
                imageList.add(R.drawable.bonus_flute);

                imageList.add(R.drawable.bonus_ocarina);
                imageList.add(R.drawable.bonus_harmonica);
                imageList.add(R.drawable.bonus_harp);
                imageList.add(R.drawable.bonus_accordion);
                imageList.add(R.drawable.bonus_trombone);
            } else if (level.equals("t")) {
                imageList.add(R.drawable.t_sock1);
                imageList.add(R.drawable.t_sock2);
            }
            countAdd = 1;
        }
    }
}
