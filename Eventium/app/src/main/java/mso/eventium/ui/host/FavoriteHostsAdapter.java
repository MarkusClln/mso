package mso.eventium.ui.host;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mso.eventium.R;
import mso.eventium.model.User;


public class FavoriteHostsAdapter extends RecyclerView.Adapter<FavoriteHostsAdapter.HostViewHolder> {

    private List<User> Hosts;
    private OnNoteListener mOnNoteListener;
    private Context mContext;

    public FavoriteHostsAdapter(Context mContext, List<User> i, FavoriteHostsAdapter.OnNoteListener onNoteListener) {
        this.mContext = mContext;
        this.Hosts = i;
        this.mOnNoteListener = onNoteListener;
    }


    @Override
    public int getItemCount() {
        return Hosts.size();
    }

    @NonNull
    @Override
    public FavoriteHostsAdapter.HostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_favorite_hosts_list_element, viewGroup, false);
        return new HostViewHolder(v, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(FavoriteHostsAdapter.HostViewHolder HostViewHolder, int i) {
        HostViewHolder.hostName.setText(Hosts.get(i).getFirstName() + " " + Hosts.get(i).getLastName());
        //HostViewHolder.hostProfileImage.setImageResource(Hosts.get(i).getProfileImage());
        HostViewHolder.ratingBar.setRating(3.5f);
        if (i == Hosts.size() - 1) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) HostViewHolder.hostLinearLayout.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, 300);
        }
    }


    public static class HostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hostName;
        ImageView hostProfileImage;
        //CardView eventCardView;
        LinearLayout hostLinearLayout;
        RatingBar ratingBar;

        FavoriteHostsAdapter.OnNoteListener onNoteListener;

        HostViewHolder(View itemView, FavoriteHostsAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            hostName = itemView.findViewById(R.id.host_name);
            hostProfileImage = itemView.findViewById(R.id.host_profile_image);
            //eventCardView = (CardView) itemView.findViewById(R.id.cardview);
            hostLinearLayout = itemView.findViewById(R.id.hostLinearLayout);
            ratingBar = itemView.findViewById(R.id.host_fixed_ratingbar);
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