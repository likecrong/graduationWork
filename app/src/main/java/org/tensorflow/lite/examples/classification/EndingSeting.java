package org.tensorflow.lite.examples.classification;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EndingSeting {

    public EndingSeting(Context c, String result, ImageView i, LinearLayout background, RelativeLayout say,
                        LinearLayout credit, Button ok, TextView conv, RelativeLayout left, RelativeLayout right, RelativeLayout creditLocation, String username) {
        this.c = c;
        this.result = result;
        this.i = i;
        this.background = background;
        this.say = say;

        this.credit = credit;
        this.ok = ok;
        this.conv = conv;
        this.left = left;
        this.right = right;
        this.creditLocation = creditLocation;

        this.username = username;
    }


    public EndingSeting(Context c, String result, ImageView i, LinearLayout background, RelativeLayout say,
                        LinearLayout credit, Button ok, TextView conv, RelativeLayout left, RelativeLayout right, RelativeLayout creditLocation, String username, LinearLayout pic_vegis) {
        this.c = c;
        this.result = result;
        this.i = i;
        this.background = background;
        this.say = say;

        this.credit = credit;
        this.ok = ok;
        this.conv = conv;
        this.left = left;
        this.right = right;
        this.creditLocation = creditLocation;

        this.username = username;
        this.pic_vegis = pic_vegis;
    }
    public EndingSeting(Context c, String result, LinearLayout background, RelativeLayout say,
                        LinearLayout credit, Button ok, TextView conv, RelativeLayout left, RelativeLayout right, RelativeLayout creditLocation, String username) {
        this.c = c;
        this.result = result;
        this.background = background;
        this.say = say;
        this.credit = credit;

        this.ok = ok;
        this.conv = conv;
        this.left = left;
        this.right = right;
        this.creditLocation = creditLocation;

        this.username = username;

    }

    Context c;
    String result;
    ImageView i;
    LinearLayout background;
    LinearLayout pic_vegis;
    RelativeLayout say;
    LinearLayout credit;
    Button ok;
    TextView conv;
    RelativeLayout left;
    RelativeLayout right;
    RelativeLayout creditLocation;
    String username;



    setDB s; // ??????
    DataSet d; //??????????????????

    int[] state;

    int width, height;
    LayoutInflater inflater;

    Animation anima;
    Animation anima2;
    Animation hyperspaceJump;

    int sayCount = 0;

    String[] endingList = {}; // ????????????

    // ??????
    String[] strEnding1 = {"@@, ????????? ??? ??????????", "?????? ????????? ????????? ?????????.", "?????? ???????????? ????????? ???????????? ??? ?????? ??? ?????????...",
            "????????? ?????? ???????????? ?????? ?????? ???????????? ????????????.", "???????????? ???????????? ???????????? ?????? ?????????!", "?????? ?????? ????????? ??? ???????????????...",
            "????????? ?????? ??? ?????? ????????? ??????????????? ????????????.", "????????? ?????? ??????????????? ?????????????????? ??? ??? ??????!", "@@, ?????? ???????????? ?????????.",
            "???????????? ?????? ????????????????", "????????? ??? ???????????? ????????? ????????????, ??????!"};
    // ?????????
    String[] strEnding2 = {"@@! ????????? ??????~!", "??? ??????? ????????? ??????????????? ??????????", "????????? ???????????? ??? ??? ????????? ?????? ??? ??????.",
            "?????? ?????? ?????????...", "?????? ????????? ??? ????????? ?????? ?????????.", "??? ?????? ????????? ???????????? ??????????????? ??????. @@! ?????? ?????? ??????????????? ?????????!",
            "???????????? ?????? ??????????????? ?????? ??? ??????.", "????????? ?????? ????????????~!", "????????? ????????????, ???... ????????? ?????? ????????? ????????????!",
            "???????????? ?????? ?????? ????????? ????????? ??????????????????, ??????!", "??? ?????? ?????????, ??? ??? ?????? ????????????.", "????????? ???????????? ????????????, ??????!"};
    // ??????
    String[] strEnding3 = {"@@, ??????!", "?????? ?????? ??????????????? ????????? ?????? ?????????.", "?????? ?????? ????????? ????????? ????????? ????????????.",
            "????????? ???????????? ??????????????? ??????????????? ?????? ???????????????.", "??? @@, ?????? ??????????????? ???????????? ??????????????? ????????????!",
            "???????????? ?????? ?????? ??????????????? ?????????.", "????????? ???????????? ?????? ????????? ????????? ????????????.", "?????? ?????? ?????? ????????? ?????????!",
            "??????????????? ?????? ????????? ????????????!", "???????????? ??? ???????????? ???, ??????~!"};
    // ??????
    String[] strEnding4 = {"@@! ?????? ?????? ????????? ?????????.", "????????? ??????????????? ????????? ????????????...", "????????? ????????? ???????????? ?????????????",
            "??????????????? ???????????? ??????!", "???????????? ?????? ?????? ??????????????? ?????????.", "?????? ?????? ??? ???????????? ??? ???????????? ??????!!",
            "???????????? ??? ??????, @@!"};

    public void move() {
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
        anima = AnimationUtils.loadAnimation(c, R.anim.fadein_for_ending);
        background.startAnimation(anima);

        if (result.equals("1")) {

            i.setImageResource(R.drawable.kevin_space);
            //1. 2??? ??????
            //2. 3??? ????????????
            //3. 2??? ??????
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //1??? ?????????
                    float tmpX = width / 2; //????????????
                    ObjectAnimator animX = ObjectAnimator.ofFloat(i, "x", tmpX);
                    ObjectAnimator animY = ObjectAnimator.ofFloat(i, "y", i.getY());
                    AnimatorSet animSetXY = new AnimatorSet();
                    animSetXY.playTogether(animX, animY);
                    animSetXY.setDuration(2000);
                    i.setImageResource(R.drawable.kevin_space_side);
                    animSetXY.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            i.setImageResource(R.drawable.kevin_space);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showCon(strEnding1);
                                    creditLocation = left;
                                }
                            }, 2000);
                        }
                    }, 2000);
                }
            }, 2000);
        } else if (result.equals("2")) { // ?????????

            i.setImageResource(R.drawable.kevin_stewardess);
            //1. 2??? ??????
            //2. 3??? ????????????
            //3. 2??? ??????
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //1??? ?????????
                    float tmpX = width / 3; //????????????
                    ObjectAnimator animX = ObjectAnimator.ofFloat(i, "x", tmpX);
                    ObjectAnimator animY = ObjectAnimator.ofFloat(i, "y", i.getY());
                    AnimatorSet animSetXY = new AnimatorSet();
                    animSetXY.playTogether(animX, animY);
                    animSetXY.setDuration(2000);
                    i.setImageResource(R.drawable.kevin_stewardess_side);
                    animSetXY.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            i.setImageResource(R.drawable.kevin_stewardess);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showCon(strEnding2);
                                    creditLocation = left;
                                }
                            }, 2000);
                        }
                    }, 2000);
                }
            }, 2000);

        } else if (result.equals("3")) {

            pic_vegis.setVisibility(View.VISIBLE);
            i.setImageResource(R.drawable.kevin_farm);
            i.setVisibility(View.INVISIBLE);

            hyperspaceJump = AnimationUtils.loadAnimation(c, R.anim.max);
            i.startAnimation(hyperspaceJump);
            i.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() { //????????????
                    showCon(strEnding3);
                    creditLocation = right;
                }
            }, 3000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() { //????????????
                    showCon(strEnding4);
                    creditLocation = right;
                  }
            }, 3000);
        }
    }

    public void showCon(String[] endingStr) {

        endingList = endingStr;
        conv.setText(endingList[sayCount].replace("@@", username));

        say.setVisibility(View.VISIBLE);
        ok.setVisibility(View.VISIBLE);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sayCount++;
                if (sayCount < endingList.length) {
                    conv.setText(endingList[sayCount].replace("@@", username));
                } else { // ????????? ????????? ???
                    say.setVisibility(View.GONE);
                    ok.setVisibility(View.GONE);

                    if (result.equals("1")) {
                        i.setVisibility(View.INVISIBLE);
                        background.setBackgroundResource(R.drawable.e1_2);
                    } else if (result.equals("2")) {
                        i.setVisibility(View.INVISIBLE);
                        background.setBackgroundResource(R.drawable.e2_2);
                    } else if (result.equals("3")) {
                        i.setVisibility(View.INVISIBLE);
                        pic_vegis.setVisibility(View.INVISIBLE);
                        hyperspaceJump.setFillAfter(false);
                        background.setBackgroundResource(R.drawable.e3_2);
                    } else if (result.equals("4")) {
                        background.setBackgroundResource(R.drawable.e4_2);
                    }

                    background.startAnimation(anima);


                    // 200828 ?????????
                    credit.setVisibility(View.VISIBLE);
                    creditLocation.setVisibility(View.VISIBLE);
                    anima2 = AnimationUtils.loadAnimation(c, R.anim.credit_translate);
                    creditLocation.startAnimation(anima2);


                    new Handler().postDelayed(new Runnable() {// 8??? ?????????
                        @Override
                        public void run() {
                            credit.setVisibility(View.INVISIBLE);
                            creditLocation.setVisibility(View.INVISIBLE);

                        }
                    }, anima2.getDuration());
                }
            }
        });
    }

}
