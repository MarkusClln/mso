package mso.eventium.ui.user;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.auth0.android.result.Credentials;

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
        TextView textView = root.findViewById(R.id.credentials);

        token =getArguments().getString(ARGS_TOKEN);

        if(token != null){
            loginButton.setText("Logout");
            textView.setText(token);
        }else{
            loginButton.setText("Login");
        }





        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainActivity) getActivity()).login) {
                    ((MainActivity) getActivity()).login();
                } else {
                    ((MainActivity) getActivity()).logout();
                }
            }
        });
        return root;
    }


}
