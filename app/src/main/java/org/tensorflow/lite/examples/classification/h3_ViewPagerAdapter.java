package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class h3_ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Integer> imageList;
    private int[] answerList;
    private Drawable drawable;


    public h3_ViewPagerAdapter(Context context, ArrayList<Integer> imageList, int[] answerList) {
        this.mContext = context;
        this.imageList = imageList;
        this.answerList = answerList;
        this.drawable = context.getResources().getDrawable(R.drawable.frame);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.h3_pagerview, null);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(imageList.get(position));


        if (answerList!=null && answerList[position] == 0) {
            imageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        }

        view.setBackgroundColor(Color.argb(0,255,255,255));
        container.addView(view);


        return view;
    }
    public LayerDrawable layer(){
        return null;
    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }


    public int calcPadding(){ // padding 계산하는 함수
        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        float width = dm.widthPixels;
        float height = dm.heightPixels;
        float h_weight = 0.7f; // height weight

        return (int)((width-height*h_weight)/2);
    }


    //리스트가 변경 될 때 무조건 notifyDataSetChanged 호출 해주자.
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
