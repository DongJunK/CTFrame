package com.ctman.adefault;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.ctman.adefault.Adapters.GalleryImageAdapter;
import com.ctman.adefault.Interfaces.IRecyclerViewClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Activity_Pixabay_Category extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    GalleryImageAdapter galleryImageAdapter;        //어뎁터

    String category;        //선택 카테고리
    public static Thread thread;
    public static  final int LOAD_SUCCESS = 101;        //성공값
    public String search_val = "";
    public int page_num = 1;        //검색 페이지
    public static boolean refresh_check = true;     //화면 갱신 중인지 아닌지 체크하는 플래그값

    ArrayList<String> img_url_list_result = new ArrayList<>();     //이미지 url를 저장할 array list
    ArrayList<String> img_tag_list = new ArrayList<>();     //이미지 tag를 저장할 array list
    ArrayList<String> img_user_list = new ArrayList<>();    //이미지 user를 저장할 array list
    ArrayList<String> img_like_list = new ArrayList<>();    //이미지 like를 저장할  array list

    final ArrayList<Frag4.pic_info> images = new ArrayList<>();

    private final Activity_Pixabay_Category.MyHandler mHandler = new Activity_Pixabay_Category.MyHandler(Activity_Pixabay_Category.this);

    //UI를 갱신 해주는 핸들러
    private static class MyHandler extends Handler {
        private final WeakReference<Activity_Pixabay_Category> weakReference;

        public MyHandler(Activity_Pixabay_Category mainactivity) {
            weakReference = new WeakReference<Activity_Pixabay_Category>(mainactivity);
        }

        @Override
        public void handleMessage(Message msg) {

            Activity_Pixabay_Category mainactivity = weakReference.get();

            if (mainactivity != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        mainactivity.galleryImageAdapter.notifyDataSetChanged();
                        //thread.interrupt();
                        //로그
                        //Log.i("check", "쓰레드 종료");
                        break;
                }
            }
        }
    }

    /**************************************************/
    /****** 픽사 베이 기본 사진 다운 받는 함수*********/
    /****** 파라미터 검색할 페이지 : page *********/
    /**************************************************/
    public ArrayList<String> default_pixa(int page){
        refresh_check = false;      //사용중이라고 표시하고
        ArrayList<String> img_url_list = new ArrayList<>();     //이미지 url를 저장할 array list

        String API_KEY = "10343836-4aa4b118c22502ac110971841";     // api key 값
        int Page_num = page;        //페이지 값
        String url = "https://pixabay.com/api/?key=" + API_KEY + "&category=" + category + "&page=" + Page_num + "&image_type=photo";      //검색 값
        //로그
        //Log.i("check", url);
        BufferedReader reader = null;       //결과값을 읽기 위한 버퍼리더
        StringBuffer tmp = null;        //결과값을 임시 저장할 스트링버퍼
        String result = null;       //결과값을 임시 저장할 string 변수

        try{
            URL ready_pixa = new URL(url);      //url로 준비
            HttpURLConnection connect_pixa = (HttpURLConnection)ready_pixa.openConnection();        //http로 연결
            connect_pixa.setRequestProperty("Content-Type", "application/json");        //json으로 요청
            connect_pixa.setDoOutput(true);
            connect_pixa.setRequestMethod("GET");       //get 방식으로

            //결과값 버퍼 리더에 넣기 (인코딩은 utf8)
            reader = new BufferedReader(new InputStreamReader(connect_pixa.getInputStream(), "UTF-8"));

            tmp = new StringBuffer();       //결과값을 임시 저장할 스트링버퍼

            String k;
            while((k = reader.readLine()) != null){
                tmp.append(k);
            }
            reader.close();

            result = tmp.toString();        //파싱 받은 결과를 string 값으로 저장

            JSONArray jsonArray = new JSONObject(result).getJSONArray("hits");

            //원하는 값을 arraylist에 넣는 for 문
            for(int i = 0 ; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String img_url = jsonObject.getString("webformatURL");      //이미지 url
                String img_tag = jsonObject.getString("tags");      //이미지 tag
                String img_user = jsonObject.getString("user");     //이미지 올린 사람
                String img_like = jsonObject.getString("likes");        //이미지 좋아요 횟수

                //array list에 저쟝
                img_url_list.add(img_url);
                img_tag_list.add(img_tag);
                img_user_list.add(img_user);
                img_like_list.add(img_like);

            }
        }
        catch (Exception e){
            //로그
            //Log.i("pixacheck", "default_pixa  예외처리됨\n" + e.toString());
        }

        //이미지 array list 반환
        for(int i = 0 ; i<img_url_list.size(); i++){
            img_url_list_result.add(img_url_list.get(i));
        }

        images.clear();

        for(int i =0; i<img_url_list_result.size(); i++){
            //images ArrayList에 클래스 넣고
            images.add(new Frag4.pic_info());
            //이미지 주소 넣고
            images.get(i).set_url(img_url_list_result.get(i));
            //이미지 태그 넣고
            images.get(i).set_tag(img_tag_list.get(i));
        }
        refresh_check = true;      //다시 초기화
        return img_url_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixabay_category);

        //픽사베이 http 통신을 위해서 thread 생성
        thread = new Thread(){
            public void run(){
                //로그
                //Log.i("check", "요청 페이지 :" + page_num);
                ArrayList<String> test = default_pixa(page_num);

                Bundle bun = new Bundle();
                bun.putStringArrayList("PIXA_URL", test);

                if(page_num == 1) {
                    Message msg = mHandler.obtainMessage(LOAD_SUCCESS);
                    mHandler.sendMessage(msg);
                }

            }
        };

        //로그
        //Log.i("check", "스레드 시작");
        thread.start();

        Intent intent = getIntent();        //넘겨 받은 값 가져오기 위해 intent 생성
        category = intent.getStringExtra("category");       //카테고리 입력

        recyclerView = (RecyclerView)findViewById(R.id.recycleview_category);        //리사이클 뷰
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        final IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, ImageView imageView) {
                //open full screen activity with omage clicked
                Intent i = new Intent(Activity_Pixabay_Category.this, Activity_Fullscreen.class);

                /***************************************************/
                /** 클래스를 보내기 위해서는 Bundle을 통해서 통신 **/
                /***************************************************/
                Bundle bundle = new Bundle();
                bundle.putSerializable("PIC_INFO", images);

                //번들을 intent에 넣어서 보냄
                i.putExtras(bundle);

                i.putExtra("POSITION", position);
                startActivity(i);
            }
            @Override
            public void onLongClick(View view, int position, ImageView imageView) {

            }

            @Override
            public void onClick(int adapterPosition, ImageView imageView, CheckBox checkBox) {

            }

            @Override
            public void onLongClick(View v, int adapterPosition, ImageView imageView, CheckBox checkBox) {

            }
        };

        galleryImageAdapter = new GalleryImageAdapter(Activity_Pixabay_Category.this, images, listener);
        recyclerView.setAdapter(galleryImageAdapter);


        //하단 페이지 도착시 자료 갱신
        //참고 : https://medium.com/@ydh0256/android-recyclerview-%EC%9D%98-%EC%B5%9C%EC%83%81%EB%8B%A8%EA%B3%BC-%EC%B5%9C%ED%95%98%EB%8B%A8-%EC%8A%A4%ED%81%AC%EB%A1%A4-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EA%B0%90%EC%A7%80%ED%95%98%EA%B8%B0-f0e5fda34301
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(-1)){
                    //Log.i("check", "리스트 상단");
                }
                else if(!recyclerView.canScrollVertically(1)){
                    //Log.i("check", "리스트 하단");


                    if(refresh_check) {
                        page_num+=1;
                        thread = new Thread() {
                            public void run() {
                                //로그
                                //Log.i("check", "요청 페이지 :" + page_num);
                                ArrayList<String> test = default_pixa(page_num);

                                Bundle bun = new Bundle();
                                bun.putStringArrayList("PIXA_URL", test);

                                Message msg = mHandler.obtainMessage(LOAD_SUCCESS);
                                mHandler.sendMessage(msg);
                            }
                        };
                        thread.start();
                    }
                    //어뎁터를 추가로 다는 방법
                    /*
                    galleryImageAdapter = new GalleryImageAdapter(getActivity(), images, listener);
                    recyclerView.setAdapter(galleryImageAdapter);
                    */
                }
                else{
                    //Log.i("check", "리스트 중간");
                }
            }
        });
        this.galleryImageAdapter.notifyDataSetChanged();


    }
}

