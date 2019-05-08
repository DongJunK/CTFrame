package com.example.adefault;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.adefault.Adapters.GalleryImageAdapter;
import com.example.adefault.Interfaces.IRecyclerViewClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Frag3 extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    GalleryImageAdapter galleryImageAdapter;        //어뎁터

    public static int page_num =1;      //초기 페이지 값
    public static Thread thread;
    public static  final int LOAD_SUCCESS = 101;        //성공값
    public String search_val = "";

    ArrayList<String> img_url_list_result = new ArrayList<>();     //이미지 url를 저장할 array list
    ArrayList<String> img_tag_list = new ArrayList<>();     //이미지 tag를 저장할 array list
    ArrayList<String> img_user_list = new ArrayList<>();    //이미지 user를 저장할 array list
    ArrayList<String> img_like_list = new ArrayList<>();    //이미지 like를 저장할  array list

    final ArrayList<String> images = new ArrayList<>();
    private final Frag3.MyHandler mHandler = new Frag3.MyHandler(Frag3.this);

    //UI를 갱신 해주는 핸들러
    private static class MyHandler extends Handler {
        private final WeakReference<Frag3> weakReference;

        public MyHandler(Frag3 mainactivity) {
            weakReference = new WeakReference<Frag3>(mainactivity);
        }

        @Override
        public void handleMessage(Message msg) {

            Frag3 mainactivity = weakReference.get();

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
        ArrayList<String> img_url_list = new ArrayList<>();     //이미지 url를 저장할 array list

        String API_KEY = "10343836-4aa4b118c22502ac110971841";     // api key 값
        int Page_num = page;        //페이지 값
        String url = "https://pixabay.com/api/?key=" + API_KEY + "&q=" + search_val + "&page=" + Page_num + "&image_type=photo&order=latest";      //검색 값
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
            img_url_list_result.add(img_url_list.get(i));
        }

        images.clear();

        for(int i =0; i<img_url_list_result.size(); i++){
            images.add(img_url_list_result.get(i));
            //images[i] = img_url_list_result.get(i);//수정
        }

        return img_url_list;
    }

    public static Frag3 newInstance() {
        Frag3 fragment = new Frag3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag3, container, false);
//        GridView gridView = (GridView) view.findViewById(R.id.galleryGridView);
//        gridView.setAdapter(new ImageAdapter(getContext()));
//        registerForContextMenu(gridView);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

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

        galleryImageAdapter = new GalleryImageAdapter(getActivity(), images, listener);
        recyclerView.setAdapter(galleryImageAdapter);

        //검색 버튼 눌렀을때 동작
        final SearchView searchView = (SearchView)view.findViewById(R.id.search_pixa);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색어 입력 후 버튼 클릭하면 일어나는 동작
                //로그
                Log.i("check", "삭제이전 " + Integer.toString(images.size()));


                //galleryImageAdapter.notifyDataSetChanged();     //재 갱신
                if(images.size() > 0){
                    //recyclerView.removeItemDecorationAt(0);
                    img_url_list_result.clear();
                    images.clear();     //이미지 배열 초기화
                    galleryImageAdapter.notifyDataSetChanged();     //재 갱신
                    //clear(images, galleryImageAdapter);     //내용 지우는 함수 실행
                    //로그
                    Log.i("check", "삭제이후 " + Integer.toString(images.size()));
                }

                page_num = 1;       //검색 페이지는 1페이지로 다시 초기화
                search_val = query;     //검색값 변수 값 저장

                thread = new Thread(){
                    public void run(){
                        //로그
                        Log.i("check", "검색 요청 페이지 :" + page_num);
                        ArrayList<String> test = default_pixa(page_num);

                        Bundle bun = new Bundle();
                        bun.putStringArrayList("PIXA_URL", test);

                        Message msg = mHandler.obtainMessage(LOAD_SUCCESS);
                        mHandler.sendMessage(msg);
                    }
                };

                thread.start();

                Log.i("check", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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

    //어뎁터의 내용을 지우는 함수
    public void clear(ArrayList<String> data, GalleryImageAdapter galleryImageAdapter) {
        final int size = data.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                data.remove(0);
            }

            galleryImageAdapter.notifyItemRangeRemoved(0, size);
        };
        galleryImageAdapter.notifyItemChanged(0,size);
        galleryImageAdapter.notifyDataSetChanged();
    }
}