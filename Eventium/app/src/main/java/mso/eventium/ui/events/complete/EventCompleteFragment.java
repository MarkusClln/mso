package mso.eventium.ui.events.complete;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mso.eventium.R;
import mso.eventium.adapter.EventAdapter;
import mso.eventium.model.Event;
import mso.eventium.ui.events.EventDetailFragment;

public class EventCompleteFragment extends Fragment implements EventAdapter.OnNoteListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_event_complete, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new EventAdapter(getActivity(), getDummyData(), this);
        recyclerView.setAdapter(mAdapter);


        return root;
    }

    private List<Event> getDummyData(){
        List<Event> events = new ArrayList<>();

        events.add(new Event("Event1", "host1"));
        events.add(new Event("Event2", "host2"));
        events.add(new Event("Event3", "host3"));

        return events;
    }

    @Override
    public void onNoteClick(int position) {
        EventDetailFragment detailFragment = new EventDetailFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_frame, detailFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
