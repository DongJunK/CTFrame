package com.example.adefault;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.example.adefault.Adapters.FullSizeAdapter;

import java.util.ArrayList;

public class Activity_Fullscreen extends AppCompatActivity {
    ViewPager viewPager;
    //수정 String[] images;
    ArrayList<String> images;
    int position;

    FullSizeAdapter fullSizeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        //세로 화면 고정
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(savedInstanceState==null)
        {
            Intent i = getIntent();
            images = i.getStringArrayListExtra("IMAGES");
            //images = i.getStringArrayExtra("IMAGES");
            position = i.getIntExtra("POSITION",0);
        }

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        fullSizeAdapter = new FullSizeAdapter(this,images);
        viewPager.setAdapter(fullSizeAdapter);
        viewPager.setCurrentItem(position,true);
    }


    //줌인 줌 아웃 기능 막음
    @Override
    public boolean onTouchEvent(MotionEvent ev){

        try{
            return super.onTouchEvent(ev);
        }catch (IllegalArgumentException e){

        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return true;
    }


}
