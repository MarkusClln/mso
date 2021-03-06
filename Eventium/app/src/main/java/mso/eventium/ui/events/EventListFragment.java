package mso.eventium.ui.events;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.datastorage.BackendService;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import mso.eventium.model.Event;
import mso.eventium.ui.events.detail.EventDetailActivity;
import retrofit2.Call;
import retrofit2.Callback;


public class EventListFragment extends Fragment implements RVAdapter.OnNoteListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String LIST_TYPE_ARG_NAME = "listType";
    public static final String TRANSITION_FOR_NAME = "transitionName";
    public static final String TRANSITION_FOR_ICON = "transitionIcon";

    public RVAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Event> eventModels;
    private ListTypeEnum listType;
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

        final View root = inflater.inflate(R.layout.fragment_event_list, container, false);

        this.listType = ListTypeEnum.valueOf(getArguments().getString(LIST_TYPE_ARG_NAME));
        mRecyclerView = root.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);

        eventModels = new ArrayList<>();
        mAdapter = new RVAdapter(getContext(), eventModels, currentLocation, listType, this);

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

        setUpCurrentLocation();

        return root;
    }


    @Override
    public void onNoteClick(int position) {
        transitionActivity(position);
    }


    private void transitionActivity(int position) {
        final TextView mViewName = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.event_name);
        final ImageView mViewIcon = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.event_icon);

        Pair<View, String> p1 = Pair.create(mViewName, TRANSITION_FOR_NAME);
        Pair<View, String> p2 = Pair.create(mViewIcon, TRANSITION_FOR_ICON);

        final ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this.getActivity(), p1, p2);

        final Intent intent = new Intent(getContext(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.ARG_EVENT_ID, mAdapter.getFilteredEvents().get(position).getEvent_id());

        startActivity(intent, options.toBundle());
    }


    @Override
    public void onStart() {
        // Fetching data from server
        loadRecyclerViewData();

        super.onStart();
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }


    private void loadRecyclerViewData() {
        System.out.println("Current location: " + currentLocation);

        final String token = ((MainActivity) getActivity()).getToken();

        if (currentLocation != null) {
            switch (listType) {
                case ALL:
                    allEvents();
                    break;
                case FAVORITED:
                    if (token != null) {
                        favoritedEvents(token);
                    } else {
                        //Todo logge dich ein Bild
                    }

                    break;
                case OWNED:
                    if (token != null) {
                        ownedEvents(token);
                    } else {
                        //Todo logge dich ein Bild
                    }
                    break;
                default:
                    //error or something
            }
        }

        //for requests look at
        //https://stackoverflow.com/questions/44454797/pull-to-refresh-recyclerview-android
        // :-)

    }

    private void allEvents() {
        final Call<List<EventEntity>> events = BackendService.getInstance(getContext()).getAllEvents(currentLocation.getLatitude(), currentLocation.getLongitude(), 10000); //TODO hardcoded distance
        events.enqueue(getHandlePinCallback(null));
    }

    private void favoritedEvents(String token) {
        final Call<List<EventEntity>> events = BackendService.getInstance(getContext()).getFavoritedEvents(token);
        events.enqueue(getHandlePinCallback(token));
    }


    private void ownedEvents(String token) {
        final Call<List<EventEntity>> events = BackendService.getInstance(getContext()).getCreatedEvents(token);
        events.enqueue(getHandlePinCallback(token));
    }


    private Callback<List<EventEntity>> getHandlePinCallback(String token) {
        mSwipeRefreshLayout.setRefreshing(true);
        return new Callback<List<EventEntity>>() {
            @Override
            public void onResponse(Call<List<EventEntity>> call, retrofit2.Response<List<EventEntity>> response) {

                eventModels = new ArrayList<>();

                if(response.body() != null){
                    for (EventEntity eventEntity : response.body()) {

                        try {
                            boolean liked = eventEntity.getUsersThatLiked() != null ? eventEntity.getUsersThatLiked().contains(token) : false;
                            boolean disliked = eventEntity.getUsersThatDisliked() != null ? eventEntity.getUsersThatDisliked().contains(token) : false;

                            int countOfUsersThatLiked = eventEntity.getUsersThatLiked() != null ? eventEntity.getUsersThatLiked().size() : 0;
                            int countOfUsersThatDisliked = eventEntity.getUsersThatDisliked() != null ? eventEntity.getUsersThatDisliked().size() : 0;
                            int points = countOfUsersThatLiked - countOfUsersThatDisliked;

                            final Event item = new Event(
                                    eventEntity.getName(),
                                    eventEntity.getDescription(),
                                    eventEntity.getShortDescription(),
                                    eventEntity.getDate(),
                                    "",
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


                mAdapter = new RVAdapter(getContext(), eventModels, currentLocation,listType,EventListFragment.this);
                mRecyclerView.setAdapter(mAdapter);

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<EventEntity>> call, Throwable t) {
                Toast.makeText(getContext(), "Oops. Timeout error!", Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
                if (mAdapter.getItemCount() == 0) {
                    mRecyclerView.setBackgroundResource(R.drawable.no_connection);
                }
                Log.e("BACKEND", "ERROR", t);
                Log.e("BACKEND", "CALL " + call.request().toString());
            }
        };
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
        ALL, FAVORITED, OWNED;
    }
}

