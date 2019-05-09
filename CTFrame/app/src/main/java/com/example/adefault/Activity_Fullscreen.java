package com.example.adefault;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //저장 버튼 눌렀을때 동작
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("check", "버튼 눌림");
                Snackbar.make(v, "서버 앨범에 저장 완료", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

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
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try{
            return super.dispatchTouchEvent(ev);
        }catch (Exception e){
            return false;
        }
    }


}
