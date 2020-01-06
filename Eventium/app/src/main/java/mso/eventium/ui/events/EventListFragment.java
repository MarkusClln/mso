package mso.eventium.ui.events;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionInflater;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.datastorage.BackendService;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import mso.eventium.model.Event;
import retrofit2.Call;
import retrofit2.Callback;


public class EventListFragment extends Fragment implements RVAdapter.OnNoteListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String LIST_TYPE_ARG_NAME = "listType";

    private View root;
    private RecyclerView mRecyclerView;
    public RVAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Event> eventModels;
    public ListTypeEnum listType;
    private Location currentLocation;

    public static EventListFragment newInstance(ListTypeEnum type) {
        final EventListFragment fragment = new EventListFragment();
        final Bundle args = new Bundle();
        args.putString(LIST_TYPE_ARG_NAME, type.name());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_event_list, container, false);
        //eventFragment = inflater.inflate(R.layout.fragment_event, container, false);


        this.listType = ListTypeEnum.valueOf(getArguments().getString(LIST_TYPE_ARG_NAME));

        eventModels = new ArrayList<>();
        //eventModels.add(new Event("Event1_1", "","", "23.01.2019", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails, ""));
        mRecyclerView = root.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RVAdapter(getContext(), eventModels, this);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(root.getContext());
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

        setUpCurrentLocation();

        return root;


    }


    @Override
    public void onNoteClick(int position) {
        transitionActivity(position);
    }


    private void transitionActivity(int position) {
        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.slide_bottom));

        Event EventModel = eventModels.get(position);


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
    public void onResume() {

        System.out.println("RESUME " + listType);
        super.onResume();
    }

    @Override
    public void onRefresh() {
        System.out.println("onRefresh " + listType);
        loadRecyclerViewData();
    }


    private void loadRecyclerViewData() {
        if (currentLocation != null) {
            switch (listType) {
                case ALL:
                    allEvents();
                    break;
                case SAVED:
                    if (((MainActivity) getActivity()).getToken() != null) {
                        savedEvents();
                    } else {
                        //Todo logge dich ein Bild
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    break;
                case OWNED:
                    if (((MainActivity) getActivity()).getToken() != null) {
                        ownedEvents();
                    } else {
                        //Todo logge dich ein Bild
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    break;
                default:
                    //error or something
            }
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        //for requests look at
        //https://stackoverflow.com/questions/44454797/pull-to-refresh-recyclerview-android
        // :-)

    }


    private void allEvents() {
        mSwipeRefreshLayout.setRefreshing(true);

        if (((MainActivity) getActivity()).getToken() != null) {

            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject auth0 = new JSONObject(s);
                        setEvents(auth0.getString("auth0"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener el = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Oops. Timeout error!", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mAdapter.getItemCount() == 0) {
                        mRecyclerView.setBackgroundResource(R.drawable.no_connection);
                    }
                }
            };

            StringRequest req1 = ((MainActivity) getActivity()).backendClient.getAuth(((MainActivity) getActivity()).getToken(), rl, el);
            ((MainActivity) getActivity()).queue.add(req1);
        } else {
            setEvents(null);
        }


    }

    private void setEvents(final String token) {
        final Call<List<PinEntity>> pins = BackendService.getInstance(getResources()).getAllPins(currentLocation.getLatitude(), currentLocation.getLongitude(), 10000); //TODO hardcoded distance
        pins.enqueue(new Callback<List<PinEntity>>() {
            @Override
            public void onResponse(Call<List<PinEntity>> call, retrofit2.Response<List<PinEntity>> response) {

                eventModels = new ArrayList<>();
                for (PinEntity pinEntity : response.body()) {


                    for (EventEntity eventEntity : pinEntity.getEvents()) {

                        try {
                            int distance_rounded = (int) pinEntity.getDistance();  //TODO in methode verschieben die aus EventEntity das EventViewModel macht (oder besser die TextView das es anzeigt soll was dranschreiben)
                            String distance_str = "";
                            if (distance_rounded < 1000) {
                                distance_str = distance_rounded + " m";
                            } else {
                                distance_rounded = (int) pinEntity.getDistance() / 1000;
                                distance_str = distance_rounded + " m";
                            }


                            boolean liked = eventEntity.getUsersThatLiked() != null ? eventEntity.getUsersThatLiked().contains(token) : false;
                            boolean disliked = eventEntity.getUsersThatDisliked() != null ? eventEntity.getUsersThatDisliked().contains(token) : false;

                            int countOfUsersThatLiked = eventEntity.getUsersThatLiked() != null ? eventEntity.getUsersThatLiked().size() : 0;
                            int countOfUsersThatDisliked = eventEntity.getUsersThatDisliked() != null ? eventEntity.getUsersThatDisliked().size() : 0;
                            int points = countOfUsersThatLiked - countOfUsersThatDisliked;


                            Event item = new Event(
                                    eventEntity.getName(),
                                    eventEntity.getDescription(),
                                    eventEntity.getShortDescription(),
                                    eventEntity.getDate().toString(),
                                    distance_str,
                                    R.drawable.img_drink,
                                    eventEntity.getPinId(),
                                    liked,
                                    disliked,
                                    points,
                                    eventEntity.getId(),
                                    eventEntity.getCategory()

                            );
                            eventModels.add(item);
                        } catch (NumberFormatException nfe) {
                            System.err.println("NumberFormatException: " + nfe.getMessage());
                        }
                    }
                }

                mAdapter = new RVAdapter(getContext(), eventModels, EventListFragment.this);
                mRecyclerView.setAdapter(mAdapter);

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<PinEntity>> call, Throwable t) {
                Toast.makeText(getContext(), "Oops. Timeout error!", Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
                if (mAdapter.getItemCount() == 0) {
                    mRecyclerView.setBackgroundResource(R.drawable.no_connection);
                }
            }
        });
    }

    private void savedEvents() {
        mSwipeRefreshLayout.setRefreshing(true);
        Response.Listener rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONArray events = new JSONArray(s);
                    eventModels = new ArrayList<>();

                    for (int j = 0; j < events.length(); j++) {

                        JSONObject event = events.getJSONObject(j);

                        JSONArray likedEvents = event.getJSONArray("likedUsers");
                        ArrayList<String> likedEventsAsArrayList = new ArrayList<String>();
                        if (likedEvents != null) {
                            int len = likedEvents.length();
                            for (int likedEventCounter = 0; likedEventCounter < len; likedEventCounter++) {
                                likedEventsAsArrayList.add(likedEvents.get(likedEventCounter).toString());
                            }
                        }

                        JSONArray dislikedEvents = event.getJSONArray("dislikedUsers");
                        ArrayList<String> dislikedEventsAsArrayList = new ArrayList<String>();
                        if (dislikedEvents != null) {
                            int len = dislikedEvents.length();
                            for (int dislikedEventCounter = 0; dislikedEventCounter < len; dislikedEventCounter++) {
                                dislikedEventsAsArrayList.add(dislikedEvents.get(dislikedEventCounter).toString());
                            }
                        }


                        int points = likedEventsAsArrayList.size() - dislikedEventsAsArrayList.size();

                        Event item = new Event(
                                event.getString("name"),
                                event.getString("description"),
                                event.getString("shortDescription"),
                                event.getString("date"),
                                "Fix this",
                                R.drawable.img_drink,
                                event.getString("pin_id"),
                                true,
                                false,
                                points,
                                event.getString("_id"),
                                event.getString("category")

                        );
                        eventModels.add(item);

                    }


                    mAdapter = new RVAdapter(getContext(), eventModels, EventListFragment.this);
                    mRecyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }

        };


        if (((MainActivity) getActivity()).getToken() != null) {
            StringRequest req1 = ((MainActivity) getActivity()).backendClient.getFavEvents(((MainActivity) getActivity()).getToken(), rl);
            ((MainActivity) getActivity()).queue.add(req1);
        } else {
            //Todo
            //not logged in
        }

    }

    private void ownedEvents() {
        mSwipeRefreshLayout.setRefreshing(true);
        Response.Listener rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONArray events = new JSONArray(s);
                    eventModels = new ArrayList<>();

                    for (int j = 0; j < events.length(); j++) {
                        JSONObject event = events.getJSONObject(j);

                        JSONArray likedEvents = event.getJSONArray("likedUsers");
                        ArrayList<String> likedEventsAsArrayList = new ArrayList<String>();
                        if (likedEvents != null) {
                            int len = likedEvents.length();
                            for (int likedEventCounter = 0; likedEventCounter < len; likedEventCounter++) {
                                likedEventsAsArrayList.add(likedEvents.get(likedEventCounter).toString());
                            }
                        }

                        JSONArray dislikedEvents = event.getJSONArray("dislikedUsers");
                        ArrayList<String> dislikedEventsAsArrayList = new ArrayList<String>();
                        if (dislikedEvents != null) {
                            int len = dislikedEvents.length();
                            for (int dislikedEventCounter = 0; dislikedEventCounter < len; dislikedEventCounter++) {
                                dislikedEventsAsArrayList.add(dislikedEvents.get(dislikedEventCounter).toString());
                            }
                        }


                        int points = likedEventsAsArrayList.size() - dislikedEventsAsArrayList.size();

                        Event item = new Event(
                                event.getString("name"),
                                event.getString("description"),
                                event.getString("shortDescription"),
                                event.getString("date"),
                                "Fix this",
                                R.drawable.img_drink,
                                event.getString("pin_id"),
                                false,
                                false,
                                points,
                                event.getString("_id"),
                                event.getString("category")

                        );
                        eventModels.add(item);

                    }


                    mAdapter = new RVAdapter(getContext(), eventModels, EventListFragment.this);
                    mRecyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }

        };


        if (((MainActivity) getActivity()).getToken() != null) {
            StringRequest req1 = ((MainActivity) getActivity()).backendClient.getOwnEvents(((MainActivity) getActivity()).getToken(), rl);
            ((MainActivity) getActivity()).queue.add(req1);
        } else {
            //Todo
            //not logged in
        }

    }

    private void setUpCurrentLocation() {
        final FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLocation = location;
                            loadRecyclerViewData();
                        }
                    }
                });
    }

    public enum ListTypeEnum {
        ALL, SAVED, OWNED;
    }

}

