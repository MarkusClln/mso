package mso.eventium.ui.user;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.auth0.android.result.Credentials;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mso.eventium.MainActivity;
import mso.eventium.R;

//import com.auth0.android.Auth0;

public class UserFragment extends Fragment {

    private static String ARGS_TOKEN = "args_token";

    public static UserFragment newInstance(String token) {

        Bundle args = new Bundle();
        if(token!= null){
            args.putString(ARGS_TOKEN, token);
        }
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private String token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_user, container, false);
        Button loginButton = root.findViewById(R.id.logout);

        token =getArguments().getString(ARGS_TOKEN);

        if(token != null){
            loginButton.setText("Logout");
            ((MainActivity) getActivity()).getProfile();
        }else{
            loginButton.setText("Login");
            TextView nameTV = root.findViewById(R.id.userName);
            TextView emailTV = root.findViewById(R.id.email);
            nameTV.setVisibility(View.INVISIBLE);
            emailTV.setVisibility(View.INVISIBLE);

            TextView nameTVdesc = root.findViewById(R.id.userNameDesc);
            TextView emailTVdesc = root.findViewById(R.id.emailDesc);
            nameTVdesc.setVisibility(View.INVISIBLE);
            emailTVdesc.setVisibility(View.INVISIBLE);
        }





        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainActivity) getActivity()).login) {
                    ((MainActivity) getActivity()).login();
                    TextView nameTV = root.findViewById(R.id.userName);
                    TextView emailTV = root.findViewById(R.id.email);
                    nameTV.setVisibility(View.VISIBLE);
                    emailTV.setVisibility(View.VISIBLE);

                    TextView nameTVdesc = root.findViewById(R.id.userNameDesc);
                    TextView emailTVdesc = root.findViewById(R.id.emailDesc);
                    nameTVdesc.setVisibility(View.VISIBLE);
                    emailTVdesc.setVisibility(View.VISIBLE);
                } else {
                    ((MainActivity) getActivity()).logout();
                    Button loginButton = root.findViewById(R.id.logout);
                    TextView nameTV = root.findViewById(R.id.userName);
                    TextView emailTV = root.findViewById(R.id.email);
                    loginButton.setText("Login");
                    nameTV.setVisibility(View.INVISIBLE);
                    emailTV.setVisibility(View.INVISIBLE);
                    nameTV.setText("");
                    emailTV.setText("");

                    TextView nameTVdesc = root.findViewById(R.id.userNameDesc);
                    TextView emailTVdesc = root.findViewById(R.id.emailDesc);
                    nameTVdesc.setVisibility(View.INVISIBLE);
                    emailTVdesc.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button dummiData = root.findViewById(R.id.dummiData);
        dummiData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDummiData();
            }
        });

        return root;
    }

    public void createDummiData(){
        final List<String> list = new ArrayList<String>();




        Response.Listener responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    list.add(response.get("_id").toString());
                    if(list.size()==5){
                        createDummiData2(list);
                    }
                }catch (JSONException e){

                }

            }
        };

        JsonObjectRequest req1 = ((MainActivity) getActivity()).bc.createPin(token,49.466633, 8.259154, "Eine Location1", "Ding Dong1", responseListener );
        ((MainActivity) getActivity()).queue.add(req1);
        JsonObjectRequest req2 = ((MainActivity) getActivity()).bc.createPin(token,49.466643, 8.259144, "Eine Location2", "Ding Dong2", responseListener );
        ((MainActivity) getActivity()).queue.add(req2);
        JsonObjectRequest req3 = ((MainActivity) getActivity()).bc.createPin(token,49.466653, 8.259134, "Eine Location3", "Ding Dong3", responseListener );
        ((MainActivity) getActivity()).queue.add(req3);
        JsonObjectRequest req4 = ((MainActivity) getActivity()).bc.createPin(token,49.466663, 8.259124, "Eine Location4", "Ding Dong4", responseListener );
        ((MainActivity) getActivity()).queue.add(req4);
        JsonObjectRequest req5 = ((MainActivity) getActivity()).bc.createPin(token,49.466673, 8.259114, "Eine Location5", "Ding Dong5", responseListener );
        ((MainActivity) getActivity()).queue.add(req5);



    }

    public void createDummiData2(List<String> list){

        Response.Listener responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("JSONPost", response.toString());
                //pDialog.hide();
            }
        };
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2019);
        myCalendar.set(Calendar.MONTH, Calendar.AUGUST);
        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
        myCalendar.set(Calendar.HOUR, 11);
        myCalendar.set(Calendar.MINUTE, 16);


        JsonObjectRequest req1 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(0),"Ein Event 1", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req1);
        JsonObjectRequest req2 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(0),"Ein Event 2", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req2);
        JsonObjectRequest req3 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(0),"Ein Event 3", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req3);
        JsonObjectRequest req4 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(1),"Ein Event 4", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req4);
        JsonObjectRequest req5 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(1),"Ein Event 5", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req5);
        JsonObjectRequest req6 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(2),"Ein Event 6", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req6);
        JsonObjectRequest req7 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(2),"Ein Event 7", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req7);
        JsonObjectRequest req8 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(3),"Ein Event 8", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req8);
        JsonObjectRequest req9 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(3),"Ein Event 9", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req9);
        JsonObjectRequest req10 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(4),"Ein Event 11", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req10);
        JsonObjectRequest req11 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(4),"Ein Event 12", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req11);
        JsonObjectRequest req12 = ((MainActivity) getActivity()).bc.createEvent(token, list.get(4),"Ein Event 13", "Ding Dong Lang", "Ding Dong", myCalendar, responseListener);
        ((MainActivity) getActivity()).queue.add(req12);

    }
}
