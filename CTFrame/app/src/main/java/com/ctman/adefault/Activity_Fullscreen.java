package com.ctman.adefault;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ctman.adefault.Adapters.FullSizeAdapter;
import com.ctman.adefault.Interfaces.SendDataToServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Fullscreen extends AppCompatActivity {
    ViewPager viewPager;
    //수정 String[] images;
    ArrayList<Frag4.pic_info> images;
    int position;
    String email, url, tag;

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

        /**** 저장 버튼 눌렀을때 동작 ****/
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("check", "버튼 눌림");
                email = MainActivity.loginId;

                Log.i("check", email);
                url = images.get(position).get_url();
                tag = images.get(position).get_tag();

                Log.i("저장 URL check", url);
                Log.i("저장 TAG check", tag);

                //다운로드 php함수 호출
                DownLoadPixa(v);

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

    public void DownLoadPixa(View v)
    {
        //section 0 여기 건들지마
        JSONObject obj = new JSONObject();
        SendDataToServer sendDataToServer = new SendDataToServer();
        //section 0 여기 건들지마

        //section 2 여기는 고치치마라//
        JSONObject post_dict = new JSONObject();
        //section 2 여기 까지//

        //section 3 보내야 하는 값 만큼 매치시켜줘서 보내면됨//
        try {
            post_dict.put("email" , email);
            post_dict.put("url" , url);
            post_dict.put("tag" , tag);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //section 3 여기까지//

        if (post_dict.length() > 0) {
            try
            {
                //section 4   "signUpCheck 라고 되어있는 부분을 승배가 준 파일로 고쳐서 보낼것 //
                obj = new JSONObject(sendDataToServer.execute(String.valueOf(post_dict),"upload_pixa_pic").get());
                //section 4//
            }
            catch (Exception e)
            {
                Log.i("Exception",e.toString());
            }
        }
    }

}
