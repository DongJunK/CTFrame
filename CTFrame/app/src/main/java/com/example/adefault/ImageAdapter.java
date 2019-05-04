package com.example.adefault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {

    private int[] images_id ={R.drawable.pix1,R.drawable.pix2,R.drawable.pix3,R.drawable.pix4,R.drawable.pix5,R.drawable.pix6,
            R.drawable.pix7,R.drawable.pix8,R.drawable.pix9,R.drawable.pix10};
    Context ctx;
    ImageAdapter(Context ctx){
        this.ctx =ctx;
    }
    @Override
    public int getCount(){
        return images_id.length;
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public Object getItem(int position){
        return images_id[position];
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View gridView = convertView;
        if(gridView==null){
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.custom_img_layout,null);
        }
        ImageView imv = (ImageView)gridView.findViewById(R.id.myImage);
        imv.setImageResource(images_id[position]);
        return gridView;
    }

    public View getView1(int arg0, View arg1, ViewGroup arg2) {

        ImageView imageView = new ImageView(ctx);
        imageView.setImageResource(images_id[arg0]);
        return imageView;
    }
}
