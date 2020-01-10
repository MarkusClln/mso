package mso.eventium.ui.bonussystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import mso.eventium.R;
import mso.eventium.adapter.ViewPagerAdapter;
import mso.eventium.model.Bonus;
import mso.eventium.ui.events.EventListFragment;

public class BonussystemFragment extends Fragment implements BonussystemAdapter.OnNoteListener {
    private RecyclerView mRecyclerView;
    private BonussystemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<mso.eventium.model.Bonus> bonusList;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private BonussystemListFragment bonusListFragmentAll = BonussystemListFragment.newInstance(BonussystemListFragment.ListTypeEnum.ALL);
    private BonussystemListFragment bonusListFragmentUsed = BonussystemListFragment.newInstance(BonussystemListFragment.ListTypeEnum.USED);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_bonussystem, container, false);
//
//        mRecyclerView = root.findViewById(R.id.rvBonus);
//        mRecyclerView.setHasFixedSize(true);
//
//        mLayoutManager = new LinearLayoutManager(root.getContext());
//        mAdapter = new BonussystemAdapter(getContext(), bonusList, this);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//


        tabLayout = root.findViewById(R.id.bonustablayout);
        viewPager = root.findViewById(R.id.bonusviewpager);
        FragmentManager fm = getFragmentManager();
        adapter = new ViewPagerAdapter(fm);


        adapter.AddFragment(bonusListFragmentAll, "Verfügbare");
        adapter.AddFragment(bonusListFragmentUsed, "Eingelöste");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);

        return root;
    }



    @Override
    public void onNoteClick(int position) {

        //transitionActivity(position);

    }
}
