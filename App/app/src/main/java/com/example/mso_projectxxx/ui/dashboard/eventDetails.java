package com.example.mso_projectxxx.ui.dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mso_projectxxx.R;



public class eventDetails extends Fragment {


    private static final String ARG_NAME = "name";
    private static final String ARG_AGE = "age";
    private static final String ARG_PHOTO = "photo";
    private static final String ARG_TRANSITION_PHOTO = "transitionPhoto1";
    private static final String ARG_TRANSITION_NAME = "transitionName1";

    // TODO: Rename and change types of parameters
    private String mName;
    private String mAge;
    private int mPhoto;
    private String mTransitionPhoto;
    private String mTransitionName;

    public eventDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name Parameter 1.
     * @param age Parameter 2.
     * @param photo Parameter 2.
     * @param transitionPhoto Parameter 2.
     * @param transitionName Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static eventDetails newInstance(String name, String age, int photo, String transitionPhoto, String transitionName) {
        eventDetails fragment = new eventDetails();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_AGE, age);
        args.putInt(ARG_PHOTO, photo);
        args.putString(ARG_TRANSITION_PHOTO, transitionPhoto);
        args.putString(ARG_TRANSITION_NAME, transitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
            mAge = getArguments().getString(ARG_AGE);
            mPhoto = getArguments().getInt(ARG_PHOTO);
            mTransitionPhoto = getArguments().getString(ARG_TRANSITION_PHOTO);
            mTransitionName = getArguments().getString(ARG_TRANSITION_NAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blank, container, false);

        ImageView IV_pic = (ImageView)root.findViewById(R.id.person_photo);
        TextView TV_name = (TextView)root.findViewById(R.id.person_name);
        TextView TV_age = (TextView)root.findViewById(R.id.person_age);

        TV_name.setText(mName);
        //TV_age.setText(mAge);
        IV_pic.setImageResource(mPhoto);

        IV_pic.setTransitionName(mTransitionPhoto);
        TV_name.setTransitionName(mTransitionName);

        return root;
    }





}
