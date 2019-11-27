package mso.eventium.ui.events;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mso.eventium.R;
import mso.eventium.model.Event;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> implements Filterable {

    private List<EventModel> EventModels;
    private List<EventModel> EventModelsFiltered;
    private OnNoteListener mOnNoteListener;
    private Context mContext;


    public RVAdapter(Context mContext, List<EventModel> i, OnNoteListener onNoteListener){
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


        EventViewHolder.eventName.setText(EventModelsFiltered.get(i).event_name);
        EventViewHolder.eventDescription.setText(EventModelsFiltered.get(i).event_description);
        EventViewHolder.eventDate.setText(EventModelsFiltered.get(i).event_date);
        EventViewHolder.eventTime.setText(EventModelsFiltered.get(i).event_time);
        EventViewHolder.eventDistance.setText(EventModelsFiltered.get(i).event_distance);
        EventViewHolder.eventIcon.setImageResource(EventModelsFiltered.get(i).event_icon);

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
                if(Key.isEmpty()){
                    EventModelsFiltered = EventModels;
                }else{
                    List<EventModel> lstFiltered = new ArrayList<>();
                    for( EventModel row : EventModels){
                        //Config here search inputs
                        if(row.getName().toLowerCase().contains(Key.toLowerCase())||
                                row.getEvent_description().toLowerCase().contains(Key.toLowerCase())||
                                row.getEvent_date().toLowerCase().contains(Key.toLowerCase())||
                                row.getEvent_time().toLowerCase().contains(Key.toLowerCase())||
                                row.getEvent_distance().toLowerCase().contains(Key.toLowerCase())){
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

                EventModelsFiltered = (List<EventModel>) results.values;
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

        OnNoteListener onNoteListener;

        EventViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventDescription = (TextView)itemView.findViewById(R.id.event_description);
            eventDate = (TextView)itemView.findViewById(R.id.event_date);
            eventTime = (TextView)itemView.findViewById(R.id.event_time);
            eventDistance = (TextView)itemView.findViewById(R.id.event_distance);
            eventIcon = (ImageView)itemView.findViewById(R.id.event_icon);
            eventCardView = (CardView) itemView.findViewById(R.id.cardview);

            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }


}