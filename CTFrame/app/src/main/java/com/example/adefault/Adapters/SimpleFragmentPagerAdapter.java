package com.example.adefault.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.adefault.Frag1;
import com.example.adefault.Frag2;
import com.example.adefault.Frag3;
import com.example.adefault.Frag4;
import com.example.adefault.R;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        if (position == 0) {
            return new Frag4();
        } else if (position == 1){
            return new Frag2();
        } else if(position==2) {
            return new Frag3();
        }
        return new Frag1();
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.default_pixa);
            case 1:
                return mContext.getString(R.string.by_category);
            case 2:
                return mContext.getString(R.string.search);
            default:
                return null;
        }
    }
}