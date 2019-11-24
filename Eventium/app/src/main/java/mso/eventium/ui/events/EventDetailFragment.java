package mso.eventium.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import mso.eventium.R;

public class EventDetailFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_event_detail, container, false);

        TextView eventName = (TextView) root.findViewById(R.id.eventNameDetail);
        TextView hostName = (TextView) root.findViewById(R.id.eventHostDetail);

        return root;
    }

}
