package mso.eventium.ui.bonussystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import mso.eventium.R;
import mso.eventium.model.Bonus;


public class BonussystemAdapter extends RecyclerView.Adapter<BonussystemAdapter.BonusViewHolder> {

    private List<Bonus> bonusList;
    private OnNoteListener mOnNoteListener;
    private Context mContext;
    private BonussystemListFragment.ListTypeEnum listType;

    public BonussystemAdapter(Context mContext, List<Bonus> i, BonussystemListFragment.ListTypeEnum listType, BonussystemAdapter.OnNoteListener onNoteListener) {
        this.mContext = mContext;
        this.bonusList = i;
        this.mOnNoteListener = onNoteListener;
        this.listType = listType;
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
        BonusViewHolder.eventName.setText(bonusList.get(i).getName());
        BonusViewHolder.bonusDescription.setText(bonusList.get(i).getDescription());


        if(this.listType.equals(BonussystemListFragment.ListTypeEnum.USED)){
            BonusViewHolder.btn.setVisibility(View.INVISIBLE);
            BonusViewHolder.bonusCostUsageDate.setText(bonusList.get(i).getUsedOnDate());
            BonusViewHolder.usageDate.setVisibility(View.VISIBLE);
        }
        else{
            BonusViewHolder.bonusCostUsageDate.setText(bonusList.get(i).getCost());
            BonusViewHolder.usageDate.setVisibility(View.INVISIBLE);
        }
    }


    public static class BonusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName;
        TextView bonusDescription;
        TextView bonusCostUsageDate;
        LinearLayout hostLinearLayout;
        TextView usageDate;
        MaterialButton btn;
        BonussystemAdapter.OnNoteListener onNoteListener;

        BonusViewHolder(View itemView, BonussystemAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            eventName = itemView.findViewById(R.id.bonus_event_name);
            bonusDescription = itemView.findViewById(R.id.bonus_description);
            bonusCostUsageDate = itemView.findViewById(R.id.bonus_cost_or_used_date);
            hostLinearLayout = itemView.findViewById(R.id.bonusLinearLayout);
            btn = itemView.findViewById(R.id.getBonusButton);
            usageDate = itemView.findViewById(R.id.usageDate);
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
