package com.example.adefault;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.adefault.Adapters.CategoryAdapter;

import java.util.ArrayList;


public class Frag2 extends Fragment {
    GridView gridView;
    Intent intent;
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
        categories.add(new Category(getString(R.string.fashion), R.drawable.fashion));
        categories.add(new Category(getString(R.string.nature), R.drawable.nature));
        categories.add(new Category(getString(R.string.Backgrounds),R.drawable.back_grounds));
        categories.add(new Category(getString(R.string.science), R.drawable.science));
        categories.add(new Category(getString(R.string.education), R.drawable.euducation));
        categories.add(new Category(getString(R.string.People), R.drawable.people));
        categories.add(new Category(getString(R.string.Feelings), R.drawable.feelings));
        categories.add(new Category(getString(R.string.Religion), R.drawable.religion));
        categories.add(new Category(getString(R.string.Health), R.drawable.health));
        categories.add(new Category(getString(R.string.Places), R.drawable.places));
        categories.add(new Category(getString(R.string.Animals), R.drawable.animals));
        categories.add(new Category(getString(R.string.Industry), R.drawable.industry));
        categories.add(new Category(getString(R.string.Food), R.drawable.food));
        categories.add(new Category(getString(R.string.Computer), R.drawable.computer));
        categories.add(new Category(getString(R.string.sports), R.drawable.sports));
        categories.add(new Category(getString(R.string.Transportation), R.drawable.transportation));
        categories.add(new Category(getString(R.string.Travel), R.drawable.travel));
        categories.add(new Category(getString(R.string.Buildings), R.drawable.buildings));
        categories.add(new Category(getString(R.string.Music), R.drawable.music));

        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories);

        // Get a reference to the ListView, and attach the adapter to the listView.
        final ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(categoryAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
               // if (listView[position]==1):
                Object object = categories.get(position).getCategoryName();
                String val = String.valueOf(object);

                Log.i("check", val);
                intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                intent.putExtra("category", val);
                startActivity(intent);



                if(listView.getAdapter().getItem(position) == "Fashion"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "nature");
                    startActivity(intent);

                }else if(listView.getAdapter().getItem(position) == "Nature"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "nature");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "Backgrounds"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "backgrounds");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "Science"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "science");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "Education"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "education");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "People"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "people");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "Feelings"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "feelings");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "Religion"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "religion");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "Health"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "health");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "Places"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "places");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "Animals"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "animals");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "Industry"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "industry");
                    startActivity(intent);
                }else if(listView.getAdapter().getItem(position) == "nature"){
                    intent = new Intent(getActivity(), Activity_Pixabay_Category.class);
                    intent.putExtra("category", "fashion");
                    startActivity(intent);
                }


            }
        });
        return view;
    }
}
