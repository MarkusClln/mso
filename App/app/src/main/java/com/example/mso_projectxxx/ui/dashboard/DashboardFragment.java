package com.example.mso_projectxxx.ui.dashboard;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mso_projectxxx.MainActivity;
import com.example.mso_projectxxx.R;


import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;


public class DashboardFragment extends Fragment implements RVAdapter.OnNoteListener {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView person_photo;
    private TextView person_age;
    private TextView person_name;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initializeData();
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mRecyclerView = (RecyclerView)root.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(root.getContext());
        mAdapter = new RVAdapter(items, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        View item = inflater.inflate(R.layout.item, container, false);


        person_photo = (ImageView) item.findViewById(R.id.person_photo);
        person_age = (TextView)item.findViewById(R.id.person_age);
        person_name = (TextView)item.findViewById(R.id.person_name);

        return root;


    }


    public List<item> items;

    private void initializeData(){
        items = new ArrayList<>();
        items.add(new item("Emma Wilson", "23 years old", R.drawable.bild1));
        items.add(new item("Lavery Maiss", "25 years old", R.drawable.ic_dashboard_black_24dp));
        items.add(new item("Lillie Watts", "35 years old", R.drawable.ic_dashboard_black_24dp));
        items.add(new item("Lillie Watts", "35 years old", R.drawable.ic_dashboard_black_24dp));
        items.add(new item("Lillie Watts", "35 years old", R.drawable.ic_dashboard_black_24dp));
        items.add(new item("Lillie Watts", "35 years old", R.drawable.ic_dashboard_black_24dp));
        items.add(new item("Lillie Watts", "35 years old", R.drawable.ic_dashboard_black_24dp));
        items.add(new item("Lillie Watts", "35 years old", R.drawable.ic_dashboard_black_24dp));
        items.add(new item("Lillie Watts", "35 years old", R.drawable.ic_dashboard_black_24dp));
        items.add(new item("Lillie Watts", "35 years old", R.drawable.ic_dashboard_black_24dp));


    }


    @Override
    public void onNoteClick(int position) {

        item item = items.get(position);
        Pair[] pair = new Pair[3];
        pair[0] = new Pair<View, String> (person_photo, "image_shared");
        pair[1] = new Pair<View, String> (person_age, "text2_shared");
        pair[2] = new Pair<View, String> (person_name, "text_shared");


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this.getActivity(), pair);
        Intent i = new Intent(getActivity(), Dashboard_shared.class);
        i.putExtra("name", item.name);
        i.putExtra("age", item.age);
        i.putExtra("pic", item.photoId);
        //startActivity(i, options.toBundle());
        startActivity(i);
        CustomIntent.customType(this.getContext(), "left-to-right");
    }
}