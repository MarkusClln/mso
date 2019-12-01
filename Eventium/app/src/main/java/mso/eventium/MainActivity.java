package mso.eventium;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Dialog;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;

//
//import com.auth0.android.Auth0;
//import com.auth0.android.Auth0Exception;
//import com.auth0.android.authentication.AuthenticationException;
//import com.auth0.android.provider.AuthCallback;
//import com.auth0.android.provider.VoidCallback;
//import com.auth0.android.provider.WebAuthProvider;
//import com.auth0.android.result.Credentials;
import com.auth0.android.Auth0;
import com.auth0.android.Auth0Exception;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.VoidCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import mso.eventium.ui.events.EventFragment;
import mso.eventium.ui.host.FavoriteHostsFragment;
import mso.eventium.ui.map.MapFragment;
import mso.eventium.ui.user.UserFragment;


public class MainActivity extends AppCompatActivity {

    enum ActiveFragments
    {
        MAP, EVENTS, USER, HOSTS
    }
    //Activity starts with this set fragment
    public ActiveFragments activeFragment = ActiveFragments.EVENTS;

    public boolean login = true;
    private FloatingActionButton floatingActionButton;
    private BottomAppBar bottomAppBar;
    private MaterialButton accountButton;
    private MaterialButton favoriteHostsButton;
    private Auth0 auth0;


    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setupAuth0();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActivityCompat.requestPermissions(this, INITIAL_PERMS, 1331);

        setContentView(R.layout.activity_main);

        setFragmentView();

        createFloatingActionButton();
        createBottomNavBarButtons();



    }

    private void setFragmentView(){
        String intentFragment = getIntent().getStringExtra("intentFragment");

        if(intentFragment != null){
            switch (intentFragment){
                case "mapFragment":
                    activeFragment = ActiveFragments.MAP;

                    break;
                case "eventsFragment":
                    activeFragment = ActiveFragments.EVENTS;
                    break;
                case "userFragment":
                    activeFragment = ActiveFragments.USER;
                    break;
                case "hostsFragment":
                    activeFragment = ActiveFragments.HOSTS;
                    break;
            }
            getIntent().removeExtra("intentFragment");
        }

        if(activeFragment == ActiveFragments.EVENTS){
            EventFragment eventFragment = new EventFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, eventFragment);
            fragmentTransaction.commit();
            floatingActionButton = findViewById(R.id.fab);
            floatingActionButton.setImageResource(R.drawable.ic_map_white_24dp);
        }
        if(activeFragment == ActiveFragments.MAP) {

            double lat = getIntent().getDoubleExtra("location_lat", -1);
            double lng = getIntent().getDoubleExtra("location_lng", -1);
            getIntent().removeExtra("location_lat");
            getIntent().removeExtra("location_lng");


            MapFragment mapFragment = MapFragment.newInstance(lat, lng);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, mapFragment);
            fragmentTransaction.commit();
            floatingActionButton = findViewById(R.id.fab);
            floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);

        }
        if(activeFragment == ActiveFragments.USER) {

            UserFragment userFragment = new UserFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, userFragment);
            fragmentTransaction.commit();
            floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);

        }
        if(activeFragment == ActiveFragments.HOSTS){
            FavoriteHostsFragment favoriteHostsFragment = new FavoriteHostsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, favoriteHostsFragment);
            fragmentTransaction.commit();
            floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);
        }



    }

    private void createFloatingActionButton(){
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(activeFragment==ActiveFragments.EVENTS){
                    activeFragment = ActiveFragments.MAP;
                }else{
                    activeFragment = ActiveFragments.EVENTS;
                }
                setFragmentView();


            }
        });
    }

    private void createBottomNavBarButtons(){
        accountButton = findViewById(R.id.fourth_menu_item);

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFragment = ActiveFragments.USER;
                setFragmentView();

            }
        });

        favoriteHostsButton = findViewById(R.id.second_menu_item);
        favoriteHostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFragment = ActiveFragments.HOSTS;
                setFragmentView();
            }
        });
        bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);
    }

    private void setupAuth0(){
        auth0 = new Auth0(this);
        auth0.setOIDCConformant(true);
    }

    public void login() {

        WebAuthProvider.login(auth0)
                .withScheme("demo")
                .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
                .start(this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull final Dialog dialog) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }

                        });
                    }

                    @Override
                    public void onFailure(final AuthenticationException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(@NonNull final Credentials credentials) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                TextView textView = findViewById(R.id.credentials);
                                textView.setText(credentials.getAccessToken());
                                Button loginButton = findViewById(R.id.logout);
                                loginButton.setText("Logout");
                                login = false;

                            }
                        });
                    }
                });

    }

    public void logout() {

        WebAuthProvider.logout(auth0)
                .withScheme("demo")
                .start(this, new VoidCallback() {
                    @Override
                    public void onSuccess(Void payload) {

                        TextView textView = findViewById(R.id.credentials);
                        textView.setText("");
                        Button loginButton = findViewById(R.id.logout);
                        loginButton.setText("Login");
                        login = true;

                    }

                    @Override
                    public void onFailure(Auth0Exception error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
