package com.example.adefault.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.adefault.Interfaces.IRecyclerViewClickListener;
import com.example.adefault.R;

/**
 * Created by Lirvess on 2019-05-02.
 */

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ImageViewHolder> {

    Context context;
    String[] uriList;
    IRecyclerViewClickListener clickListener;

    public  GalleryImageAdapter(Context context, String[]uriList, IRecyclerViewClickListener clickListener){
        this.context = context;
        this.uriList = uriList;
        this.clickListener = clickListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item,parent,false);
        return new ImageViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String currentImage = uriList[position];
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
    public int getItemCount() {
        return uriList.length;
    }

    public class ImageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        ProgressBar progressBar;

        public ImageViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            clickListener.onClick(v,getAdapterPosition());
        }
    }
}
