package mso.eventium.ui.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mso.eventium.R;
import mso.eventium.model.Newsfeed;

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedAdapter.NewsFeedViewHolder> {

    private List<Newsfeed> newsfeedList;
    private OnNoteListener mOnNoteListener;
    private Context mContext;

    public NewsfeedAdapter(Context mContex,List<Newsfeed> newsfeedList, NewsfeedAdapter.OnNoteListener onNoteListener) {
        this.mContext = mContext;
        this.newsfeedList = newsfeedList;
        this.mOnNoteListener = onNoteListener;
    }


    @Override
    public int getItemCount() {
        return newsfeedList.size();
    }

    @Override
    public NewsfeedAdapter.NewsFeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_newsfeed_list_element, viewGroup, false);
        return new NewsFeedViewHolder(v, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(NewsfeedAdapter.NewsFeedViewHolder NewsFeedViewHolder, int i) {
        NewsFeedViewHolder.title.setText(newsfeedList.get(i).getTitle());
        NewsFeedViewHolder.description.setText(newsfeedList.get(i).getDescription());

        if(newsfeedList.get(i).isAdd()){
            NewsFeedViewHolder.iconAdd.setVisibility(View.VISIBLE);
            NewsFeedViewHolder.iconUpdate.setVisibility(View.INVISIBLE);
        }
        else{
            NewsFeedViewHolder.iconAdd.setVisibility(View.INVISIBLE);
            NewsFeedViewHolder.iconUpdate.setVisibility(View.VISIBLE);
        }
    }


    public static class NewsFeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        NewsfeedAdapter.OnNoteListener onNoteListener;
        ImageView iconAdd;
        ImageView iconUpdate;

        NewsFeedViewHolder(View itemView, NewsfeedAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            title = itemView.findViewById(R.id.newsfeedTitle);
            description = itemView.findViewById(R.id.newsfeedDescription);
            iconAdd = itemView.findViewById(R.id.iconAddNewsFeed);
            iconUpdate = itemView.findViewById(R.id.iconUpdateNewsFeed);
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
