package com.example.mso_projectxxx.ui.dashboard;

import android.app.Activity;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeTransform;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionSet;

import com.example.mso_projectxxx.MainActivity;
import com.example.mso_projectxxx.R;
import com.example.mso_projectxxx.ui.dashboard.eventDetails;


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



        return root;


    }


    public List<item> items;

    private void initializeData(){
        items = new ArrayList<>();
        items.add(new item("Emma Wilson", "23 years old", R.drawable.bild1));
        items.add(new item("Lavery Maiss", "25 years old", R.drawable.ic_dashboard_black_24dp));
        items.add(new item("Lillie Watts", "35 years old", R.drawable.ic_dashboard_black_24dp));



    }


    @Override
    public void onNoteClick(int position) {

        ImageView mViewPhoto = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.person_photo);
        TextView mViewName = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.person_name);
        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));

        // Create new fragment to add (Fragment B)
        item item = items.get(position);
        String transitionPhoto = "transitionPhoto"+position;
        String transitionName = "transitionName"+position;
        Fragment fragment = eventDetails.newInstance(item.name, item.age, item.photoId, transitionPhoto, transitionName);
        fragment.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
        fragment.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));

        // Add Fragment B
        FragmentTransaction ft = getFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack("transaction")
                .addSharedElement(mViewPhoto, transitionPhoto)
                .addSharedElement(mViewName, transitionName);

        ft.commit();
        
    }

}

