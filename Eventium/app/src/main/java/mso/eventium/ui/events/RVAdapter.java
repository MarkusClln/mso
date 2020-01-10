package mso.eventium.ui.events;


import android.content.Context;
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
import mso.eventium.model.Event;

import static mso.eventium.ui.events.EventListFragment.TRANSITION_FOR_ICON;
import static mso.eventium.ui.events.EventListFragment.TRANSITION_FOR_NAME;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> implements Filterable {

    private List<Event> events;
    private List<Event> filteredEvents;
    private OnNoteListener mOnNoteListener;
    private Context context;


    public RVAdapter(Context context, List<Event> events, OnNoteListener onNoteListener) {
        this.context = context;
        this.events = events;
        this.mOnNoteListener = onNoteListener;
        this.filteredEvents = events;
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        EventViewHolder.eventDate.setText(dateFormat.format(filteredEvents.get(i).getEvent_date()));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        EventViewHolder.eventTime.setText(timeFormat.format(filteredEvents.get(i).getEvent_date()));

        EventViewHolder.eventDistance.setText(filteredEvents.get(i).getEvent_distance());
        EventViewHolder.eventIcon.setImageResource(filteredEvents.get(i).getEvent_icon());


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
                            int filterTime = hoursFilter * 60 + minutesFilter;

                            final Calendar cal =  Calendar.getInstance();
                            cal.setTime(row.getEvent_date());
                            int hoursEvent = cal.get(Calendar.HOUR_OF_DAY);
                            int minutesEvent = cal.get(Calendar.MINUTE);
                            int eventTimeInt = hoursEvent * 60 + minutesEvent;

                            if (eventTimeInt < filterTime) {
                                add = false;
                            }
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