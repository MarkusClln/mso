package mso.eventium.ui.host;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import mso.eventium.R;
import mso.eventium.adapter.ViewPagerAdapter;
import mso.eventium.ui.events.EventListFragment;

public class HostDetailActivity extends AppCompatActivity {
    public static final String ARG_HOST_NAME = "name";
    public static final String ARG_HOST_ICON = "icon";

    public static final String ARG_TRANSITION_EVENT_ICON = "transitionIcon";
    public static final String ARG_TRANSITION_EVENT_NAME = "transitionName";


    private String mName;
    private int mIcon;

    private String mTransitionIcon;
    private String mTransitionName;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = getIntent().getStringExtra(ARG_HOST_NAME);
        //mIcon = getIntent().getIntExtra(ARG_HOST_ICON,0);


        mTransitionName = getIntent().getStringExtra(ARG_TRANSITION_EVENT_NAME);
        // mTransitionIcon = getIntent().getStringExtra(ARG_TRANSITION_EVENT_ICON);

        setContentView(R.layout.activity_host_detail);

        TextView mNameView;
        //ImageView mIconView;

        mNameView = findViewById(R.id.hostDetailName);
        //mIconView = (ImageView)findViewById(R.id.hostDetailImage);

        mNameView.setText(mName);
        //mIconView.setImageResource(mIcon);


        mNameView.setTransitionName(mTransitionName);
        //mIconView.setTransitionName(mTransitionIcon);

        tabLayout = (TabLayout) findViewById(R.id.hostDetailTabLayout);
        viewPager = (ViewPager) findViewById(R.id.hostDetailViewPager);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(fm);


//         EventListFragment eventListFragmentAll = EventListFragment.newInstance("df1");
//         EventListFragment eventListFragmentSaved = EventListFragment.newInstance("df2");
//
//        adapter.AddFragment(eventListFragmentAll, "Aktuelle");
//        adapter.AddFragment(eventListFragmentSaved, "Vergangene");


        //FragmentStateAdapter adapter = new ViewPagerAdapter2(getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);




    }
}
