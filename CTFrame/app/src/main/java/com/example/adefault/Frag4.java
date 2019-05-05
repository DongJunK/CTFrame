package com.example.adefault;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adefault.Adapters.GalleryImageAdapter;

import com.example.adefault.Interfaces.IRecyclerViewClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;


public class Frag4 extends android.support.v4.app.Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<String> img_url_list_result = new ArrayList<>();     //이미지 url를 저장할 array list
    ArrayList<String> img_tag_list = new ArrayList<>();     //이미지 tag를 저장할 array list
    ArrayList<String> img_user_list = new ArrayList<>();    //이미지 user를 저장할 array list
    ArrayList<String> img_like_list = new ArrayList<>();    //이미지 like를 저장할  array list

    final String[] images = new String[10];     //출력할 사진 갯수
    public static  final int LOAD_SUCCESS = 101;        //성공값
    GalleryImageAdapter galleryImageAdapter;        //어뎁터

    public static Frag4 newInstance() {
        Frag4 fragment = new Frag4();
        return fragment;
    }

    private int[] images_id = {R.drawable.pix1, R.drawable.pix2, R.drawable.pix3, R.drawable.pix4, R.drawable.pix5, R.drawable.pix6,
            R.drawable.pix7, R.drawable.pix8, R.drawable.pix9, R.drawable.pix10};

    private final MyHandler mHandler = new MyHandler(Frag4.this);

    //핸들러 작성
    private static class MyHandler extends Handler {
        private final WeakReference<Frag4> weakReference;

        public MyHandler(Frag4 mainactivity) {
            weakReference = new WeakReference<Frag4>(mainactivity);
        }

        @Override
        public void handleMessage(Message msg) {

            Frag4 mainactivity = weakReference.get();

            if (mainactivity != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        mainactivity.galleryImageAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //픽사베이 http 통신을 위해서 thread 생성
        new Thread(){
            public void run(){
                ArrayList<String> test = default_pixa(1);

                Bundle bun = new Bundle();
                bun.putStringArrayList("PIXA_URL", test);

                Message msg = mHandler.obtainMessage(LOAD_SUCCESS);
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**************************************************/
    /****** 픽사 베이 기본 사진 다운 받는 함수*********/
    /****** 파라미터 검색할 페이지 : page *********/
    /**************************************************/
    public ArrayList<String> default_pixa(int page){
        ArrayList<String> img_url_list = new ArrayList<>();     //이미지 url를 저장할 array list

        String API_KEY = "10343836-4aa4b118c22502ac110971841";     // api key 값
        int Page_num = page;        //페이지 값
        String url = "https://pixabay.com/api/?key=" + API_KEY + "&page=" + Page_num + "&image_type=photo&order=latest";      //검색 값
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
            Log.i("pixacheck", "default_pixa  예외처리됨\n" + e.toString());
        }

        //이미지 array list 반환
        for(int i = 0 ; i<img_url_list.size(); i++){
            Log.i("pixacheck", img_url_list.get(i));
            img_url_list_result.add(img_url_list.get(i));
        }

        for(int i =0; i<10; i++){
            images[i] = img_url_list_result.get(i);
        }

        return img_url_list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag4, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        //final String[] images = new String[10];
        /*
        Random random = new Random();


        for (int i = 0; i < images.length; i++)
            //https://pixabay.com/images/search/
            //이미지 배열에 픽사베이에서 가져온 사진 들고 오기
            images[i] = "https://picsum.photos/600?image=" + random.nextInt(1000 + 1);
        */

        /*
        for(int i =0; i<10; i++){
            images[i] = img_url_list_result.get(i);
        }
        */
        final IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                //open full screen activity with omage clicked
                Intent i = new Intent(getContext(), Activity_Fullscreen.class);
                i.putExtra("IMAGES", images);
                i.putExtra("POSITION", position);
                startActivity(i);
            }
        };

        // this대신 getActivity 사용

        galleryImageAdapter = new GalleryImageAdapter(getActivity(), images, listener);
        recyclerView.setAdapter(galleryImageAdapter);


        return view;

    }
}

