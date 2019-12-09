package mso.eventium.ui.map;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.model.Event;
import mso.eventium.ui.events.EventListFragment;
import mso.eventium.ui.events.RVAdapter;

public class EventiumMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private SupportMapFragment fragment;
    private GoogleMap googleMap;

    public static EventiumMapFragment newInstance(double lat, double lng) {
        final EventiumMapFragment eventiumMapFragment = new EventiumMapFragment();

        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        eventiumMapFragment.setArguments(args);

        return eventiumMapFragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_map, container, false);

        final FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.locationMap);
        if (fragment == null) {

            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.locationMap, fragment).commit();
        }

            fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map

                Response.Listener rl = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONArray array = new JSONArray(s);

                            for(int i=0; i<array.length(); i++)
                            {
                                JSONObject o = array.getJSONObject(i);

                                JSONArray coordinates =o.getJSONObject("location").getJSONArray("coordinates");
                                int events =o.getJSONArray("events").length();
                                LatLng pos = new LatLng(coordinates.getDouble(0), coordinates.getDouble(1));

                                googleMap.addMarker(new MarkerOptions().position(pos).title(o.getString("name")).snippet(events+" Events"));


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                };



                StringRequest req1 = ((MainActivity) getActivity()).bc.getAllPins(49.466633, 8.259154,100, rl);
                ((MainActivity) getActivity()).queue.add(req1);


                LatLng lu = new LatLng(49.477409, 8.445180);


                double lat = getArguments().getDouble("lat");
                double lng = getArguments().getDouble("lng");


                if (lat != -1 && lng != -1) {
                    LatLng pos = new LatLng(lat, lng);
                    //googleMap.addMarker(new MarkerOptions().position(pos).title("Your Event").snippet("zoom after trans"));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(pos).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                } else {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(lu).zoom(13).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    googleMap.setOnMarkerClickListener(EventiumMapFragment.this);
                }


                createLocation(lu, "Ludwigshafen");

            }
        });

        Places.initialize(root.getContext(), "AIzaSyDHsNp7dZiPYx4fhBm_bU_8R15zGUziJqg");
        PlacesClient placesClient = Places.createClient(root.getContext());

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                fm.findFragmentById(R.id.place_autocomplete);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(root.getContext(), place.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(root.getContext(), "An error occurred: " + status, Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }



    private void createLocation(LatLng location, String title) {
        googleMap.addMarker(new MarkerOptions().position(location)
                .title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

    }

    private final Handler mHandler = new Handler();
    private Runnable mAnimation;


    @Override
    public boolean onMarkerClick(final Marker marker) {
        // This causes the marker at Perth to bounce into position when it is clicked.
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500L;

        // Cancels the previous animation
        mHandler.removeCallbacks(mAnimation);

        // Starts the bounce animation
        mAnimation = new BounceAnimation(start, duration, marker, mHandler);
        mHandler.post(mAnimation);
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private static class BounceAnimation implements Runnable {

        private final long mStart, mDuration;
        private final Interpolator mInterpolator;
        private final Marker mMarker;
        private final Handler mHandler;

        private BounceAnimation(long start, long duration, Marker marker, Handler handler) {
            mStart = start;
            mDuration = duration;
            mMarker = marker;
            mHandler = handler;
            mInterpolator = new BounceInterpolator();
        }

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - mStart;
            float t = Math.max(1 - mInterpolator.getInterpolation((float) elapsed / mDuration), 0f);
            mMarker.setAnchor(0.5f, 1.0f + 1.2f * t);

            if (t > 0.0) {
                // Post again 16ms later.
                mHandler.postDelayed(this, 16L);
            }
        }
    }
}