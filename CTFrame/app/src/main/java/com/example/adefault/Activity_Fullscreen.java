package com.example.adefault;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.example.adefault.Adapters.FullSizeAdapter;

public class Activity_Fullscreen extends AppCompatActivity {
    ViewPager viewPager;
    String[] images;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        if(savedInstanceState==null)
        {
            Intent i = getIntent();
            images = i.getStringArrayExtra("IMAGES");
            position = i.getIntExtra("POSITION",0);
        }

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        FullSizeAdapter fullSizeAdapter = new FullSizeAdapter(this,images);
        viewPager.setAdapter(fullSizeAdapter);
        viewPager.setCurrentItem(position,true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){

        try{
            return super.onTouchEvent(ev);
        }catch (IllegalArgumentException e){
            return true;
        }
    }
}
