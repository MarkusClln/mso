package mso.eventium.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mso.eventium.MainActivity;
import mso.eventium.R;


public class ActivityEventDetail extends AppCompatActivity {

    public static final String ARG_EVENT_NAME = "name";
    public static final String ARG_EVENT_DESCRIPTION = "description";
    public static final String ARG_EVENT_DATE = "date";
    public static final String ARG_EVENT_TIME = "time";
    public static final String ARG_EVENT_ICON = "icon";
    public static final String ARG_EVENT_PHOTO = "photo";

    public static final String ARG_TRANSITION_EVENT_ICON = "transitionIcon";
    public static final String ARG_TRANSITION_EVENT_NAME = "transitionName";
    public static final String ARG_TRANSITION_EVENT_DESCRIPTION = "transitionDescription";

    // TODO: Rename and change types of parameters
    private String mName;
    private String mDescription;
    private String mDate;
    private String mTime;
    private int mIcon;
    private int mPhoto;
    private String mTransitionIcon;
    private String mTransitionName;
    private String mTransitionDescription;
    MapView mMapView;
    private GoogleMap googleMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = getIntent().getStringExtra(ARG_EVENT_NAME);
        mDescription = getIntent().getStringExtra(ARG_EVENT_DESCRIPTION);
        mDate = getIntent().getStringExtra(ARG_EVENT_DATE);
        mTime = getIntent().getStringExtra(ARG_EVENT_TIME);

        //Todo Change default Value to default Icon and Image
        mIcon = getIntent().getIntExtra(ARG_EVENT_ICON,0);
        mPhoto = getIntent().getIntExtra(ARG_EVENT_PHOTO,0);

        mTransitionIcon = getIntent().getStringExtra(ARG_TRANSITION_EVENT_ICON);
        mTransitionName = getIntent().getStringExtra(ARG_TRANSITION_EVENT_NAME);
        mTransitionDescription = getIntent().getStringExtra(ARG_TRANSITION_EVENT_DESCRIPTION);


        setContentView(R.layout.activity_event_detail);

        TextView mNameView;
        TextView mDescriptionView;
        TextView mDateView;
        TextView mTimeView;
        ImageView mIconView;
        ImageView mPhotoView;

        mNameView = (TextView)findViewById(R.id.event_name);
        mDescriptionView = (TextView)findViewById(R.id.event_description);
        mDateView = (TextView)findViewById(R.id.event_date);
        mTimeView = (TextView)findViewById(R.id.event_time);
        mIconView = (ImageView)findViewById(R.id.event_icon);
        mPhotoView = (ImageView)findViewById(R.id.event_photo);

        mNameView.setText(mName);
        mDescriptionView.setText(mDescription);
        mDateView.setText(mDate);
        mTimeView.setText(mTime);
        mIconView.setImageResource(mIcon);
        mPhotoView.setImageResource(mPhoto);


        mNameView.setTransitionName(mTransitionName);
        mDescriptionView.setTransitionName(mTransitionDescription);
        mIconView.setTransitionName(mTransitionIcon);


        mPhotoView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_event_detail_photo));


        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng mannheim = new LatLng( 49.4874592, 8.4660395);

                googleMap.addMarker(new MarkerOptions().position(mannheim).title("Ein Event").snippet("Hier ist das Event"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mannheim, 15));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
                {
                    @Override
                    public void onMapClick(LatLng arg0)
                    {
                        backToMapActivity(arg0);
                    }
                });
                googleMap.getUiSettings().setAllGesturesEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(false);
                googleMap.getUiSettings().isMapToolbarEnabled();

            }
        });




    }

    public void backToMapActivity(LatLng arg0){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("location_lat", arg0.latitude);
        intent.putExtra("location_lng", arg0.longitude);
        startActivity(intent);
        finishAffinity();
    }

}
