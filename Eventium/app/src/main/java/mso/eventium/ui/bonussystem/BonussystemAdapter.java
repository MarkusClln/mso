package mso.eventium.ui.bonussystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mso.eventium.R;
import mso.eventium.model.Bonus;


public class BonussystemAdapter extends RecyclerView.Adapter<BonussystemAdapter.BonusViewHolder> {

    private List<Bonus> bonusList;
    private OnNoteListener mOnNoteListener;
    private Context mContext;

    public BonussystemAdapter(Context mContext, List<Bonus> i, BonussystemAdapter.OnNoteListener onNoteListener) {
        this.mContext = mContext;
        this.bonusList = i;
        this.mOnNoteListener = onNoteListener;
    }


    @Override
    public int getItemCount() {
        return bonusList.size();
    }

    @NonNull
    @Override
    public BonussystemAdapter.BonusViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_bonussystem_list_element, viewGroup, false);
        return new BonusViewHolder(v, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(BonussystemAdapter.BonusViewHolder BonusViewHolder, int i) {
        BonusViewHolder.bonusName.setText(bonusList.get(i).getName());
        //HostViewHolder.hostProfileImage.setImageResource(Hosts.get(i).getProfileImage());
//        if (i == bonusList.size() - 1) {
//            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) BonusViewHolder.hostLinearLayout.getLayoutParams();
//            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, 300);
//        }
    }


    public static class BonusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bonusName;
        //CardView eventCardView;
        LinearLayout hostLinearLayout;

        BonussystemAdapter.OnNoteListener onNoteListener;

        BonusViewHolder(View itemView, BonussystemAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            bonusName = itemView.findViewById(R.id.bonus_name);
            hostLinearLayout = itemView.findViewById(R.id.bonusLinearLayout);
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
