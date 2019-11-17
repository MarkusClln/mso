package com.example.mso_projectxxx.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mso_projectxxx.R;


import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initializeData();
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        mRecyclerView = (RecyclerView)root.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(root.getContext());
        mAdapter = new RVAdapter(items);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }


    public List<item> items;

    private void initializeData(){
        items = new ArrayList<>();
        items.add(new item("Emma Wilson", "23 years old", R.drawable.ic_dashboard_black_24dp));
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


}