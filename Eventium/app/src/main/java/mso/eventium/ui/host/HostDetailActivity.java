package mso.eventium.ui.host;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mso.eventium.R;

public class HostDetailActivity extends AppCompatActivity {
    public static final String ARG_HOST_NAME = "name";
    public static final String ARG_HOST_ICON = "icon";

    public static final String ARG_TRANSITION_EVENT_ICON = "transitionIcon";
    public static final String ARG_TRANSITION_EVENT_NAME = "transitionName";


    private String mName;
    private int mIcon;

    private String mTransitionIcon;
    private String mTransitionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = getIntent().getStringExtra(ARG_HOST_NAME);
        //mIcon = getIntent().getIntExtra(ARG_HOST_ICON,0);


        mTransitionName = getIntent().getStringExtra(ARG_TRANSITION_EVENT_NAME);
        // mTransitionIcon = getIntent().getStringExtra(ARG_TRANSITION_EVENT_ICON);

        setContentView(R.layout.activity_host_detail);

        TextView mNameView;
        //ImageView mIconView;

        mNameView = (TextView)findViewById(R.id.hostDetailName);
        //mIconView = (ImageView)findViewById(R.id.hostDetailImage);

        mNameView.setText(mName);
        //mIconView.setImageResource(mIcon);


        mNameView.setTransitionName(mTransitionName);
        //mIconView.setTransitionName(mTransitionIcon);

    }
}
