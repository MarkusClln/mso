package mso.eventium.ui.events.complete;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import com.google.android.material.tabs.TabLayout;

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

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tablayout);
        tabLayout.setVisibility(View.VISIBLE);


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

       // View root2 = inflater.inflate(R.layout.fragment_event, container, false);












        TextView mT = recyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.name);
        mT.setTransitionName("bsp_name");
        EventDetailFragment detailFragment = new EventDetailFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.root_frame, new EventDetailFragment());
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.addSharedElement(mT,"bsp_name");
//        transaction.addToBackStack(null);
//        transaction.commit();


        //ImageView mViewPhoto = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.person_photo);
        //TextView mViewName = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.person_name);

        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));

        // Create new fragment to add (Fragment B)
        //item item = items.get(position);
        //String transitionPhoto = "transitionPhoto"+position;
        String transitionName = "bsp_name";
        //Fragment fragment = eventDetails.newInstance(item.name, item.age, item.photoId, transitionPhoto, transitionName);
        detailFragment.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
        detailFragment.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));

        // Add Fragment B
        FragmentTransaction ft = getFragmentManager().beginTransaction()
                .replace(R.id.root_frame, detailFragment)
                .addToBackStack(null)
                .addSharedElement(mT, transitionName);

        ft.commit();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new EventAdapter(getActivity(), getDummyData(), this);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new EventAdapter(getActivity(), getDummyData(), this);
        recyclerView.setAdapter(mAdapter);
    }
}
