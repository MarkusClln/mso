package mso.eventium.ui.events;


import android.content.Context;
import android.content.res.ColorStateList;
import android.media.Image;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

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


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> implements Filterable {

    private List<Event> EventModels;
    private List<Event> EventModelsFiltered;
    private OnNoteListener mOnNoteListener;
    private Context mContext;




    public RVAdapter(Context mContext, List<Event> i, OnNoteListener onNoteListener) {
        this.mContext = mContext;
        this.EventModels = i;
        this.mOnNoteListener = onNoteListener;
        this.EventModelsFiltered = i;
    }

    @Override
    public int getItemCount() {
        return EventModelsFiltered.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_events_list_element, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v, mOnNoteListener);

        return evh;
    }

    @Override
    public void onBindViewHolder(final EventViewHolder EventViewHolder, final int i) {


        EventViewHolder.eventIcon.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        EventViewHolder.eventCardView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        EventViewHolder.eventName.setText(EventModelsFiltered.get(i).getEvent_name());
        EventViewHolder.eventDescription.setText(EventModelsFiltered.get(i).getEvent_short_description());
        int points =EventModelsFiltered.get(i).getEvent_points();
        EventViewHolder.eventPoints.setText(Integer.toString(points));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            final Calendar myCalendar = Calendar.getInstance();
            Date date = format.parse(EventModelsFiltered.get(i).getEvent_date());
            myCalendar.setTime(date);
            String myFormat = "dd/MM/yy"; //In which you need put here
            SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.GERMANY);

            EventViewHolder.eventDate.setText(sdf1.format(myCalendar.getTime()));

            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
            EventViewHolder.eventTime.setText(sdf2.format(myCalendar.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        EventViewHolder.eventDistance.setText(EventModelsFiltered.get(i).getEvent_distance());
        EventViewHolder.eventIcon.setImageResource(EventModelsFiltered.get(i).getEvent_icon());


        if(EventModelsFiltered.get(i).isUpvoted()){
            EventViewHolder.btnUpvote.setImageResource(R.drawable.ic_up_blue_24dp);
            EventViewHolder.btnDownvote.setImageResource(R.drawable.ic_down_grey_24dp);

            EventViewHolder.btnUpvote.setEnabled(false);
            EventViewHolder.btnDownvote.setEnabled(true);
        }else if(EventModelsFiltered.get(i).isDownvoted()){
            EventViewHolder.btnUpvote.setImageResource(R.drawable.ic_up_grey_24dp);
            EventViewHolder.btnDownvote.setImageResource(R.drawable.ic_down_blue_24dp);

            EventViewHolder.btnUpvote.setEnabled(true);
            EventViewHolder.btnDownvote.setEnabled(false);
        }else{
            EventViewHolder.btnUpvote.setImageResource(R.drawable.ic_up_grey_24dp);
            EventViewHolder.btnDownvote.setImageResource(R.drawable.ic_down_grey_24dp);

            EventViewHolder.btnUpvote.setEnabled(true);
            EventViewHolder.btnDownvote.setEnabled(true);
        }

        //EventViewHolder.saveEventButton.setChecked(true);

        EventViewHolder.eventName.setTransitionName("transitionName" + i);
        EventViewHolder.eventDescription.setTransitionName("transitionDescription" + i);
        EventViewHolder.eventDate.setTransitionName("transitionDate" + i);
        EventViewHolder.eventTime.setTransitionName("transitionTime" + i);
        EventViewHolder.eventDistance.setTransitionName("transitionDistance" + i);
        EventViewHolder.eventIcon.setTransitionName("transitionIcon" + i);

        final MainActivity activity = (MainActivity) mContext;
        EventViewHolder.btnUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EventModelsFiltered.get(i).isUpvoted()) {

                    Response.Listener rl = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    };
                    JsonObjectRequest req1 = activity.bc.pushFavEvent(activity.getToken(), EventModelsFiltered.get(i).getEvent_id(), rl);
                    activity.queue.add(req1);

                    if(EventModelsFiltered.get(i).isDownvoted()){
                        EventModelsFiltered.get(i).setEvent_points(EventModelsFiltered.get(i).getEvent_points()+2);
                        EventViewHolder.eventPoints.setText(Integer.toString(EventModelsFiltered.get(i).getEvent_points()));
                    }else{
                        EventModelsFiltered.get(i).setEvent_points(EventModelsFiltered.get(i).getEvent_points()+1);
                        EventViewHolder.eventPoints.setText(Integer.toString(EventModelsFiltered.get(i).getEvent_points()));
                    }

                    EventModelsFiltered.get(i).setUpvoted(true);
                    EventModelsFiltered.get(i).setDownvoted(false);

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
                if (!EventModelsFiltered.get(i).isDownvoted()) {

                    Response.Listener rl = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    };

                    if(EventModelsFiltered.get(i).isUpvoted()){
                        EventModelsFiltered.get(i).setEvent_points(EventModelsFiltered.get(i).getEvent_points()-2);
                        EventViewHolder.eventPoints.setText(Integer.toString(EventModelsFiltered.get(i).getEvent_points()));
                    }else{
                        EventModelsFiltered.get(i).setEvent_points(EventModelsFiltered.get(i).getEvent_points()-1);
                        EventViewHolder.eventPoints.setText(Integer.toString(EventModelsFiltered.get(i).getEvent_points()));
                    }

                    JsonObjectRequest req1 = activity.bc.deleteFavEvent(activity.getToken(), EventModelsFiltered.get(i).getEvent_id(), rl);
                    activity.queue.add(req1);
                    EventModelsFiltered.get(i).setDownvoted(true);
                    EventModelsFiltered.get(i).setUpvoted(false);



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
                if (filter.length ==0) {
                    EventModelsFiltered = EventModels;
                } else {
                    List<Event> lstFiltered = new ArrayList<>();
                    for (Event row : EventModels) {

                        boolean add = true;
                        if (!row.getName().toLowerCase().contains(filter[0].toLowerCase()) && !row.getEvent_description().toLowerCase().contains(filter[0].toLowerCase())) {
                            add = false;
                        }
                        if(!filter[1].equals("")){
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date eventDate = format.parse(row.getEvent_date().substring(0,10));
                                Date filterDate = format.parse(filter[1].substring(0,10));

                                if(eventDate.before(filterDate)){
                                    add = false;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if(!filter[2].equals("Wähle Uhrzeit hier!")){

                                String[] time = filter[2].split(":");
                                int hoursFilter = Integer.parseInt(time[0]);
                                int minutesFilter = Integer.parseInt(time[1]);
                                int FilterTime = hoursFilter*60 + minutesFilter;

                                String subTime = row.getEvent_date().substring(11,16);
                                String[] eventTime = subTime.split(":");
                                int hoursEvent = Integer.parseInt(eventTime[0]);
                                int minutesEvent = Integer.parseInt(eventTime[1]);
                                int EventTime = hoursEvent*60 + minutesEvent;

                                if(EventTime<FilterTime){
                                    add=false;
                                }



                        }



                        if(add){
                            lstFiltered.add(row);
                        }

                    }
                    EventModelsFiltered = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = EventModelsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                EventModelsFiltered = (List<Event>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName;
        TextView eventDescription;
        TextView eventDate;
        TextView eventTime;
        TextView eventDistance;
        ImageView eventIcon;
        CardView eventCardView;
        ConstraintLayout eventConstraintLayout;
        ToggleButton saveEventButton;
        ImageView btnUpvote;
        ImageView btnDownvote;
        TextView eventPoints;

        OnNoteListener onNoteListener;

        EventViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name_desc);
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