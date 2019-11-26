package mso.eventium.ui.events;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import mso.eventium.R;
import mso.eventium.adapter.ViewPagerAdapter;


public class EventFragment  extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private EditText searchInput;


    private EventListFragment df1 = EventListFragment.newInstance("df1");
    private EventListFragment df2 = EventListFragment.newInstance("df2");
    private EventListFragment df3 = EventListFragment.newInstance("df3");



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event, container, false);



        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        searchInput =(EditText) root.findViewById(R.id.searchBox);

        FragmentManager fm = getFragmentManager();
        adapter = new ViewPagerAdapter(fm);


        adapter.AddFragment(df1, "ALLE");
        adapter.AddFragment(df2, "GESPEICHERT");
        adapter.AddFragment(df3, "EIGENE");

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
                df1.mAdapter.getFilter().filter(s);
                df2.mAdapter.getFilter().filter(s);
                df3.mAdapter.getFilter().filter(s);
                df1.search=s;
                df2.search=s;
                df3.search=s;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
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