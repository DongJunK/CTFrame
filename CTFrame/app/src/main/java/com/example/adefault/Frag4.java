package com.example.adefault;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adefault.Adapters.GalleryImageAdapter;

import com.example.adefault.Interfaces.IRecyclerViewClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Frag4 extends android.support.v4.app.Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public static int page_num =1;      //초기 페이지 값
    public static Thread thread;

    ArrayList<String> img_url_list_result = new ArrayList<>();     //이미지 url를 저장할 array list
    ArrayList<String> img_tag_list = new ArrayList<>();     //이미지 tag를 저장할 array list
    ArrayList<String> img_user_list = new ArrayList<>();    //이미지 user를 저장할 array list
    ArrayList<String> img_like_list = new ArrayList<>();    //이미지 like를 저장할  array list

    //final String[] images = new String[20];     //출력할 사진 갯수//수정
    final ArrayList<Frag4.pic_info> images = new ArrayList<>();
    public static  final int LOAD_SUCCESS = 101;        //성공값

    GalleryImageAdapter galleryImageAdapter;        //어뎁터

    //생성자 다른 클래스에서 호출 가능하도록
    public Frag4()
    {    }

    /************************************************************************/
    /******************* 사진 정보를 담을 클래스 ****************************/
    /******************* 정보 : 이미지 url, tag 값 **************************/
    /******************* 함수 : 생성자, set_tag, get_url, get_tag ***********/
    /************************************************************************/
    @SuppressWarnings("serial")
    public static class pic_info implements Serializable {
        String url = "";
        String tag = "";

        //생성자
        public pic_info()
        {
        }

        //url를 넣는 함수
        public void set_url(String url)
        {
            this.url = url;
        }

        //tag 값을 넣는 함수
        public void set_tag(String tag)
        {
            this.tag = tag;
        }

        //url를 반환 하는 함수
        public String get_url()
        {
            return this.url;
        }

        //tag를 반환 하는 함수
        public String get_tag()
        {
            return this.tag;
        }
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
                        //thread.interrupt();
                        //로그
                        //Log.i("check", "쓰레드 종료");
                        break;
                }
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        page_num =1;

        //픽사베이 http 통신을 위해서 thread 생성
        thread = new Thread(){
            public void run(){
                //로그
                Log.i("check", "요청 페이지 :" + page_num);
                ArrayList<String> test = default_pixa(page_num);

                //Bundle bun = new Bundle();
                //bun.putStringArrayList("PIXA_URL", test);

                if(page_num == 1) {
                    Message msg = mHandler.obtainMessage(LOAD_SUCCESS);
                    mHandler.sendMessage(msg);
                }

            }
        };

        //로그
        Log.i("check", "스레드 시작");
        thread.start();
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
        //로그
        Log.i("check", url);
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

                //array list에 저쟝
                img_url_list.add(img_url);
                img_tag_list.add(img_tag);

            }
        }
        catch (Exception e){
            default_pixa(page);
            //로그
            Log.i("pixacheck", "default_pixa  예외처리됨\n" + e.toString());
        }

        //이미지 array list 반환
        for(int i = 0 ; i<img_url_list.size(); i++){
            img_url_list_result.add(img_url_list.get(i));
        }

        images.clear();

        /***********************************************************/
        //최종 출력할 이미지 ArrayList에 값을 넣어줌
        for(int i =0; i<img_url_list_result.size(); i++){
            //images ArrayList에 클래스 넣고
            images.add(new pic_info());
            //이미지 주소 넣고
            images.get(i).set_url(img_url_list_result.get(i));
            //이미지 태그 넣고
            images.get(i).set_tag(img_tag_list.get(i));

            //images.add(img_url_list_result.get(i));
        }
        /***********************************************************/

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

        //FullScreen 엑티비티 실행 , 저장 실행 되는곳
        final IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, ImageView imageView) {
                //open full screen activity with omage clicked
                Intent i = new Intent(getContext(), Activity_Fullscreen.class);
                /***************************************************/
                /** 클래스를 보내기 위해서는 Bundle을 통해서 통신 **/
                /***************************************************/
                Bundle bundle = new Bundle();
                bundle.putSerializable("PIC_INFO", images);

                //번들을 intent에 넣어서 보냄
                i.putExtras(bundle);
                //i.putExtra("IMAGES", images);
                i.putExtra("POSITION", position);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position, ImageView imageView) {

            }
        };

        // this대신 getActivity 사용
        galleryImageAdapter = new GalleryImageAdapter(getActivity(), images, listener);
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
                    Log.i("check", "리스트 하단");
                    page_num+=1;

                    thread = new Thread(){
                        public void run(){
                            //로그
                            Log.i("check", "요청 페이지 :" + page_num);
                            ArrayList<String> test = default_pixa(page_num);

                            Bundle bun = new Bundle();
                            bun.putStringArrayList("PIXA_URL", test);

                            Message msg = mHandler.obtainMessage(LOAD_SUCCESS);
                            mHandler.sendMessage(msg);
                        }
                    };

                    thread.start();

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

        return view;

    }

    //화면이 회전되면 adapter 갱신
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        this.galleryImageAdapter.notifyDataSetChanged();
    }
}

