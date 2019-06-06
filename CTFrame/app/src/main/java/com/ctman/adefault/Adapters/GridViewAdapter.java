package com.ctman.adefault.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ctman.adefault.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private ArrayList<String> image;
    private Context context;
    private LayoutInflater layoutInflater;
    View view;
    Bitmap bitmap;
    String pic;
    public GridViewAdapter(Context context, ArrayList<String> image)
    {
        this.context = context;
        this.image = image;
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public Object getItem(int i) {
        return image.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convrtview, ViewGroup viewGroup) {

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convrtview == null) {
            view = new View(context);
            view = layoutInflater.inflate(R.layout.listview_item, null);
            ImageView icon = (ImageView)view.findViewById(R.id.iv_image);
            pic = image.get(i);
            Thread mThread = new Thread() {
                public void run() {
                    try {
                        //Log.i("CTFrame",pic);
                        URL url = new URL(pic);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start();

            try {
                mThread.join();
                icon.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return view;
    }
}
