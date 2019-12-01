package mso.eventium.ui.host;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mso.eventium.R;
import mso.eventium.model.Event;
import mso.eventium.model.User;

public class FavoriteHostsFragment extends Fragment implements FavoriteHostsAdapter.OnNoteListener {
    private RecyclerView mRecyclerView;
    private FavoriteHostsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<User> hosts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_favorite_hosts, container, false);
        hosts = getDummyData();

        mRecyclerView = root.findViewById(R.id.rvHosts);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(root.getContext());
        mAdapter = new FavoriteHostsAdapter(getContext(), hosts, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return root;
    }

    public ArrayList<User> getDummyData() {
        ArrayList<User> users = new ArrayList<User>();
        List<Event> userEvents1 = new ArrayList<Event>();
        userEvents1.add(new Event("Event1_1", "", "23.01.2019", "24:00", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails));
        userEvents1.add(new Event("Event1_2", "", "07.03.2020", "12:00", "distance: 300m", R.drawable.img_disco, R.drawable.ic_flaschen));
        List<Event> userEvents2 = new ArrayList<Event>();
        userEvents1.add(new Event("Event2_1", "", "23.01.2019", "24:00", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails));
        userEvents1.add(new Event("Event2_2", "", "07.03.2020", "12:00", "distance: 300m", R.drawable.img_disco, R.drawable.ic_flaschen));
        List<Event> userEvents3 = new ArrayList<Event>();
        userEvents1.add(new Event("Event3_1", "", "23.01.2019", "24:00", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails));
        userEvents1.add(new Event("Event3_2", "", "07.03.2020", "12:00", "distance: 300m", R.drawable.img_disco, R.drawable.ic_flaschen));


        users.add(new User("Hans", "Wurst", userEvents1));
        users.add(new User("Ursel", "Mursel", userEvents2));
        users.add(new User("David", "Schneemann", userEvents3));

        users.add(new User("Hans", "Wurst", userEvents1));
        users.add(new User("Ursel", "Mursel", userEvents2));
        users.add(new User("David", "Schneemann", userEvents3));

        users.add(new User("Hans", "Wurst", userEvents1));
        users.add(new User("Ursel", "Mursel", userEvents2));
        users.add(new User("David", "Schneemann", userEvents3));

        return users;
    }

    @Override
    public void onNoteClick(int position) {

        transitionActivity(position);

    }

    private void transitionActivity(int position) {
        //setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
        //setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.slide_bottom));

        User host = hosts.get(position);


        String transitionName = "transitionName" + position;
        String transitionIcon = "transitionIcon" + position;
        String transitionDescription = "transitionDescription" + position;


        TextView mViewName = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.host_name);
        //ImageView mViewIcon = mRecyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.host_profile_image);


        mViewName.setTransitionName(transitionName);
        //mViewIcon.setTransitionName(transitionIcon);


        Intent intent = new Intent(getContext(), HostDetailActivity.class);
        intent.putExtra(HostDetailActivity.ARG_HOST_NAME, host.getFirstName() + " " + host.getLastName());
//      intent.putExtra(HostDetailActivity.ARG_TRANSITION_HOST_ICON, transitionIcon);

        Pair<View, String> p1 = Pair.create((View) mViewName, transitionName);

//
//
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this.getActivity(), p1);

        startActivity(intent, options.toBundle());


    }
}
