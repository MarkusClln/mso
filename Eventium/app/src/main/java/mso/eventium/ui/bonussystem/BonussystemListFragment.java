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
        bonusModels = getDummyData(this.listType.equals(ListTypeEnum.USED));
        //eventModels.add(new Event("Event1_1", "","", "23.01.2019", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails, ""));
        mRecyclerView = root.findViewById(R.id.bonusRv);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new BonussystemAdapter(getContext(), bonusModels, this.listType, this);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(root.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    public ArrayList<Bonus> getDummyData(boolean isUsed) {
        ArrayList<Bonus> bonusList = new ArrayList<Bonus>();
        if(isUsed){
            bonusList.add(new Bonus("Holiday On Ice", "Freier Eintritt","", "07.02.2020"));
            bonusList.add(new Bonus("Eventium", "1 Event promoten","", "01.02.2020"));
        }
        else{
            bonusList.add(new Bonus("Evita", "5€ Rabatt an der Abendkasse","3000", ""));
            bonusList.add(new Bonus("TWIZE", "1 Freigetränk","2000", ""));
            bonusList.add(new Bonus("All Night Long", "1 Gratis Shot","2000", ""));
            bonusList.add(new Bonus("Eventium", "App in der Farbe Rot","1500", ""));
            bonusList.add(new Bonus("Eventium", "App in der Farbe Grün","1500", ""));
            bonusList.add(new Bonus("Eventium", "App in der Farbe Gelb","1500", ""));
        }




        return bonusList;
    }


    @Override
    public void onNoteClick(int position) {
    }


}
