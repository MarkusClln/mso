package mso.eventium.ui.events;

import android.drm.DrmManagerClient;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import mso.eventium.R;
import mso.eventium.adapter.ViewPagerAdapter;
import mso.eventium.model.Event;
import mso.eventium.ui.fragments.FilterFragment;


public class EventFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private EditText searchInput;
    private View root;
    TextView rangeSliderView;
    TextView dateView;
    TextView timeView;


    private EventListFragment eventListFragmentAll = EventListFragment.newInstance("all");
    private EventListFragment eventListFragmentSaved = EventListFragment.newInstance("saved");
    private EventListFragment eventListFragmentOwned = EventListFragment.newInstance("owned");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_event, container, false);


        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        searchInput =(EditText) root.findViewById(R.id.searchBox);

        FragmentManager fm = getFragmentManager();
        adapter = new ViewPagerAdapter(fm);


        adapter.AddFragment(eventListFragmentAll, "ALLE");
        adapter.AddFragment(eventListFragmentSaved, "GESPEICHERT");
        adapter.AddFragment(eventListFragmentOwned, "EIGENE");

        //FragmentStateAdapter adapter = new ViewPagerAdapter2(getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               doFitler();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        rangeSliderView = root.findViewById(R.id.slider_value);
        dateView = root.findViewById(R.id.date);
        timeView = root.findViewById(R.id.timeText);
        dateView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doFitler();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rangeSliderView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doFitler();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        timeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doFitler();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return root;
    }

    public void doFitler(){
        String text = searchInput.getText().toString();
        String date = dateView.getText().toString();
        String range = rangeSliderView.getText().toString();
        String time = timeView.getText().toString();
        String s = text+"|-|"+date+"|-|"+time+"|-|"+range;
        eventListFragmentAll.mAdapter.getFilter().filter(s);
        eventListFragmentSaved.mAdapter.getFilter().filter(s);
        eventListFragmentOwned.mAdapter.getFilter().filter(s);
    }


    @Override
    public void onResume() {


        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}