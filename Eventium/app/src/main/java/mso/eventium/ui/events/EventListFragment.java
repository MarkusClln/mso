package mso.eventium.ui.events;


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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionInflater;

import java.util.ArrayList;
import java.util.List;

import mso.eventium.R;
import mso.eventium.model.Event;


public class EventListFragment extends Fragment implements RVAdapter.OnNoteListener, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView mRecyclerView;
    public RVAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View root;
    private View eventFragment;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public CharSequence search = "";


    public static EventListFragment newInstance(String someParams) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString("ParamName", someParams);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initializeData();
        root = inflater.inflate(R.layout.fragment_event_list, container, false);
        //eventFragment = inflater.inflate(R.layout.fragment_event, container, false);


        mRecyclerView = root.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(root.getContext());
        mAdapter = new RVAdapter(getContext(), EventModels, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        // SwipeRefreshLayout
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                loadRecyclerViewData();
            }
        });


        return root;


    }


    private List<Event> EventModels;

    private int times = 0;

    private void initializeData() {
        String des1 = "Gefallen eigentum schuppen so ei feinheit. Gegen er kinde kenne mu se. Im zu sauber labsal werden en heraus sterne mu. Trostlos der das streckte gefallts ins tag begierig. Gebrauch eleonora horchend gedanken als ich befehlen. Geschirr manchmal an spateren hinunter es sichtbar er ri einander. Herkommen betrubtes einfacher es so am kreiselnd verwegene schnupfen.";
        String des2 = "Lief die wege als wohl. Tun zog angenommen nun dienstmagd mancherlei federdecke hat augenblick wohnzimmer. Schwemmen verweilen zufrieden stadtchen in zu wo. Erisch spinat gut mochte karten spahte stille ich. Te wu eines ja warum lobte gehen. Flecken lacheln bei schoner anblick mut beschlo sie. Luften keinem la he sterne es. In em sorgen besuch es kissen stille. In schlafen lampchen verlohnt feinheit sichtbar ab vorliebe.";
        String des3 = "Te braunen pa am kapelle gerbers zu heruber. He am meisten bessern steigst kriegen da. Sterne keinen allein ihr des. Naturlich getrunken ist alt hin schwachem kam grundlich. Tadelte lebhaft aus niemand spielen nah konnten. Ten mut mehrere heiland nachtun brummte bereits. Sei von tun der vergnugen schreibet vogelnest. Heftig da fragen feinen durren la erregt mi. Konnte ins ich soviel schade fallen lassen.";
        String des4 = "In la ausdenken fu ertastete sorglosen am filzhutes schwemmen. Im vollends hinabsah gebogene funkelte du en irgendwo. Als vor sagst ferne ihn kinde spiel durch. Lieb tust ubel gar alt froh. Harmlos kleines offnung da heiland in spiegel anderen la wu. Sah geheimnis schonheit furchtete gar magdebett tanzmusik zufrieden. Roten litze krank abend sag die denkt seine. Ordentlich bei getunchten leuchtturm auf geschlafen geschwatzt und. Und messingnen handarbeit der hinstellte ihr uberwunden ich.";
        EventModels = new ArrayList<>();
        EventModels.add(new Event("Loaded: " + times++, des1, "24.12.1993", "12:00", "distance: 1km", R.drawable.img_disco, R.drawable.ic_best_choice));
        EventModels.add(new Event("Das zweite Event", des2, "23.01.2019", "24:00", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails));
        EventModels.add(new Event("kurzer Name", des3, "07.03.2020", "12:00", "distance: 300m", R.drawable.img_disco, R.drawable.ic_flaschen));
        EventModels.add(new Event("Das ist ein Event mit einem sehr langen Namen", des4, "24.12.1993", "12:00", "distance: 1km", R.drawable.img_drink, R.drawable.ic_best_choice));
        EventModels.add(new Event("Das  Event", des1, "23.01.2019", "12:00", "distance: 1km", R.drawable.img_disco, R.drawable.ic_cocktails));
        EventModels.add(new Event("Das  Event", des2, "07.03.2020", "12:00", "distance: 1km", R.drawable.img_drink, R.drawable.ic_flaschen));
        EventModels.add(new Event("Das  Event", des3, "24.12.1993", "12:00", "distance: 1km", R.drawable.img_disco, R.drawable.ic_best_choice));
        EventModels.add(new Event("Das  Event", des4, "23.01.2019", "12:00", "distance: 1km", R.drawable.img_drink, R.drawable.ic_cocktails));


    }


    @Override
    public void onNoteClick(int position) {
        transitionActivity(position);
    }


    private void transitionActivity(int position) {
        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.slide_bottom));

        Event EventModel = EventModels.get(position);


        String transitionName = "transitionName" + position;
        String transitionIcon = "transitionIcon" + position;
        String transitionDescription = "transitionDescription" + position;


        TextView mViewName = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.event_name);
        TextView mViewDescription = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.event_description);
        ImageView mViewIcon = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.event_icon);


        mViewName.setTransitionName(transitionName);
        mViewDescription.setTransitionName(transitionDescription);
        mViewIcon.setTransitionName(transitionIcon);


        Intent intent = new Intent(getContext(), ActivityEventDetail.class);
        intent.putExtra(ActivityEventDetail.ARG_EVENT_NAME, EventModel.getEvent_name());
        intent.putExtra(ActivityEventDetail.ARG_EVENT_DESCRIPTION, EventModel.getEvent_description());
        intent.putExtra(ActivityEventDetail.ARG_EVENT_ICON, EventModel.getEvent_icon());
        intent.putExtra(ActivityEventDetail.ARG_EVENT_PHOTO, EventModel.getEvent_photo());
        intent.putExtra(ActivityEventDetail.ARG_EVENT_DATE, EventModel.getEvent_date());
        intent.putExtra(ActivityEventDetail.ARG_EVENT_TIME, EventModel.getEvent_time());
        intent.putExtra(ActivityEventDetail.ARG_TRANSITION_EVENT_NAME, transitionName);
        intent.putExtra(ActivityEventDetail.ARG_TRANSITION_EVENT_ICON, transitionIcon);
        intent.putExtra(ActivityEventDetail.ARG_TRANSITION_EVENT_DESCRIPTION, transitionDescription);

        Pair<View, String> p1 = Pair.create((View) mViewName, transitionName);
        Pair<View, String> p2 = Pair.create((View) mViewIcon, transitionIcon);
        Pair<View, String> p3 = Pair.create((View) mViewDescription, transitionDescription);


        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this.getActivity(), p1, p2, p3);
        startActivity(intent, options.toBundle());


    }


    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }


    private void loadRecyclerViewData() {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        initializeData();

        mAdapter = new RVAdapter(getContext(), EventModels, this);
        mAdapter.getFilter().filter(search);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);

        //for requests look at
        //https://stackoverflow.com/questions/44454797/pull-to-refresh-recyclerview-android
        // :-)

    }
}

