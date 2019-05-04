package com.example.adefault;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;


public class Frag2 extends Fragment {
    GridView gridView;

    public static Frag2 newInstance() {
        Frag2 fragment = new Frag2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag2, container, false);
        // Create an ArrayList of Category objects
        final ArrayList<Category> categories = new ArrayList<Category>();
        categories.add(new Category(getString(R.string.Architecture), R.drawable.architecture, getString(R.string.first_p),
                25.093163, 55.159065, getString(R.string.map_location_title)));

        categories.add(new Category(getString(R.string.BeautyFashion), R.drawable.beauty_fashion, getString(R.string.first_p),
                52.248346, 21.015270, getString(R.string.map_location_title2)));

        categories.add(new Category(getString(R.string.NatureLandscapes), R.drawable.nature_landscapes, getString(R.string.first_p),
                25.093163, 55.159065, getString(R.string.map_location_title)));

        categories.add(new Category(getString(R.string.BusinessFinance), R.drawable.business_finance, getString(R.string.first_p),
                52.248346, 21.015270, getString(R.string.map_location_title2)));

        categories.add(new Category(getString(R.string.FoodDrink), R.drawable.food_drink, getString(R.string.first_p),
                25.093163, 55.159065, getString(R.string.map_location_title)));

        categories.add(new Category(getString(R.string.People), R.drawable.people, getString(R.string.first_p),
                52.248346, 21.015270, getString(R.string.map_location_title2)));

        // Create an {@link CategoryAdapter}, whose data source is a list of
        // {@link Categories}. The adapter knows how to create list item views for each item
        // in the list.

        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(categoryAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

            }
        });
        return view;
    }
}
