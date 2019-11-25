package mso.eventium.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import mso.eventium.R;

public class UserFirstFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user_first, container, false);

        Button btn = (Button) root.findViewById(R.id.userbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                /*
                 * IMPORTANT: We use the "root frame" defined in
                 * "root_fragment.xml" as the reference to replace fragment
                 */
                trans.replace(R.id.user_root_frame, new UserDetailFragment());

                /*
                 * IMPORTANT: The following lines allow us to add the fragment
                 * to the stack and return to it later, by pressing back
                 */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });
        return root;
    }
}
