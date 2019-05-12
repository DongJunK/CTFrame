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
    ArrayList<Frag4.pic_info> images;
    int position;

    FullSizeAdapter fullSizeAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final Intent i = getIntent();     //인텐트 받아 오기

        if(savedInstanceState==null)
        {

            /** intent 값을 serializableExtra 로 받아옴**/
            images = (ArrayList<Frag4.pic_info>) i.getSerializableExtra("PIC_INFO");
            //images = i.getStringArrayListExtra("IMAGES");
            //images = i.getStringArrayExtra("IMAGES");
            position = i.getIntExtra("POSITION",0);
        }

        //저장 버튼 눌렀을때 동작
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("check", "버튼 눌림");
                String url = images.get(position).get_url();
                String tag = images.get(position).get_tag();

                Log.i("저장 URL check", url);
                Log.i("저장 TAG check", tag);

                Snackbar.make(v, "서버 앨범에 저장 완료", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        //세로 화면 고정
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        viewPager = (ViewPager)findViewById(R.id.viewpager);

        fullSizeAdapter = new FullSizeAdapter(this,images);
        viewPager.setAdapter(fullSizeAdapter);
        viewPager.setCurrentItem(position,true);

        /******************************************************************************/
        /**스크롤 했을때 페이지 변경되는 것을 확인하고 position 값을 변경시키는 함수**/
        /******************************************************************************/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int first = position;
                int second = i;

                if(first!=second){
                    Log.i("check", " 변경전 " + position);
                    position = i;
                    Log.i("check", " 변경후 " + position);
                }
            }
            @Override
            public void onPageSelected(int i) {}

            @Override
            public void onPageScrollStateChanged(int i) {}
        });
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
