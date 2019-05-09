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
        categories.add(new Category(getString(R.string.Architecture), R.drawable.architecture));

        categories.add(new Category(getString(R.string.BeautyFashion), R.drawable.beauty_fashion));

        categories.add(new Category(getString(R.string.Nature_Landscapes), R.drawable.nature_landscapes));

        categories.add(new Category(getString(R.string.BusinessFinance), R.drawable.business_finance));

        categories.add(new Category(getString(R.string.FoodDrink), R.drawable.food_drink));

        categories.add(new Category(getString(R.string.People), R.drawable.people));

        categories.add(new Category(getString(R.string.Science), R.drawable.scinec));

        categories.add(new Category(getString(R.string.Education), R.drawable.euducation));

        categories.add(new Category(getString(R.string.Feelings), R.drawable.feelings));

        categories.add(new Category(getString(R.string.Religion), R.drawable.religion));

        categories.add(new Category(getString(R.string.Health), R.drawable.health));

        categories.add(new Category(getString(R.string.Animals), R.drawable.animals));

        categories.add(new Category(getString(R.string.Places), R.drawable.places));

        categories.add(new Category(getString(R.string.Industry), R.drawable.industry));

        categories.add(new Category(getString(R.string.Food), R.drawable.food));

        categories.add(new Category(getString(R.string.Computer), R.drawable.computer));

        categories.add(new Category(getString(R.string.Transportation), R.drawable.transportation));

        categories.add(new Category(getString(R.string.Travel), R.drawable.travel));

        categories.add(new Category(getString(R.string.Music), R.drawable.music));

        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories);

        // Get a reference to the ListView, and attach the adapter to the listView.
        final ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(categoryAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
               // if (listView[position]==1):
               // Intent i = new Intent();
            }
        });
        return view;
    }
}
