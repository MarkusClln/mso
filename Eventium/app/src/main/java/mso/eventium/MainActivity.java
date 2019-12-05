package mso.eventium;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.Auth0;
import com.auth0.android.Auth0Exception;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.VoidCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mso.eventium.client.backendClient;
import mso.eventium.ui.events.EventFragment;
import mso.eventium.ui.host.FavoriteHostsFragment;
import mso.eventium.ui.map.EventiumMapFragment;
import mso.eventium.ui.user.UserFragment;

//
//import com.auth0.android.Auth0;
//import com.auth0.android.Auth0Exception;
//import com.auth0.android.authentication.AuthenticationException;
//import com.auth0.android.provider.AuthCallback;
//import com.auth0.android.provider.VoidCallback;
//import com.auth0.android.provider.WebAuthProvider;
//import com.auth0.android.result.Credentials;


public class MainActivity extends AppCompatActivity {

    private enum ActiveFragments {
        MAP, EVENTS, USER, HOSTS
    }

    //Activity starts with this set fragment
    public ActiveFragments activeFragment = ActiveFragments.EVENTS;
    private String ip;
    public backendClient bc;
    public boolean login;
    private FloatingActionButton floatingActionButton;
    private BottomAppBar bottomAppBar;
    private MaterialButton accountButton;
    private MaterialButton favoriteHostsButton;
    private Auth0 auth0;
    private String token;
    private SharedPreferences prefs;
    private AuthenticationAPIClient authenticationAPIClient;
    public RequestQueue queue;


    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences("mso.eventium", Context.MODE_PRIVATE);

        ip = getResources().getString(R.string.IP_Server);
        bc = new backendClient(ip);
        setupAuth0();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActivityCompat.requestPermissions(this, INITIAL_PERMS, 1331);

        setContentView(R.layout.activity_main);

        setFragmentView();

        createFloatingActionButton();
        createBottomNavBarButtons();
        queue = Volley.newRequestQueue(this);


    }

    private void setFragmentView() {
        String intentFragment = getIntent().getStringExtra("intentFragment");

        if (intentFragment != null) {
            switch (intentFragment) {
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

        if (activeFragment == ActiveFragments.EVENTS) {
            EventFragment eventFragment = new EventFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, eventFragment);
            fragmentTransaction.commit();
            floatingActionButton = findViewById(R.id.fab);
            floatingActionButton.setImageResource(R.drawable.ic_map_white_24dp);
        }
        if (activeFragment == ActiveFragments.MAP) {

            double lat = getIntent().getDoubleExtra("location_lat", -1);
            double lng = getIntent().getDoubleExtra("location_lng", -1);
            getIntent().removeExtra("location_lat");
            getIntent().removeExtra("location_lng");


            EventiumMapFragment mapFragment = EventiumMapFragment.newInstance(lat, lng);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, mapFragment);
            fragmentTransaction.commit();
            floatingActionButton = findViewById(R.id.fab);
            floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);

        }
        if (activeFragment == ActiveFragments.USER) {

            UserFragment userFragment = UserFragment.newInstance(token);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, userFragment);
            fragmentTransaction.commit();
            floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);


        }
        if (activeFragment == ActiveFragments.HOSTS) {
            FavoriteHostsFragment favoriteHostsFragment = new FavoriteHostsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, favoriteHostsFragment);
            fragmentTransaction.commit();
            floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);
        }


    }

    private void createFloatingActionButton() {
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activeFragment == ActiveFragments.EVENTS) {
                    activeFragment = ActiveFragments.MAP;
                } else {
                    activeFragment = ActiveFragments.EVENTS;
                }
                setFragmentView();


            }
        });
    }

    private void createBottomNavBarButtons() {
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

    private void setupAuth0() {
        auth0 = new Auth0(this);
        auth0.setOIDCConformant(true);
        authenticationAPIClient = new AuthenticationAPIClient(auth0);

        token = prefs.getString("token", null);
        if(token!=null){
            login=false;
        }else{
            login=true;
        }

    }

    public void login() {

        WebAuthProvider.login(auth0)
                .withScheme("demo").withAudience("https://mso-api")
                .withScope("openid profile email offline_access read:current_user update:current_user_metadata")
                //.withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
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
                                token = credentials.getAccessToken();
                                System.out.println(credentials.getIdToken());
                                Button loginButton = findViewById(R.id.logout);
                                loginButton.setText("Logout");
                                login = false;
                                SharedPreferences.Editor mEditor = prefs.edit();
                                mEditor.putString("token", token).apply();

                                getProfile(true);



                            }
                        });
                    }
                });
    }

    public void getProfile() {
        getProfile(false);
    }

    public void getProfile(boolean createUser2) {
        final boolean createUser = createUser2;
        authenticationAPIClient.
                userInfo(token)
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(UserProfile userinfo) {
                        TextView nameTV = findViewById(R.id.userName);
                        TextView emailTV = findViewById(R.id.email);
                        nameTV.setText(userinfo.getNickname());
                        emailTV.setText(userinfo.getEmail());
                        if(createUser){
                            queue.add(bc.createUser(token,userinfo.getEmail(), userinfo.getId(), userinfo.getNickname()));
                        }

                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        // Show error
                    }
                });
    }

    public void logout() {

        WebAuthProvider.logout(auth0)
                .withScheme("demo")
                .start(this, new VoidCallback() {
                    @Override
                    public void onSuccess(Void payload) {
                        token = null;
                        login = true;
                        SharedPreferences.Editor mEditor = prefs.edit();
                        mEditor.remove("token").apply();

                    }

                    @Override
                    public void onFailure(Auth0Exception error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }


}
