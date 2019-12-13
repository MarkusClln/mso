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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.annotation.Target;
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
    private FloatingActionButton createLocationButton;
    private boolean isCreateLocation = false;
    MarkerModel createMarker;
    LatLng reloadLocation;
    Float zoom;



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

                reloadLocation = new LatLng(googleMap.getCameraPosition().target.latitude,googleMap.getCameraPosition().target.longitude);
                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        LatLng currentLocation = new LatLng(googleMap.getCameraPosition().target.latitude,googleMap.getCameraPosition().target.longitude);
                        double distance = distFrom(currentLocation.latitude, currentLocation.longitude, reloadLocation.latitude, reloadLocation.longitude);

                        if(distance>=1000){
                            createLocationsFromBackend();
                            reloadLocation = currentLocation;
                        }

                       mClusterManager.cluster();



                    }
                });


                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

                double lat = getArguments().getDouble("lat");
                double lng = getArguments().getDouble("lng");

                if (lat != -1 && lng != -1) {
                    LatLng pos = new LatLng(lat, lng);
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

                setUpOnMapClick();
            }
        });




        setUpSearchBar();
        setUpFAB();

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

        if(isCreateLocation){

        }


        return false;
    }

    private void setUpCluster() {

        mClusterManager = new ClusterManager<MarkerModel>(this.getContext(), googleMap){

        };

        googleMap.setOnCameraIdleListener(mClusterManager);
       // googleMap.setOnMarkerClickListener(mClusterManager);

    }

    private void addItems(double lat, double lng, String title, String snippet) {

        MarkerModel offsetItem = new MarkerModel(lat, lng, title, snippet);
        mClusterManager.addItem(offsetItem);
        mClusterManager.cluster();
    }

    private void setUpFAB(){
        createLocationButton = root.findViewById(R.id.createLocationFAB);
        createLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCreateLocation){
                    isCreateLocation = false;
                    createLocationButton.setImageResource(R.drawable.ic_location_on_blue_24dp);
                    createLocationsFromBackend();
                }else{
                    isCreateLocation = true;
                    createLocationButton.setImageResource(R.drawable.ic_close_blue_24dp);
                    mClusterManager.clearItems();
                    mClusterManager.cluster();
                }

            }
        });

    }

    private void setUpOnMapClick(){

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                if(isCreateLocation){
                    // Creating a marker
                    if(createMarker!= null){
                        mClusterManager.removeItem(createMarker);
                    }
                    createMarker = new MarkerModel(latLng.latitude, latLng.longitude, "", "");
                    mClusterManager.addItem(createMarker);
                    mClusterManager.cluster();

                }

            }
        });
    }

    private void createLocationsFromBackend(){
        Response.Listener rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                googleMap.clear();
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
        if(!isCreateLocation){
            Double currentLat = googleMap.getCameraPosition().target.latitude;
            Double currentLng = googleMap.getCameraPosition().target.longitude;
            StringRequest req1 = ((MainActivity) getActivity()).backendClient.getAllPins(currentLat, currentLng,10000, rl, el);
            ((MainActivity) getActivity()).queue.add(req1);
        }
    }

    private void setUpSearchBar(){
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
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (float) (earthRadius * c);

        return dist;
    }

}