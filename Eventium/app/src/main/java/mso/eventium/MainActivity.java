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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
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

import mso.eventium.client.BackendClient;
import mso.eventium.ui.events.EventFragment;
import mso.eventium.ui.bonussystem.BonussystemFragment;
import mso.eventium.ui.map.EventiumMapFragment;
import mso.eventium.ui.newsfeed.NewsfeedFragment;
import mso.eventium.ui.user.UserFragment;
import mso.eventium.ui.create.CreateFragment;

//
//import com.auth0.android.Auth0;
//import com.auth0.android.Auth0Exception;
//import com.auth0.android.authentication.AuthenticationException;
//import com.auth0.android.provider.AuthCallback;
//import com.auth0.android.provider.VoidCallback;
//import com.auth0.android.provider.WebAuthProvider;
//import com.auth0.android.result.Credentials;


public class MainActivity extends AppCompatActivity {

    //Activity starts with this set fragment
    public ActiveFragments activeFragment = ActiveFragments.EVENTS;

    public BackendClient backendClient;
    public RequestQueue queue;
    private Auth0 auth0;
    private String token;
    private SharedPreferences prefs;
    private AuthenticationAPIClient authenticationAPIClient;

    //UI Elements
    private FloatingActionButton floatingActionButton;
    private BottomAppBar bottomAppBar;
    private MaterialButton accountButton;
    private MaterialButton favoriteHostsButton;
    private MaterialButton createButton;
    private MaterialButton messagesButton;


    private enum ActiveFragments {
        MAP, EVENTS, USER, BONUS, CREATE, MESSAGES
    }

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences("mso.eventium", Context.MODE_PRIVATE);

        backendClient = new BackendClient(getResources().getString(R.string.IP_Server));
        queue = Volley.newRequestQueue(this);
        setupAuth0();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActivityCompat.requestPermissions(this, INITIAL_PERMS, 1331);

        setContentView(R.layout.activity_main);

        this.onFragmentChanged();
        this.createEventMapButton();

        this.setupBottomNavBarButtons();
    }

    /**
     * Sets the desired fragment
     *
     * @param desiredFragment
     * @param setMapicon
     */
    private void setupFragment(Fragment desiredFragment, boolean setMapicon) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, desiredFragment);
        fragmentTransaction.commit();
        floatingActionButton = findViewById(R.id.eventMapButton);

        if (setMapicon) {
            floatingActionButton.setImageResource(R.drawable.ic_map_white_24dp);
        } else {
            floatingActionButton.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);
        }
    }

    /**
     * Called when a fragment gets changed
     */
    private void onFragmentChanged() {

        //Das wird nur benutzt wenn man in der Eventansicht auf die Karte klickt
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
                case "bonusFragment":
                    activeFragment = ActiveFragments.BONUS;
                    break;
                case "CreateFragment":
                    activeFragment = ActiveFragments.CREATE;
                    break;
            }
            getIntent().removeExtra("intentFragment");
        }

        //Prüfe welches Fragment grade als aktiv gesetzt ist
        if (activeFragment == ActiveFragments.EVENTS) {
            setupFragment(new EventFragment(), true);
        }
        if (activeFragment == ActiveFragments.MAP) {
            double lat = getIntent().getDoubleExtra("location_lat", -1);
            double lng = getIntent().getDoubleExtra("location_lng", -1);
            getIntent().removeExtra("location_lat");
            getIntent().removeExtra("location_lng");

            setupFragment(EventiumMapFragment.newInstance(lat, lng), false);
        }
        if (activeFragment == ActiveFragments.USER) {
            setupFragment(UserFragment.newInstance(token), false);
        }
        if (activeFragment == ActiveFragments.BONUS) {
            setupFragment(new BonussystemFragment(), false);
        }
        if (activeFragment == ActiveFragments.CREATE) {
            setupFragment(new CreateFragment(), false);
        }
        if (activeFragment == ActiveFragments.MESSAGES) {
            setupFragment(new NewsfeedFragment(), false);
        }
    }

    /**
     * Setup the switching button for map and event
     */
    private void createEventMapButton() {
        floatingActionButton = findViewById(R.id.eventMapButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activeFragment == ActiveFragments.EVENTS) {
                    activeFragment = ActiveFragments.MAP;
                } else {
                    activeFragment = ActiveFragments.EVENTS;
                }
                onFragmentChanged();
            }
        });
    }

    /**
     * Sets up the bottom navbar with onClick actions for every button
     */
    private void setupBottomNavBarButtons() {
        accountButton = findViewById(R.id.fourth_menu_item);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFragment = ActiveFragments.USER;
                onFragmentChanged();

            }
        });

        favoriteHostsButton = findViewById(R.id.second_menu_item);
        favoriteHostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFragment = ActiveFragments.BONUS;
                onFragmentChanged();
            }
        });

        createButton = findViewById(R.id.third_menu_item);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFragment = ActiveFragments.CREATE;
                onFragmentChanged();
            }
        });

        messagesButton = findViewById(R.id.first_menu_item);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFragment = ActiveFragments.MESSAGES;
                onFragmentChanged();
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
        Log.i("AUTH", "Logged in: " + isLoggedIn() + " with token: " + token);
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
        authenticationAPIClient.userInfo(token)
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile userinfo) {
                        if (createUser) {
                            queue.add(backendClient.createUser(token, userinfo.getEmail(), userinfo.getId(), userinfo.getNickname()));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView nameTV = findViewById(R.id.userName);
                                TextView emailTV = findViewById(R.id.email);
                                nameTV.setText(userinfo.getNickname());
                                emailTV.setText(userinfo.getEmail());

                            }
                        });
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

    public String getToken() {
        return this.token;
    }

    public boolean isLoggedIn() {
        return token != null;
    }
}
