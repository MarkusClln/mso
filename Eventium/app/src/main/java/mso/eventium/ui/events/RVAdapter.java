package mso.eventium.ui.events;


import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.datastorage.BackendService;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import mso.eventium.helper.CommonHelper;
import mso.eventium.model.Event;
import retrofit2.Call;
import retrofit2.Callback;

import static mso.eventium.ui.events.EventListFragment.TRANSITION_FOR_ICON;
import static mso.eventium.ui.events.EventListFragment.TRANSITION_FOR_NAME;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> implements Filterable {

    private List<Event> events;

    public List<Event> getFilteredEvents() {
        return filteredEvents;
    }

    private List<Event> filteredEvents;
    private OnNoteListener mOnNoteListener;
    private Context context;
    private Location currentLocation;
    private EventListFragment.ListTypeEnum listType;
    private CommonHelper helper;

    public RVAdapter(Context context, List<Event> events, Location curLocation, EventListFragment.ListTypeEnum listType, OnNoteListener onNoteListener) {
        this.context = context;
        this.events = events;
        this.mOnNoteListener = onNoteListener;
        this.filteredEvents = events;
        this.currentLocation = curLocation;
        this.listType = listType;
        this.helper = new CommonHelper();
    }

    @Override
    public int getItemCount() {
        return filteredEvents.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_events_list_element, viewGroup, false);
        return new EventViewHolder(v, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder EventViewHolder, final int i) {

        EventViewHolder.eventIcon.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        EventViewHolder.eventCardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        EventViewHolder.eventName.setText(filteredEvents.get(i).getEvent_name());
        EventViewHolder.eventDescription.setText(filteredEvents.get(i).getEvent_short_description());
        int points = filteredEvents.get(i).getEvent_points();
        EventViewHolder.eventPoints.setText(Integer.toString(points));

        EventViewHolder.eventDate.setText(helper.FormatDate(filteredEvents.get(i).getEvent_date().toString()));
        EventViewHolder.eventTime.setText(helper.FormatTime(filteredEvents.get(i).getEvent_date().toString()));



//        try {
//            String dateStr = filteredEvents.get(i).getEvent_date().toString();
//            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//            Date date = formatter.parse(dateStr);
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
//            String formatedDate = cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR);
//
//            EventViewHolder.eventDate.setText(formatedDate);
//
//            int hour = cal.get(Calendar.HOUR);
//            int minute = cal.get(Calendar.MINUTE);
//
//            EventViewHolder.eventTime.setText(String.format("%02d:%02d", hour, minute));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        final BackendService backendService = BackendService.getInstance(context);
        final Call<PinEntity> pinCall = backendService.getPinById(filteredEvents.get(i).getPin_id());
        pinCall.enqueue(new Callback<PinEntity>() {

            @Override
            public void onResponse(Call<PinEntity> call, retrofit2.Response<PinEntity> response) {
                final PinEntity pin = response.body();

                if(pin == null){
                    return;
                }

                EventViewHolder.eventLocation.setText(pin.getName());

                LatLng latlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                double dist = helper.CalculateDistance(latlng.latitude, latlng.longitude, pin.getLocation().latitude, pin.getLocation().longitude);

                if(dist > 1){
                    EventViewHolder.eventDistance.setText(String.format("%.1f", dist) + " km");
                }
                else{
                    EventViewHolder.eventDistance.setText(String.format("%.0f", dist * 1000) + " m");
                }
            }

            @Override
            public void onFailure(Call<PinEntity> call, Throwable t) {

            }
        });




        //calc distance


        EventViewHolder.eventDistance.setText(filteredEvents.get(i).getEvent_distance());
        EventViewHolder.eventIcon.setImageResource(filteredEvents.get(i).getEvent_icon());


        if(listType.equals(EventListFragment.ListTypeEnum.OWNED)){
            EventViewHolder.btnUpvote.setVisibility(View.INVISIBLE);
            EventViewHolder.btnDownvote.setVisibility(View.INVISIBLE);
        }else{
            if (filteredEvents.get(i).isUpvoted()) {
                EventViewHolder.btnUpvote.setImageResource(R.drawable.ic_up_blue_24dp);
                EventViewHolder.btnDownvote.setImageResource(R.drawable.ic_down_grey_24dp);

                EventViewHolder.btnUpvote.setEnabled(false);
                EventViewHolder.btnDownvote.setEnabled(true);
            } else if (filteredEvents.get(i).isDownvoted()) {
                EventViewHolder.btnUpvote.setImageResource(R.drawable.ic_up_grey_24dp);
                EventViewHolder.btnDownvote.setImageResource(R.drawable.ic_down_blue_24dp);

                EventViewHolder.btnUpvote.setEnabled(true);
                EventViewHolder.btnDownvote.setEnabled(false);
            } else {
                EventViewHolder.btnUpvote.setImageResource(R.drawable.ic_up_grey_24dp);
                EventViewHolder.btnDownvote.setImageResource(R.drawable.ic_down_grey_24dp);

                EventViewHolder.btnUpvote.setEnabled(true);
                EventViewHolder.btnDownvote.setEnabled(true);
            }
        }
        //EventViewHolder.saveEventButton.setChecked(true);

        EventViewHolder.eventName.setTransitionName(TRANSITION_FOR_NAME + i);
        EventViewHolder.eventDescription.setTransitionName("transitionDescription" + i);
        EventViewHolder.eventDate.setTransitionName("transitionDate" + i);
        EventViewHolder.eventTime.setTransitionName("transitionTime" + i);
        EventViewHolder.eventDistance.setTransitionName("transitionDistance" + i);
        EventViewHolder.eventIcon.setTransitionName(TRANSITION_FOR_ICON + i);

        final MainActivity activity = (MainActivity) context;
        EventViewHolder.btnUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filteredEvents.get(i).isUpvoted()) {

                    Response.Listener rl = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    };
                    JsonObjectRequest req1 = activity.backendClient.pushFavEvent(activity.getToken(), filteredEvents.get(i).getEvent_id(), rl);
                    activity.queue.add(req1);

                    if (filteredEvents.get(i).isDownvoted()) {
                        filteredEvents.get(i).setEvent_points(filteredEvents.get(i).getEvent_points() + 2);
                        EventViewHolder.eventPoints.setText(Integer.toString(filteredEvents.get(i).getEvent_points()));
                    } else {
                        filteredEvents.get(i).setEvent_points(filteredEvents.get(i).getEvent_points() + 1);
                        EventViewHolder.eventPoints.setText(Integer.toString(filteredEvents.get(i).getEvent_points()));
                    }

                    filteredEvents.get(i).setUpvoted(true);
                    filteredEvents.get(i).setDownvoted(false);

                    EventViewHolder.btnUpvote.setImageResource(R.drawable.ic_up_blue_24dp);
                    EventViewHolder.btnDownvote.setImageResource(R.drawable.ic_down_grey_24dp);

                    EventViewHolder.btnUpvote.setEnabled(false);
                    EventViewHolder.btnDownvote.setEnabled(true);


                }
            }
        });

        EventViewHolder.btnDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filteredEvents.get(i).isDownvoted()) {

                    Response.Listener rl = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    };

                    if (filteredEvents.get(i).isUpvoted()) {
                        filteredEvents.get(i).setEvent_points(filteredEvents.get(i).getEvent_points() - 2);
                        EventViewHolder.eventPoints.setText(Integer.toString(filteredEvents.get(i).getEvent_points()));
                    } else {
                        filteredEvents.get(i).setEvent_points(filteredEvents.get(i).getEvent_points() - 1);
                        EventViewHolder.eventPoints.setText(Integer.toString(filteredEvents.get(i).getEvent_points()));
                    }

                    JsonObjectRequest req1 = activity.backendClient.deleteFavEvent(activity.getToken(), filteredEvents.get(i).getEvent_id(), rl);
                    activity.queue.add(req1);
                    filteredEvents.get(i).setDownvoted(true);
                    filteredEvents.get(i).setUpvoted(false);


                    EventViewHolder.btnUpvote.setImageResource(R.drawable.ic_up_grey_24dp);
                    EventViewHolder.btnDownvote.setImageResource(R.drawable.ic_down_blue_24dp);

                    EventViewHolder.btnUpvote.setEnabled(true);
                    EventViewHolder.btnDownvote.setEnabled(false);

                }
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                String[] filter = Key.split("\\|-\\|");
                if (filter.length == 0) {
                    filteredEvents = events;
                } else {
                    List<Event> lstFiltered = new ArrayList<>();
                    for (Event row : events) {

                        boolean add = true;
                        if (!row.getName().toLowerCase().contains(filter[0].toLowerCase()) && !row.getEvent_description().toLowerCase().contains(filter[0].toLowerCase())) {
                            add = false;
                        }
                        if (!filter[1].equals("")) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date eventDate = row.getEvent_date();
                                Date filterDate = format.parse(filter[1].substring(0, 10));

                                if (eventDate.before(filterDate)) {
                                    add = false;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if (!filter[2].equals("Wähle Uhrzeit hier!")) {

                            String[] time = filter[2].split(":");
                            int hoursFilter = Integer.parseInt(time[0]);
                            int minutesFilter = Integer.parseInt(time[1]);
                            int FilterTime = hoursFilter * 60 + minutesFilter;

                            final Calendar cal =  Calendar.getInstance();
                            cal.setTime(row.getEvent_date());
                            int hoursEvent = cal.get(Calendar.HOUR_OF_DAY);
                            int minutesEvent = cal.get(Calendar.MINUTE);
                            int eventTimeInt = hoursEvent * 60 + minutesEvent;

//                            if (EventTime < FilterTime) {
//                                add = false;
//                            }


                        }


                        if (add) {
                            lstFiltered.add(row);
                        }

                    }
                    filteredEvents = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredEvents;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredEvents = (List<Event>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView eventName;
        private TextView eventDescription;
        private TextView eventDate;
        private TextView eventTime;
        private TextView eventDistance;
        private ImageView eventIcon;
        private CardView eventCardView;
        private ConstraintLayout eventConstraintLayout;
        private ToggleButton saveEventButton;
        private ImageView btnUpvote;
        private ImageView btnDownvote;
        private TextView eventPoints;
        private TextView eventLocation;

        private OnNoteListener onNoteListener;

        public EventViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            eventDistance = itemView.findViewById(R.id.event_distance);
            eventIcon = itemView.findViewById(R.id.event_icon);
            eventCardView = itemView.findViewById(R.id.cardview);
            eventConstraintLayout = itemView.findViewById(R.id.Constraintlayout);
            saveEventButton = itemView.findViewById((R.id.button_save));
            btnUpvote = itemView.findViewById((R.id.ImageView_upvote));
            btnDownvote = itemView.findViewById((R.id.ImageView_downvote));
            eventPoints = itemView.findViewById((R.id.textViewPoints));
            eventLocation = itemView.findViewById((R.id.event_location));
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}