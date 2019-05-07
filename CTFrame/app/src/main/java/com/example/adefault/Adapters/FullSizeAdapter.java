package com.example.adefault.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.adefault.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class FullSizeAdapter extends PagerAdapter
{
    Context context;
    //수정
    //String[] images;
    ArrayList<String> images;
    LayoutInflater inflater;

    //수정 String[] images 를 ArrayList<String> 으로
    public FullSizeAdapter(Context context, ArrayList<String> images)
    {
        this.context =context;
        this.images = images;
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.full_item,null);

     // ImageView imageView =(ImageView)v.findViewById(R.id.img);
//      Glide.with(context).load(images[position]).apply(new RequestOptions().centerInside())
  //            .into(imageView);
    //  ViewPager vp = (ViewPager)container;
      //vp.addView(v,0);return v;
        PhotoView imageView = (PhotoView)v.findViewById(R.id.img);
        //수정 .load(images[position] 을 .load(images.get(position)))
        Glide.with(context).load(images.get(position)).apply(new RequestOptions().centerInside())
                .thumbnail(0.5f)
                .into(imageView);
  ViewPager vp = (ViewPager)container;
        vp.addView(v,0);return v;}
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
     // super.destroyItem();
        ViewPager viewPager = (ViewPager)container;
        View v = (View)object;
        viewPager.removeView(v);

}

}