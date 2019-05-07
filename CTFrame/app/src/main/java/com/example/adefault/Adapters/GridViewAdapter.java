package com.example.adefault.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.adefault.R;

public class GridViewAdapter extends BaseAdapter {

    private int icons[];
    private Context context;
    private LayoutInflater layoutInflater;
    View view;

    public GridViewAdapter(Context context, int icons[])
    {
        this.context = context;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int i) {
        return icons[i];
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
            icon.setImageResource(icons[i]);
        }
        return view;
    }
}
