package mso.eventium.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import mso.eventium.R;
import mso.eventium.adapter.ViewPagerAdapter;
import mso.eventium.ui.events.complete.EventCompleteFragmentRoot;
import mso.eventium.ui.home.HomeFragment;
import mso.eventium.ui.map.MapFragment;

public class EventFragment  extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event, container, false);

        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.AddFragment(new EventCompleteFragmentRoot(), "ALLE");
        adapter.AddFragment(new HomeFragment(), "GESPEICHERT");
        adapter.AddFragment(new MapFragment(), "EIGENE");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        return root;
    }
}