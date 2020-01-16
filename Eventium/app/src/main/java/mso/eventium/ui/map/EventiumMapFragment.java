package mso.eventium.ui.map;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.List;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.datastorage.BackendService;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import mso.eventium.helper.CommonHelper;
import mso.eventium.model.Event;
import mso.eventium.model.MarkerModel;
import retrofit2.Call;
import retrofit2.Callback;

public class EventiumMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private GoogleMap googleMap;
    private ClusterManager<MarkerModel> mClusterManager;
    private boolean createLocation = false;
    private MarkerModel createMarker;
    private LatLng reloadLocation;
    private CommonHelper helper;

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

        final View root = inflater.inflate(R.layout.fragment_map, container, false);

        setUpMap();
        setUpSearchBar(root);
        setUpFAB(root);

        this.helper = new CommonHelper();



        return root;
    }

    public void searchLocation() {
        EditText locationSearch = getActivity().findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList.size() >= 1) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            } else {
                Toast.makeText(getContext(), location + " not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (createLocation) {
//            final Intent intent = new Intent(getContext(), PinCreateDialog.class);
//            intent.putExtra(PinCreateDialog.ARG_LAT, marker.getPosition().latitude);
//            intent.putExtra(PinCreateDialog.ARG_LNG, marker.getPosition().longitude);
//            intent.putExtra(PinCreateDialog.ARG_TOKEN, ((MainActivity) getActivity()).getToken());
//            startActivity(intent);

            //set up dialog
            final PinEntity pin = new PinEntity();
            pin.setLocation(marker.getPosition());
            final PinCreateDialog dialog = new PinCreateDialog(pin, ((MainActivity) getActivity()).getToken());
            dialog.show(getFragmentManager(), null);
        }

        return false;
    }

    private void setUpMap() {
        final FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.locationMap);

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

                reloadLocation = new LatLng(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        final LatLng currentLocation = new LatLng(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                        double distance = distFrom(currentLocation.latitude, currentLocation.longitude, reloadLocation.latitude, reloadLocation.longitude);

                        if (distance >= 1000) { //only get pins if we moved out of range
                            createLocationsFromBackend();
                            reloadLocation = currentLocation;
                        }
                        mClusterManager.cluster();
                        mClusterManager.onCameraIdle();
                    }
                });


                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker marker) {

                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        // Inflate custom layout
                        View v = getLayoutInflater().inflate(R.layout.marker_view, null);
                        String title = marker.getTitle();
                        String snippet = marker.getSnippet();
                        // Set desired height and width
                        v.setLayoutParams(new RelativeLayout.LayoutParams(500, RelativeLayout.LayoutParams.WRAP_CONTENT));

                        TextView locationHeader = (TextView) v.findViewById(R.id.marker_location_header);
                        TextView events = (TextView) v.findViewById(R.id.marker_events);

                        locationHeader.setText(title);
                        events.setText(snippet);

                        return v;
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
    }

    private void setUpCluster() {

        mClusterManager = new ClusterManager<MarkerModel>(this.getContext(), googleMap);

        googleMap.setOnCameraIdleListener(mClusterManager);
        // googleMap.setOnMarkerClickListener(mClusterManager);

    }

    private void setUpFAB(View root) {
        final FloatingActionButton createLocationButton = root.findViewById(R.id.createLocationFAB);
        createLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (createLocation) {
                    createLocation = false;
                    createLocationButton.setImageResource(R.drawable.ic_location_on_blue_24dp);
                } else {
                    createLocation = true;
                    createLocationButton.setImageResource(R.drawable.ic_close_blue_24dp);
                    mClusterManager.clearItems();
                    mClusterManager.cluster();

                    Toast.makeText(getContext(), "Klicke auf die Karte um den Standort für eine Veranstaltung festzulegen. Hast du den Standort festgelegt klicke auf den Pin", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void setUpOnMapClick() {

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                if (createLocation) {
                    // Creating a marker
                    if (createMarker != null) {
                        mClusterManager.removeItem(createMarker);
                    }
                    createMarker = new MarkerModel(latLng, "", "");
                    mClusterManager.addItem(createMarker);
                    mClusterManager.cluster();

                }

            }
        });
    }

    private void createLocationsFromBackend() {
        Double currentLat = googleMap.getCameraPosition().target.latitude;
        Double currentLng = googleMap.getCameraPosition().target.longitude;

        final Call<List<PinEntity>> pins = BackendService.getInstance(getContext()).getAllPins(currentLat, currentLng, 10000);
        pins.enqueue(new Callback<List<PinEntity>>() {
            @Override
            public void onResponse(Call<List<PinEntity>> call, retrofit2.Response<List<PinEntity>> response) {
                googleMap.clear();
                mClusterManager.clearItems();

                for (PinEntity pin : response.body()) {

                    List<EventEntity> events = pin.getEvents();
                    String eventEnumeration = "";

                    for(EventEntity event : events){
                        eventEnumeration+=event.getName() +" | "+ helper.FormatDate(event.getDate().toString()) + "\n";
                    }

                    if(events.size() != 0){
                        final MarkerModel offsetItem = new MarkerModel(pin.getLocation(), pin.getName(), eventEnumeration);
                        mClusterManager.addItem(offsetItem);
                    }
                }
                mClusterManager.cluster();
                mClusterManager.onCameraIdle();

            }

            @Override
            public void onFailure(Call<List<PinEntity>> call, Throwable t) {
            }
        });
    }

    private void setUpSearchBar(View root) {
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
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (float) (earthRadius * c);

        return dist;
    }

}