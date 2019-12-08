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

        String des1 = "Gefallen eigentum schuppen so ei feinheit. Gegen er kinde kenne mu se. Im zu sauber labsal werden en heraus sterne mu. Trostlos der das streckte gefallts ins tag begierig. Gebrauch eleonora horchend gedanken als ich befehlen. Geschirr manchmal an spateren hinunter es sichtbar er ri einander. Herkommen betrubtes einfacher es so am kreiselnd verwegene schnupfen.";
        String des2 = "Lief die wege als wohl. Tun zog angenommen nun dienstmagd mancherlei federdecke hat augenblick wohnzimmer. Schwemmen verweilen zufrieden stadtchen in zu wo. Erisch spinat gut mochte karten spahte stille ich. Te wu eines ja warum lobte gehen. Flecken lacheln bei schoner anblick mut beschlo sie. Luften keinem la he sterne es. In em sorgen besuch es kissen stille. In schlafen lampchen verlohnt feinheit sichtbar ab vorliebe.";
        String des3 = "Te braunen pa am kapelle gerbers zu heruber. He am meisten bessern steigst kriegen da. Sterne keinen allein ihr des. Naturlich getrunken ist alt hin schwachem kam grundlich. Tadelte lebhaft aus niemand spielen nah konnten. Ten mut mehrere heiland nachtun brummte bereits. Sei von tun der vergnugen schreibet vogelnest. Heftig da fragen feinen durren la erregt mi. Konnte ins ich soviel schade fallen lassen.";
        String des4 = "In la ausdenken fu ertastete sorglosen am filzhutes schwemmen. Im vollends hinabsah gebogene funkelte du en irgendwo. Als vor sagst ferne ihn kinde spiel durch. Lieb tust ubel gar alt froh. Harmlos kleines offnung da heiland in spiegel anderen la wu. Sah geheimnis schonheit furchtete gar magdebett tanzmusik zufrieden. Roten litze krank abend sag die denkt seine. Ordentlich bei getunchten leuchtturm auf geschlafen geschwatzt und. Und messingnen handarbeit der hinstellte ihr uberwunden ich.";

        String des1_short = "short Description 1";
        String des2_short = "short Description 2";
        String des3_short = "short Description 3";
        String des4_short = "short Description 4";

        userEvents1.add(new Event("Event1_1", des1,des1_short, "23.01.2019", "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails, "",false,""));
        userEvents1.add(new Event("Event1_2", des2,des2_short, "07.03.2020", "distance: 300m", R.drawable.img_disco, R.drawable.ic_flaschen, "",false,""));
        List<Event> userEvents2 = new ArrayList<Event>();
        userEvents1.add(new Event("Event2_1", des3,des3_short, "23.01.2019",  "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails, "",false,""));
        userEvents1.add(new Event("Event2_2", des4,des4_short, "07.03.2020",  "distance: 300m", R.drawable.img_disco, R.drawable.ic_flaschen, "",false,""));
        List<Event> userEvents3 = new ArrayList<Event>();
        userEvents1.add(new Event("Event3_1", des4,des4_short, "23.01.2019",  "distance: 100m", R.drawable.img_drink, R.drawable.ic_cocktails, "",false,""));
        userEvents1.add(new Event("Event3_2", des4,des4_short, "07.03.2020",  "distance: 300m", R.drawable.img_disco, R.drawable.ic_flaschen, "",false,""));


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
