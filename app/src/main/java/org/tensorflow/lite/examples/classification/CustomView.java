package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.graphics.Typeface;
public class CustomView extends View {


    String s = "";
    Boolean isShadow = false;
    int questionWidth = 0;
    int questionHeight = 0;
    int writeHeight = 0;
    public CustomView(Context context, String s, Boolean isShadow, int deviceWidth, int deviceHeight) {
        super(context);
        this.s = s;
        this.isShadow = isShadow;
        questionWidth = (int)(deviceWidth / 6) * 8;
        questionHeight = (int)(deviceHeight / 7) * 13;
        if(s.contains("b") || s.contains("d") || s.contains("f") || s.contains("h") || s.contains("i") || s.contains("k") ||s.contains("l")){
            writeHeight --;
        }
        if(s.contains("g") ||s.contains("j") ||s.contains("p") ||s.contains("q") ||s.contains("y")){
            writeHeight ++;
        }
    }

    public void onDraw(Canvas canvas) {

        if(isShadow){//가이드 음영 부분
            int j = 0;

            if(getWidth() <= getHeight()){//그럴 리 없겠지만 가로보다 세로 길이가 더 길 때
                //가로에 맞춰
                j = getWidth() / s.length();
                Log.d("asdfasdf", "1");
                Log.d("asdfasdf", "j : " + j);

            }
            else if(getWidth() / getHeight() > s.length()){//글자 수로 쪼갰을 때 가로가 더 길 때
                j = (int)(getHeight() * 0.8);
                Log.d("asdfasdf", "2");
                Log.d("asdfasdf", "j : " + j);
            }
            else{                               //글자 수로 쪼갰을 때 세로가 더 길 때
                j = (int)(getWidth()*1.1/s.length());
                Log.d("asdfasdf", "3");
                Log.d("asdfasdf", "j : " + j);
            }
            if(j > (int)(getHeight() * 0.8)){
                j = (int)(getHeight() * 0.8);
            }
            int x = getWidth() / 2 - s.length() * j / 2;
            int y = getHeight() / 2 - j / 2;


            canvas.drawColor(Color.WHITE);
            Paint MyPaint = new Paint();
            MyPaint.setStrokeWidth(8f);
            MyPaint.setStyle(Paint.Style.STROKE);
            MyPaint.setColor(Color.BLACK);
            MyPaint.setStyle(Paint.Style.FILL);
            MyPaint.setTextSize(j);
            MyPaint.setTextAlign(Paint.Align.CENTER);
            MyPaint.setTextScaleX(1.15f);
            MyPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
            if(writeHeight > 0){//gy 등
                canvas.drawText(s, (float)getWidth() / 2, y + (float)11 * j / 16, MyPaint);
            }else if(writeHeight < 0){//bdhk 등
                canvas.drawText(s, (float)getWidth() / 2, y + (float)13 * j / 16 - 10, MyPaint);
            }
            else{
                canvas.drawText(s, (float)getWidth() / 2, y + (float)3 * j / 4 - 10, MyPaint);
            }
        }
        else{//제시어 부분
            int j = 0;
            if(questionWidth <= questionHeight){//그럴 리 없겠지만 가로보다 세로 길이가 더 길 때
                //가로에 맞춰
                j = questionWidth / s.length();

            }
            else if(questionWidth / questionHeight > s.length()){//글자 수로 쪼갰을 때 가로가 더 길 때
                j = (int)(questionHeight * 0.8);
            }
            else{                               //글자 수로 쪼갰을 때 세로가 더 길 때
                j = (int)(questionWidth /s.length());
            }
            if(j > (int)(getHeight() * 0.8)){
                j = (int)(getHeight() * 0.8);
            }
            int x = getWidth() / 2 - s.length() * j / 2;
            int y = 0;

            String[] arr = s.split("");

            canvas.drawColor(Color.WHITE);
            Paint MyPaint = new Paint();
            for (int i = 0; i < s.length(); i++) {
                MyPaint.setStrokeWidth(5f);
                MyPaint.setStyle(Paint.Style.STROKE);
                MyPaint.setColor(Color.BLACK);

                Path path = new Path();
                path.moveTo(x + i * j, y);
                path.lineTo(x + i * j, y);
                path.lineTo(x + i * j, y + j);
                path.lineTo(x + (i + 1) * j, y + j);
                path.lineTo(x + (i + 1) * j, y);
                path.lineTo(x + i * j, y);
                canvas.drawPath(path, MyPaint);

                MyPaint.setStyle(Paint.Style.FILL);
                MyPaint.setTextSize(j);
                MyPaint.setTextAlign(Paint.Align.CENTER);
                MyPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));

                if(arr[0].equals("") == true) {
                    canvas.drawText(arr[i+1], x + j * 0.5f + i * j, y + j - 10, MyPaint);

                }
                else {
                    canvas.drawText(arr[i], x + j * 0.5f + i * j, y + j - 10, MyPaint);

                }
            }
        }
    }
}