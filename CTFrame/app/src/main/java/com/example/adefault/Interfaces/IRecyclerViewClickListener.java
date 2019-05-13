package com.example.adefault.Interfaces;


import android.view.View;

public interface IRecyclerViewClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
