package mso.eventium.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mso.eventium.R;
import mso.eventium.model.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private List<Event> eventList;
    Context context;
    private OnNoteListener mOnNoteListener;
    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(Context _context, List<Event> myDataset, OnNoteListener onNoteListener) {
        context = _context;
        eventList = myDataset;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v =  LayoutInflater.from(context)
                .inflate(R.layout.event_list_item, parent, false);

        final EventViewHolder viewHolder = new EventViewHolder(v, mOnNoteListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.eventName.setText(eventList.get(position).getName());
        holder.hostName.setText(eventList.get(position).getHost());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        LinearLayout eventRowItem;
        CardView eventCard;
        TextView eventName;
        TextView hostName;
        OnNoteListener onNoteListener;

        public EventViewHolder(View v, OnNoteListener onNoteListener) {
            super(v);
            eventRowItem = (LinearLayout)  v.findViewById(R.id.event_list_item);
            eventCard = (CardView)itemView.findViewById(R.id.eventcardview);
            eventName = (TextView)v.findViewById(R.id.name);
            hostName = (TextView)v.findViewById(R.id.host);
            this.onNoteListener = onNoteListener;

            v.setOnClickListener(this);
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
