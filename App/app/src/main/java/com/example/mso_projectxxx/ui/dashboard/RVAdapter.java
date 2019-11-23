package com.example.mso_projectxxx.ui.dashboard;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mso_projectxxx.R;
import com.example.mso_projectxxx.ui.dashboard.item;
import java.util.List;



public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder>{

    private List<item> items;
    private OnNoteListener mOnNoteListener;


    RVAdapter(List<item> i, OnNoteListener onNoteListener){
        this.items = i;
        this.mOnNoteListener = onNoteListener;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ItemViewHolder pvh = new ItemViewHolder(v, mOnNoteListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.personName.setText(items.get(i).name);
        itemViewHolder.personAge.setText(items.get(i).age);
        itemViewHolder.personPhoto.setImageResource(items.get(i).photoId);
        itemViewHolder.personPhoto.setTransitionName("transitionPhoto" + i);
        itemViewHolder.personName.setTransitionName("transitionName" + i);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView personName;
        TextView personAge;
        ImageView personPhoto;
        OnNoteListener onNoteListener;

        ItemViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
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