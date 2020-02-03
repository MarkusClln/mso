package mso.eventium.ui.newsfeed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import mso.eventium.R;
import mso.eventium.model.Newsfeed;
import mso.eventium.ui.bonussystem.BonussystemAdapter;
import mso.eventium.ui.bonussystem.BonussystemListFragment;

public class NewsfeedFragment extends Fragment implements NewsfeedAdapter.OnNoteListener  {

    public NewsfeedAdapter mAdapter;
    private List<Newsfeed> newsfeedList;
    private RecyclerView mRecyclerView;

    public NewsfeedFragment() {
        // Required empty public constructor
    }


    public static NewsfeedFragment newInstance() {
        NewsfeedFragment fragment = new NewsfeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        newsfeedList = new ArrayList<>();
        newsfeedList = getDummyData();
        //eventModels.add(new Event("Event1_1", "","", "23.01.2019", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails, ""));
        mRecyclerView = root.findViewById(R.id.newsfeedRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NewsfeedAdapter(getContext(), newsfeedList, this);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(root.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



        return root;
    }

    private List<Newsfeed> getDummyData(){
        List<Newsfeed> list = new ArrayList<>();

        list.add(new Newsfeed("SAPARENA","Das Event 'Adler Mannheim Playoff-Spiel 1' wurde hinzugefügt", true));
        list.add(new Newsfeed("Mannheim Wasserturm","Das Event 'Weihnachtsmarkt 2020' wurde hinzugefügt", true));
        list.add(new Newsfeed("Hochschule Mannheim","Das Event 'MSO-Präsentation' wurde aktualisiert", false));

        return list;
    }

    @Override
    public void onNoteClick(int position) {
    }

}
