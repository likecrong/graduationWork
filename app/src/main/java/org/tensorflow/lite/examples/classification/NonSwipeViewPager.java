package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NonSwipeViewPager extends ViewPager {
    public NonSwipeViewPager(Context context) {
        super(context);
    }

    public NonSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 스와이핑되서 페이지가 바뀌는것을 막는다.
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //스와이핑되서 페이가 바뀌는 것을 막는다.
        return false;
    }
}
