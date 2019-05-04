package com.example.adefault;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adefault.Adapters.GalleryImageAdapter;

import com.example.adefault.Interfaces.IRecyclerViewClickListener;

import java.util.Random;


public class Frag4 extends android.support.v4.app.Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public static Frag4 newInstance() {
        Frag4 fragment = new Frag4();
        return fragment;
    }

    private int[] images_id ={R.drawable.pix1,R.drawable.pix2,R.drawable.pix3,R.drawable.pix4,R.drawable.pix5,R.drawable.pix6,
            R.drawable.pix7,R.drawable.pix8,R.drawable.pix9,R.drawable.pix10};

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag4, container, false);
        recyclerView =(RecyclerView)view.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        Random random = new Random();
        final String[] images = new String[10];
        for(int i=0;i<images.length;i++)
//https://pixabay.com/images/search/
            images[i] = "https://picsum.photos/600?image="+random.nextInt(1000+1);
        final IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                //open full screen activity with omage clicked
                Intent i = new Intent(getContext(),Activity_Fullscreen.class);
                i.putExtra("IMAGES",images);
                i.putExtra("POSITION",position);
                startActivity(i);
            }
        };
        // this대신 getActivity 사용
        GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(getActivity(),images,listener);
        recyclerView.setAdapter(galleryImageAdapter);

        return view;

    }
}

