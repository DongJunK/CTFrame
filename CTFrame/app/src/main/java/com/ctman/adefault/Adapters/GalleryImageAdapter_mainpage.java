package com.ctman.adefault.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ctman.adefault.Interfaces.IRecyclerViewClickListener;
import com.ctman.adefault.MainActivity;
import com.ctman.adefault.R;

import java.util.ArrayList;

/**
 * Created by Lirvess on 2019-05-02.
 */

public class GalleryImageAdapter_mainpage extends RecyclerView.Adapter<GalleryImageAdapter_mainpage.ImageViewHolder> {

    Context context;
    ArrayList<String> List;     //이미지 정보 가지고 있는 ArrayList
    //String[] List; //수정
    IRecyclerViewClickListener clickListener;

    //수정
    public static SparseBooleanArray itemStateArray= new SparseBooleanArray();        //체크된것을 확인하기 위한 SparseBooleanArray
    /*
    public  GalleryImageAdapter(Context context, String[]List, IRecyclerViewClickListener clickListener){
        this.context = context;
        this.List = List;
        this.clickListener = clickListener;
    }
     */
    public GalleryImageAdapter_mainpage(Context context, ArrayList<String> List, IRecyclerViewClickListener clickListener){
        this.context = context;
        this.List = List;
        this.clickListener = clickListener;

    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.gallery_item,parent,false);
        return new ImageViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        //String currentImage = List[position];  //수정
        String currentImage = List.get(position);     //이미지 주소 가지고 오고

        //수정
        holder.setIsRecyclable(false);      //리사이클 하면 재 갱싱 되는거 막음
        holder.bind(position);      //해당 위치에 맞게 클릭

        ImageView imageView = holder.imageView;
        final ProgressBar progressBar = holder.progressBar;


        Glide.with(context).load(currentImage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        //return List.length; //수정
        return List.size();

    }

    public class ImageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        ImageView imageView;
        ProgressBar progressBar;
        CheckBox checkBox;
        public ImageViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progBar);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v){
            int adapterPosition = getAdapterPosition();     //어뎁터 위치값 가져옴

            //선택 모드이면
            if(MainActivity.selectMode == true){
                if(!itemStateArray.get(adapterPosition, false)){
                    //클릭 했을때 Array에 위치값이 false 가 아니면 클릭이라고 넣고
                    clickListener.onClick(getAdapterPosition(),imageView,checkBox);
                    itemStateArray.put(adapterPosition, true);
                }else{
                    clickListener.onClick(getAdapterPosition(),imageView,checkBox);
                    itemStateArray.put(adapterPosition, false);
                }
            }else{
                clickListener.onClick(getAdapterPosition(),imageView,checkBox);
            }


        }

        @Override
        public boolean onLongClick(View v) {
            Log.i("CTFrame","LongClickTrue");
            clickListener.onLongClick(v,getAdapterPosition(),imageView, checkBox);

            //선택 모드 진입하는 사진도 어레이에 넣어주고
            int adapterPosition = getAdapterPosition();     //어뎁터 위치값 가져옴
            itemStateArray.put(adapterPosition, true);

            return true;
        }

        void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {

                //mCheckedTextView.setChecked(false);
            }else {
                clickListener.onClick(getAdapterPosition(),imageView,checkBox);
            }
        }
    }
}
