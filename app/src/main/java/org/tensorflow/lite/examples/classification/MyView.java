package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    Paint paint = new Paint();
    Paint paint2 = new Paint();
    Path path  = new Path();    // 자취를 저장할 객체
    Path path2  = new Path();

    boolean isDraw = true;


    public MyView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint.setStrokeWidth(10f); // 선의 굵기 지정
        paint2.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint2.setStrokeWidth(40f); // 선의 굵기 지정
        paint2.setColor(Color.RED);
    }

    public MyView(Context context, AttributeSet attrs){
        super(context, attrs);
        paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint.setStrokeWidth(10f); // 선의 굵기 지정
        paint2.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint2.setStrokeWidth(40f); // 선의 굵기 지정
        paint2.setColor(Color.RED);
    }

    public MyView(Context context) {
        super(context);
        paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint.setStrokeWidth(10f); // 선의 굵기 지정
        paint2.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint2.setStrokeWidth(40f); // 선의 굵기 지정
        paint2.setColor(Color.RED);
    }
    @Override
    protected void onDraw(Canvas canvas) { // 화면을 그려주는 메서드
        canvas.drawPath(path, paint); // 저장된 path 를 그려라
        canvas.drawPath(path2, paint2); // 저장된 path 를 그려라
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                path.moveTo(x, y); // 자취에 그리지 말고 위치만 이동해라
                break;
            case MotionEvent.ACTION_MOVE :
                path.lineTo(x, y); // 자취에 선을 그려라
                break;
            case MotionEvent.ACTION_UP :
                break;
        }

        invalidate(); // 화면을 다시그려라

        return true;

    }
}