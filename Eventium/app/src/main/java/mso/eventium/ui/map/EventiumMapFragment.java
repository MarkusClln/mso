package mso.eventium.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.model.MarkerModel;

public class EventiumMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private SupportMapFragment fragment;
    private GoogleMap googleMap;
    private ClusterManager<MarkerModel> mClusterManager;
    private final Handler mHandler = new Handler();
    private Runnable mAnimation;


    GoogleApiClient mGoogleApiClient;
    View root;

    private FusedLocationProviderClient fusedLocationClient;
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

        root = inflater.inflate(R.layout.fragment_map, container, false);

        final FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.locationMap);
        if (fragment == null) {

            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.locationMap, fragment).commit();
        }


            fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                setUpCluster();

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {

                        Response.Listener rl = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                mMap.clear();
                                mClusterManager.clearItems();
                                try {
                                    JSONArray array = new JSONArray(s);

                                    for(int i=0; i<array.length(); i++)
                                    {
                                        JSONObject o = array.getJSONObject(i);

                                        JSONArray coordinates =o.getJSONObject("location").getJSONArray("coordinates");
                                        int events =o.getJSONArray("events").length();
                                        addItems(coordinates.getDouble(0), coordinates.getDouble(1),o.getString("name"), events+" Events");

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        };

                        Response.ErrorListener el =new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        };

                        Double currentLat = googleMap.getCameraPosition().target.latitude;
                        Double currentLng = googleMap.getCameraPosition().target.longitude;
                        StringRequest req1 = ((MainActivity) getActivity()).bc.getAllPins(currentLat, currentLng,10000, rl, el);
                        ((MainActivity) getActivity()).queue.add(req1);
                    }
                });


                // For dropping a marker at a point on the Map





                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());




                double lat = getArguments().getDouble("lat");
                double lng = getArguments().getDouble("lng");


                if (lat != -1 && lng != -1) {
                    LatLng pos = new LatLng(lat, lng);

                    //move map camera

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));



                } else {

                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        LatLng lu = new LatLng(location.getLatitude(), location.getLongitude());
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(lu).zoom(13).build();
                                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        googleMap.setOnMarkerClickListener(EventiumMapFragment.this);
                                    }
                                }
                            });

                }


            }
        });


        EditText locationSearch = root.findViewById(R.id.editText);

        locationSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    searchLocation();
                    return true;
                }


                return false;
            }
        });


        return root;
    }

    public void searchLocation() {
        EditText locationSearch = (EditText) getActivity().findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        if(addressList.size()>=1){
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }else{
            Toast.makeText(getContext(),location+" not found",Toast.LENGTH_LONG).show();
        }


        }
    }


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

    private void setUpCluster() {
        // Position the map.
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MarkerModel>(this.getContext(), googleMap){

        };


        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

    }

    private void addItems(double lat, double lng, String title, String snippet) {

        MarkerModel offsetItem = new MarkerModel(lat, lng, title, snippet);
        mClusterManager.addItem(offsetItem);
        mClusterManager.cluster();
    }


}