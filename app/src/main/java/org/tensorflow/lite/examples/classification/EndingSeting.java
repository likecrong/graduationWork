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



    setDB s; // 디비
    DataSet d; //소리및이미지

    int[] state;

    int width, height;
    LayoutInflater inflater;

    Animation anima;
    Animation anima2;
    Animation hyperspaceJump;

    int sayCount = 0;

    String[] endingList = {}; // 대화내용

    // 우주
    String[] strEnding1 = {"@@, 그동안 잘 지냈어?", "벌써 시간이 이렇게 흘렀네.", "같이 영어공부 하면서 여행했던 게 얼마 전 같은데...",
            "그동안 나는 승무원이 되어 많은 나라들을 여행했어.", "세계에는 아름다운 풍경들이 정말 많더라!", "너와 같이 봤다면 더 좋았을텐데...",
            "그러다 문득 더 높은 하늘로 올라가보고 싶어졌어.", "그래서 매일 공부했더니 우주비행사가 된 거 있지!", "@@, 네가 응원해준 덕이야.",
            "이번에도 나를 응원해줄래?", "우주에 잘 도착하면 사진을 보내줄게, 안녕!"};
    // 승무원
    String[] strEnding2 = {"@@! 여기야 여기~!", "나 어때? 유니폼 입어봤는데 어울려?", "이제야 승무원이 된 게 실감이 나는 것 같아.",
            "우와 진짜 떨린다...", "조금 있으면 첫 비행을 하러 가야해.", "그 전에 너에게 고맙다고 인사하려고 왔어. @@! 나랑 함께 공부해줘서 고마워!",
            "앞으로도 나는 영어공부를 계속 할 거야.", "새로운 꿈이 생겼거든~!", "지금은 비밀이구, 음... 다음에 다시 만나면 알려줄게!",
            "그러니까 다시 만날 때까지 열심히 공부하는거야, 약속!", "나 이제 가볼게, 내 첫 비행 지켜봐줘.", "그동안 건강하게 지내야해, 안녕!"};
    // 농부
    String[] strEnding3 = {"@@, 안녕!", "우리 함께 여행하면서 추억을 많이 쌓았네.", "나는 다시 농사를 지으며 살기로 결심했어.",
            "하지만 앞으로도 영어공부를 게을리하지 않고 살아갈거야.", "다 @@, 네가 영어공부의 즐거움을 알려주었기 때문이야!",
            "지금까지 나와 함께 공부해줘서 고마워.", "다음에 놀러오면 내가 맛있는 식사를 대접할게.", "내가 직접 기른 채소로 말이야!",
            "고슴도치의 농사 실력은 최고라구!", "그러니까 꼭 놀러와야 해, 안녕~!"};
    // 취준
    String[] strEnding4 = {"@@! 이번 면접 결과가 나왔어.", "열심히 공부했지만 결국엔 떨어졌어...", "그치만 이대로 포기하면 아쉽잖아?",
            "고슴도치의 자존심이 있지!", "지금까지 나와 함께 공부해줘서 고마워.", "나는 조금 더 노력해서 꼭 승무원이 될게!!",
            "그때까지 잘 지내, @@!"};

    public void move() {
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
        anima = AnimationUtils.loadAnimation(c, R.anim.fadein_for_ending);
        background.startAnimation(anima);

        if (result.equals("1")) {

            i.setImageResource(R.drawable.kevin_space);
            //1. 2초 정면
            //2. 3초 걸어가기
            //3. 2초 정면
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //1번 우주선
                    float tmpX = width / 2; //가운데로
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
        } else if (result.equals("2")) { // 승무원

            i.setImageResource(R.drawable.kevin_stewardess);
            //1. 2초 정면
            //2. 3초 걸어가기
            //3. 2초 정면
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //1번 우주선
                    float tmpX = width / 3; //가운데로
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
                public void run() { //대사치기
                    showCon(strEnding3);
                    creditLocation = right;
                }
            }, 3000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() { //대사치기
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
                } else { // 대화가 끝났을 때
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


                    // 200828 크레딧
                    credit.setVisibility(View.VISIBLE);
                    creditLocation.setVisibility(View.VISIBLE);
                    anima2 = AnimationUtils.loadAnimation(c, R.anim.credit_translate);
                    creditLocation.startAnimation(anima2);


                    new Handler().postDelayed(new Runnable() {// 8초 버티기
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
