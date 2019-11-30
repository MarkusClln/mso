package mso.eventium.ui.user;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

//import com.auth0.android.Auth0;

import mso.eventium.MainActivity;
import mso.eventium.R;

public class UserFragment extends Fragment {

    //private Auth0 auth0;
    public static final String EXTRA_CLEAR_CREDENTIALS = "com.auth0.CLEAR_CREDENTIALS";
    public static final String EXTRA_ACCESS_TOKEN = "com.auth0.ACCESS_TOKEN";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_user, container, false);

       // auth0 = new Auth0(root.getContext());
       // auth0.setOIDCConformant(true);

        //Check if the activity was launched to log the user out

        Button loginButton = root.findViewById(R.id.logout);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((MainActivity)getActivity()).login){
                    ((MainActivity)getActivity()).login();
                }else{
                    ((MainActivity)getActivity()).logout();
                }
            }
        });
        return root;
    }



}
