package mso.eventium.ui.create;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import mso.eventium.model.Event;


public class SelectCategorieDialogFragment extends DialogFragment {

    int position = 0;
    public interface SingleCoiceListener{
        void onPositiveButtonClicked(String[] list, int position);
        void onNegativeButtonClicked();
    }

    SingleCoiceListener mListener;


    public SelectCategorieDialogFragment(SingleCoiceListener mListener) {
        super();
        this.mListener = mListener;

    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] list = Event.categories;

        builder.setTitle("Wähle eine Kathegorie aus")
                .setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position = which;
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onPositiveButtonClicked(list,position);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNegativeButtonClicked();
            }
        });

        return builder.create();

    }
}
