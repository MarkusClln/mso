package mso.eventium.ui.events.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.ui.events.EventListFragment;

public class EventDetailFragment extends Fragment {

    private GoogleMap googleMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_event_detail, container, false);


        TextView mNameView = root.findViewById(R.id.event_name);
        TextView mDescriptionView = root.findViewById(R.id.event_description);
        TextView mDateView = root.findViewById(R.id.event_date);
        TextView mTimeView = root.findViewById(R.id.event_time);
        TextView mDistanceView = root.findViewById(R.id.event_distance);
        ImageView mIconView = root.findViewById(R.id.event_icon);
        ImageView mPhotoView = root.findViewById(R.id.event_photo);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        final Calendar myCalendar = Calendar.getInstance();


        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.GERMANY);
        mDateView.setText(sdf1.format(myCalendar.getTime()));
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
        mTimeView.setText(sdf2.format(myCalendar.getTime()));


        mIconView.setImageResource(R.drawable.ic_flaschen);
        mPhotoView.setImageResource(R.drawable.img_drink);

        mNameView.setTransitionName(EventListFragment.TRANSITION_FOR_NAME);
        mIconView.setTransitionName(EventListFragment.TRANSITION_FOR_ICON);

        mPhotoView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_event_detail_photo));


        final MapView mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final EventViewModel eventModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);

        eventModel.getEvent().observe(this, event -> {
            mNameView.setText(event.getName());
            mDescriptionView.setText(event.getDescription());
            myCalendar.setTime(event.getDate());
            mDistanceView.setText("TODO: Calc distance");

            setupMap(mapView, event.getPin().getLocation());
        });


        return root;
    }

    /**
     * Used for navigating to map with the location of the event marked
     *
     * @param arg0
     */
    public void GoToMapActivity(LatLng arg0) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("intentFragment", "mapFragment");
        intent.putExtra("location_lat", arg0.latitude);
        intent.putExtra("location_lng", arg0.longitude);
        startActivity(intent);
    }


    public void setupMap(final MapView mapView, final LatLng pos) {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map


                googleMap.addMarker(new MarkerOptions().position(pos).title("Ein Event").snippet("Hier ist das Event"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng arg0) {
                        GoToMapActivity(pos);
                    }
                });
                googleMap.getUiSettings().setAllGesturesEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(false);
                googleMap.getUiSettings().isMapToolbarEnabled();
            }
        });
    }
}
