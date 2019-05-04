package com.example.adefault;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Frag3 extends Fragment {
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
        return view;
    }
}