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

import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import mso.eventium.R;
import mso.eventium.adapter.ViewPagerAdapter;
import mso.eventium.ui.fragments.FilterFragment;


public class EventFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private EditText searchInput;


    private EventListFragment eventListFragmentAll = EventListFragment.newInstance("df1");
    private EventListFragment eventListFragmentSaved = EventListFragment.newInstance("df2");
    private EventListFragment eventListFragmentOwned = EventListFragment.newInstance("df3");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event, container, false);


        setFilterFragment();



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
                eventListFragmentAll.mAdapter.getFilter().filter(s);
                eventListFragmentSaved.mAdapter.getFilter().filter(s);
                eventListFragmentOwned.mAdapter.getFilter().filter(s);
                eventListFragmentAll.search = s;
                eventListFragmentSaved.search = s;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    public void setFilterFragment(){
        FilterFragment filterFragment = new FilterFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.filter_fragment, filterFragment);
        fragmentTransaction.commit();

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