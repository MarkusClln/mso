package mso.eventium.ui.events.complete;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import mso.eventium.R;
import mso.eventium.ui.events.complete.EventCompleteFragment;

public class EventCompleteFragmentRoot extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_event_complete_root, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        /*
         * When this container fragment is created, we fill it with our first
         * "real" fragment
         */
        transaction.replace(R.id.root_frame, new EventCompleteFragment(), "1");
        transaction.addToBackStack(null);
        transaction.commit();

        return view;
    }
}
