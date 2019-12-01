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
import mso.eventium.ui.map.MapFragment;
import mso.eventium.ui.user.UserFragment;


public class MainActivity extends AppCompatActivity {

    private Auth0 auth0;
    public static final String EXTRA_CLEAR_CREDENTIALS = "com.auth0.CLEAR_CREDENTIALS";
    public static final String EXTRA_ACCESS_TOKEN = "com.auth0.ACCESS_TOKEN";
    public boolean login = true;
    public double lng = -1;
    public double lat = -1;
    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;
    MaterialButton accountButton;

    boolean isMapActive = true;

    private View mContentView;


    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //part for Auth0
        auth0 = new Auth0(this);
        auth0.setOIDCConformant(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActivityCompat.requestPermissions(this, INITIAL_PERMS, 1331);


        setContentView(R.layout.activity_main);


/*
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_map, R.id.navigation_event, R.id.navigation_user).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(navView, navController);
*/


        //We only need this, if someone clicks on the map in eventDetails
        double lat = getIntent().getDoubleExtra("location_lat", -1);
        double lng = getIntent().getDoubleExtra("location_lng", -1);
        if (lat != -1 && lng != -1) {

            this.lat = lat;
            this.lng = lng;
            MapFragment mapFragment = new MapFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, mapFragment);
            fragmentTransaction.commit();
            isMapActive = true;
            floatingActionButton = findViewById(R.id.fab);
            floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);

        }else {
            EventFragment eventFragment = (EventFragment) getSupportFragmentManager().findFragmentByTag("LIST");
            eventFragment = new EventFragment();


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, eventFragment);

            fragmentTransaction.commit();
            isMapActive = false;

        }
        floatingActionButton = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottomAppBar);


        //main line for setting menu in bottom app bar
        setSupportActionBar(bottomAppBar);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                if (!isMapActive) {

                    floatingActionButton.setImageDrawable(getDrawable(R.drawable.test));
                    AnimatedVectorDrawable animation = (AnimatedVectorDrawable)floatingActionButton.getDrawable();
                    animation.start();
                    //floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);

                    MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("MAP");
                    if (mapFragment == null) {
                        mapFragment = new MapFragment();
                        fragmentTransaction.replace(R.id.nav_host_fragment, mapFragment, "MAP");
                        fragmentTransaction.commit();

                    } else {
                        getSupportFragmentManager().popBackStackImmediate("MAP", 0);
                    }


                    isMapActive = true;
                } else {
                    floatingActionButton.setImageResource(R.drawable.ic_map_white_24dp);
                    EventFragment eventFragment = (EventFragment) getSupportFragmentManager().findFragmentByTag("LIST");
                    if (eventFragment == null) {
                        eventFragment = new EventFragment();

                        fragmentTransaction.replace(R.id.nav_host_fragment, eventFragment);
                        fragmentTransaction.commit();
                        isMapActive = false;
                    } else {
                        getSupportFragmentManager().popBackStackImmediate("LIST", 0);
                    }


                }


            }
        });


        accountButton = findViewById(R.id.fourth_menu_item);

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFragment userFragment = new UserFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, userFragment);
                fragmentTransaction.commit();
                isMapActive = true;
                floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);

            }
        });

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
