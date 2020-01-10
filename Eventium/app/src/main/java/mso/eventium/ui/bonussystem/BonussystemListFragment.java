package mso.eventium.ui.bonussystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mso.eventium.R;
import mso.eventium.model.Bonus;
import mso.eventium.model.Event;
import mso.eventium.ui.events.EventListFragment;
import mso.eventium.ui.events.RVAdapter;
import mso.eventium.ui.events.detail.EventDetailActivity;

public class BonussystemListFragment extends Fragment implements BonussystemAdapter.OnNoteListener {
    private static final String LIST_TYPE_ARG_NAME = "listType";
    public enum ListTypeEnum {
        ALL, USED;
    }

    public static BonussystemListFragment newInstance(BonussystemListFragment.ListTypeEnum type) {
        final BonussystemListFragment fragment = new BonussystemListFragment();
        final Bundle args = new Bundle();
        args.putString(LIST_TYPE_ARG_NAME, type.name());
        fragment.setArguments(args);
        return fragment;
    }
    public BonussystemAdapter mAdapter;
    private BonussystemListFragment.ListTypeEnum listType;
    private RecyclerView mRecyclerView;
    private List<Bonus> bonusModels;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_bonussystem_list, container, false);

        this.listType = BonussystemListFragment.ListTypeEnum.valueOf(getArguments().getString(LIST_TYPE_ARG_NAME));
        System.out.println("Create BonusListFragment for list " + listType);

        bonusModels = new ArrayList<>();
        bonusModels = getDummyData();
        //eventModels.add(new Event("Event1_1", "","", "23.01.2019", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails, ""));
        mRecyclerView = root.findViewById(R.id.bonusRv);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new BonussystemAdapter(getContext(), bonusModels, this);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(root.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    public ArrayList<Bonus> getDummyData() {
        ArrayList<Bonus> bonusList = new ArrayList<Bonus>();

        for (int i = 0; i< 20; i++){
            bonusList.add(new Bonus("Bonus " + i));
        }

        return bonusList;
    }


    @Override
    public void onNoteClick(int position) {
    }


}
