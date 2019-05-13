package com.example.adefault.Interfaces;


import android.view.View;
import android.widget.ImageView;

public interface IRecyclerViewClickListener {
    void onClick(View view, int position, ImageView imageView);
    void onLongClick(View view, int position, ImageView imageView);
}
