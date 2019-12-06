package mso.eventium.ui.events;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    public void onBindViewHolder(EventViewHolder EventViewHolder, int i) {

        EventViewHolder.eventIcon.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        EventViewHolder.eventCardView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        EventViewHolder.eventName.setText(EventModelsFiltered.get(i).getEvent_name());
        EventViewHolder.eventDescription.setText(EventModelsFiltered.get(i).getEvent_short_description());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            final Calendar myCalendar = Calendar.getInstance();
            Date date = format.parse(EventModelsFiltered.get(i).getEvent_date());
            myCalendar.setTime(date);
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.GERMANY);

            EventViewHolder.eventDate.setText(sdf1.format(myCalendar.getTime()));

            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
            EventViewHolder.eventTime.setText(sdf2.format(myCalendar.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        EventViewHolder.eventDistance.setText(EventModelsFiltered.get(i).getEvent_distance());
        EventViewHolder.eventIcon.setImageResource(EventModelsFiltered.get(i).getEvent_icon());

        EventViewHolder.saveEventButton.setChecked(true);

        EventViewHolder.eventName.setTransitionName("transitionName" + i);
        EventViewHolder.eventDescription.setTransitionName("transitionDescription" + i);
        EventViewHolder.eventDate.setTransitionName("transitionDate" + i);
        EventViewHolder.eventTime.setTransitionName("transitionTime" + i);
        EventViewHolder.eventDistance.setTransitionName("transitionDistance" + i);
        EventViewHolder.eventIcon.setTransitionName("transitionIcon" + i);




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
                if (Key.isEmpty()) {
                    EventModelsFiltered = EventModels;
                } else {
                    List<Event> lstFiltered = new ArrayList<>();
                    for (Event row : EventModels) {
                        //Config here search inputs
                        if (row.getName().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getEvent_description().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getEvent_date().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getEvent_distance().toLowerCase().contains(Key.toLowerCase())) {
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

        OnNoteListener onNoteListener;

        EventViewHolder(View itemView, OnNoteListener onNoteListener) {
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
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);


            saveEventButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
                    scaleAnimation.setDuration(500);
                    BounceInterpolator bounceInterpolator = new BounceInterpolator();
                    scaleAnimation.setInterpolator(bounceInterpolator);
                    buttonView.startAnimation(scaleAnimation);

                    if (isChecked) {
                        //add to users saved events
                    } else {
                        //remove
                    }

                }
            });
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