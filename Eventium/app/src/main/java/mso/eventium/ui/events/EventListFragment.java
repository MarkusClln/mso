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

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.model.Event;


public class EventListFragment extends Fragment implements RVAdapter.OnNoteListener, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView mRecyclerView;
    public RVAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View root;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Event> EventModels;
    public CharSequence search = "";
    public String ListType;


    public static EventListFragment newInstance(String type) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString("ListType", type);
        fragment.setArguments(args);
        return fragment;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        root = inflater.inflate(R.layout.fragment_event_list, container, false);
        //eventFragment = inflater.inflate(R.layout.fragment_event, container, false);

        this.ListType = getArguments().getString("ListType");

        EventModels = new ArrayList<>();
        //EventModels.add(new Event("Event1_1", "","", "23.01.2019", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails, ""));
        mRecyclerView = root.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RVAdapter(getContext(), EventModels, this);

        mLayoutManager = new LinearLayoutManager(root.getContext());
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


        TextView mViewName = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.event_name_desc);
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
        intent.putExtra(ActivityEventDetail.ARG_EVENT_DISTANCE, EventModel.getEvent_distance());
        intent.putExtra(ActivityEventDetail.ARG_EVENT_PIN_ID, EventModel.getPin_id());
        intent.putExtra(ActivityEventDetail.ARG_TRANSITION_EVENT_NAME, transitionName);
        intent.putExtra(ActivityEventDetail.ARG_TRANSITION_EVENT_ICON, transitionIcon);


        Pair<View, String> p1 = Pair.create((View) mViewName, transitionName);
        Pair<View, String> p2 = Pair.create((View) mViewIcon, transitionIcon);



        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this.getActivity(), p1, p2);
        startActivity(intent, options.toBundle());


    }


    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }


    private void loadRecyclerViewData() {

        switch(ListType)
        {
            case "all":
                allEvents();
                break;
            case "saved":
                savedEvents();
                break;
            case "owned":
                ownedEvents();
                break;
            default:
                //error or something
        }


        //for requests look at
        //https://stackoverflow.com/questions/44454797/pull-to-refresh-recyclerview-android
        // :-)

    }


    private void allEvents(){
        mSwipeRefreshLayout.setRefreshing(true);

        if(((MainActivity) getActivity()).getToken()!= null){
            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    try {
                        JSONArray array = new JSONArray(s);
                        setEvents(array);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            StringRequest req1 = ((MainActivity) getActivity()).bc.getFavEventIDs(((MainActivity) getActivity()).getToken(), rl);
            ((MainActivity) getActivity()).queue.add(req1);
        }else{
            setEvents(null);
        }


    }

    private void setEvents(final JSONArray likedEvents){
        Response.Listener rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    ArrayList<String> likedEventsAsArrayList = new ArrayList<String>();
                    if (likedEvents != null) {
                        int len = likedEvents.length();
                        for (int i=0;i<len;i++){
                            likedEventsAsArrayList.add(likedEvents.get(i).toString());
                        }
                    }


                    JSONArray array = new JSONArray(s);
                    EventModels = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        JSONArray events = o.getJSONArray("events");

                        for (int j = 0; j < events.length(); j++) {
                            JSONObject event = events.getJSONObject(j);

                            try {
                                double distance = o.getDouble("distance");
                                int distance_rounded = (int) distance;
                                String distance_str = "";
                                if (distance_rounded < 1000) {
                                    distance_str = distance_rounded + " m";
                                } else {
                                    distance_rounded = (int) distance / 1000;
                                    distance_str = distance_rounded + " m";
                                }




                                String event_id = event.getString("_id");
                                boolean contains = likedEventsAsArrayList.contains(event_id);

                                Event item = new Event(
                                        event.getString("name"),
                                        event.getString("description"),
                                        event.getString("shortDescription"),
                                        event.getString("date"),
                                        distance_str,
                                        R.drawable.img_drink,
                                        event.getString("pin_id"),
                                        contains,
                                        event.getString("_id"),
                                        event.getString("category")

                                );
                                EventModels.add(item);
                            } catch (NumberFormatException nfe) {
                                System.err.println("NumberFormatException: " + nfe.getMessage());
                            }


                        }

                    }


                    mAdapter = new RVAdapter(getContext(), EventModels, EventListFragment.this);
                    mRecyclerView.setAdapter(mAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }

        };



        StringRequest req1 = ((MainActivity) getActivity()).bc.getAllPins(49.466633, 8.259154,1000, rl);
        ((MainActivity) getActivity()).queue.add(req1);
    }

    private void savedEvents(){
        mSwipeRefreshLayout.setRefreshing(true);
        Response.Listener rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONArray events = new JSONArray(s);
                    EventModels = new ArrayList<>();

                    for(int j=0; j<events.length(); j++)
                    {
                        JSONObject event = events.getJSONObject(j);

                        Event item = new Event(
                                event.getString("name"),
                                event.getString("description"),
                                event.getString("shortDescription"),
                                event.getString("date"),
                                "Fix this",
                                R.drawable.img_drink,
                                event.getString("pin_id"),
                                true,
                                event.getString("_id"),
                                event.getString("category")

                        );
                        EventModels.add(item);

                    }


                    mAdapter = new RVAdapter(getContext(), EventModels, EventListFragment.this);
                    mRecyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }

        };


        if(((MainActivity) getActivity()).getToken()!= null){
            StringRequest req1 = ((MainActivity) getActivity()).bc.getFavEvents(((MainActivity) getActivity()).getToken(), rl);
            ((MainActivity) getActivity()).queue.add(req1);
        }else{
            //Todo
            //not logged in
        }

    }

    private void ownedEvents(){
        mSwipeRefreshLayout.setRefreshing(true);
        Response.Listener rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONArray events = new JSONArray(s);
                    EventModels = new ArrayList<>();

                    for(int j=0; j<events.length(); j++)
                    {
                        JSONObject event = events.getJSONObject(j);

                        Event item = new Event(
                                event.getString("name"),
                                event.getString("description"),
                                event.getString("shortDescription"),
                                event.getString("date"),
                                "Fix this",
                                R.drawable.img_drink,
                                event.getString("pin_id"),
                                false,
                                event.getString("_id"),
                                event.getString("category")

                        );
                        EventModels.add(item);

                    }


                    mAdapter = new RVAdapter(getContext(), EventModels, EventListFragment.this);
                    mRecyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }

        };


        if(((MainActivity) getActivity()).getToken()!= null){
            StringRequest req1 = ((MainActivity) getActivity()).bc.getOwnEvents(((MainActivity) getActivity()).getToken(), rl);
            ((MainActivity) getActivity()).queue.add(req1);
        }else{
            //Todo
            //not logged in
        }

    }


}

