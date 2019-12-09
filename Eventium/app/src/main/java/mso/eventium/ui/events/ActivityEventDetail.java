package mso.eventium.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.client.backendClient;


public class ActivityEventDetail extends AppCompatActivity {

    public static final String ARG_EVENT_NAME = "name";
    public static final String ARG_EVENT_DESCRIPTION = "description";
    public static final String ARG_EVENT_DATE = "date";
    public static final String ARG_EVENT_ICON = "icon";
    public static final String ARG_EVENT_PHOTO = "photo";
    public static final String ARG_EVENT_PIN_ID = "pin_id";
    public static final String ARG_EVENT_DISTANCE = "distance";

    public static final String ARG_TRANSITION_EVENT_ICON = "transitionIcon";
    public static final String ARG_TRANSITION_EVENT_NAME = "transitionName";

    // TODO: Rename and change types of parameters
    private String mName;
    private String mDescription;
    private String mDate;
    private String pin_id;

    private String mDistance;
    private int mIcon;
    private int mPhoto;
    private String mTransitionIcon;
    private String mTransitionName;

    private MapView mMapView;
    private GoogleMap googleMap;
    private String ip;
    public backendClient bc;
    public RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ip = getResources().getString(R.string.IP_Server);
        bc = new backendClient(ip);
        queue = Volley.newRequestQueue(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = getIntent().getStringExtra(ARG_EVENT_NAME);
        mDescription = getIntent().getStringExtra(ARG_EVENT_DESCRIPTION);
        mDate = getIntent().getStringExtra(ARG_EVENT_DATE);
        pin_id = getIntent().getStringExtra(ARG_EVENT_PIN_ID);
        mDistance = getIntent().getStringExtra(ARG_EVENT_DISTANCE);

        //Todo Change default Value to default Icon and Image
        mIcon = getIntent().getIntExtra(ARG_EVENT_ICON, 0);
        mPhoto = getIntent().getIntExtra(ARG_EVENT_PHOTO, 0);

        mTransitionIcon = getIntent().getStringExtra(ARG_TRANSITION_EVENT_ICON);
        mTransitionName = getIntent().getStringExtra(ARG_TRANSITION_EVENT_NAME);


        setContentView(R.layout.activity_event_detail);

        TextView mNameView;
        TextView mDescriptionView;
        TextView mDateView;
        TextView mTimeView;
        TextView mDistanceView;
        ImageView mIconView;
        ImageView mPhotoView;

        mNameView = findViewById(R.id.event_name_desc);
        mDescriptionView = findViewById(R.id.event_description);
        mDateView = findViewById(R.id.event_date);
        mTimeView = findViewById(R.id.event_time);
        mDistanceView = findViewById(R.id.event_distance);
        mIconView = findViewById(R.id.event_icon);
        mPhotoView = findViewById(R.id.event_photo);

        mNameView.setText(mName);
        mDescriptionView.setText(mDescription);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            final Calendar myCalendar = Calendar.getInstance();
            Date date = format.parse(mDate);
            myCalendar.setTime(date);
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.GERMANY);
            mDateView.setText(sdf1.format(myCalendar.getTime()));
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
            mTimeView.setText(sdf2.format(myCalendar.getTime()));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDistanceView.setText(mDistance);
        mIconView.setImageResource(mIcon);
        mPhotoView.setImageResource(mPhoto);


        mNameView.setTransitionName(mTransitionName);
        mIconView.setTransitionName(mTransitionIcon);


        mPhotoView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_event_detail_photo));


        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getPin(pin_id);

    }

    public void backToMapActivity(LatLng arg0) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("intentFragment", "mapFragment");
        intent.putExtra("location_lat", arg0.latitude);
        intent.putExtra("location_lng", arg0.longitude);
        startActivity(intent);
        finishAffinity();
    }

    public void getPin(String pin_id){
        Response.Listener rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject o = new JSONObject(s);

                    JSONArray coordinates =o.getJSONObject("location").getJSONArray("coordinates");
                    LatLng pos = new LatLng(coordinates.getDouble(0), coordinates.getDouble(1));

                    setupMap(pos);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Stopping swipe refresh
            }

        };


        StringRequest req1 = bc.getPin(pin_id, rl);
        queue.add(req1);
    }

    public void setupMap(final LatLng pos){
        mMapView.getMapAsync(new OnMapReadyCallback() {
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
                        backToMapActivity(pos);
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
