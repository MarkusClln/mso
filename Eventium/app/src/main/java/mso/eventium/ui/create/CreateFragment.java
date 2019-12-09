package mso.eventium.ui.create;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import mso.eventium.R;
import mso.eventium.client.backendClient;


public class CreateFragment extends Fragment implements SelectCategorieDialogFragment.SingleCoiceListener {

    //private static final String ARG_PARAM1 = "param1";
    //private String mParam1;

    private String ip;
    public backendClient bc;
    public RequestQueue queue;
    View root;
    MaterialButton btnSelectCategorie;
    MaterialButton btnCreateEvent;
    TextView dateView;
    TextView timeView;
    EditText nameView;
    EditText shortDescView;
    EditText descView;
    AutoCompleteTextView locationAutoCompleteTextView;
    String [][] autocomplete_ids = new String[0][0];
    String [] autocomplete;


    final Calendar myCalendar = Calendar.getInstance();


    public CreateFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance() {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
        ip = getResources().getString(R.string.IP_Server);
        bc = new backendClient(ip);
        queue = Volley.newRequestQueue(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_create, container, false);

        setUpEditText();
        setUpTimePicker();
        setUpCalenderPicker();
        setupCategoriesButton();
        setUpCreateButton();
        setUpLocationEdit();


        //  https://www.truiton.com/2018/06/android-autocompletetextview-suggestions-from-webservice-call/



        return root;
    }

    private void setupCategoriesButton(){
        btnSelectCategorie = root.findViewById(R.id.selectCategorieButton);
        btnSelectCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment selectCategorieDialog = new SelectCategorieDialogFragment(CreateFragment.this);
                selectCategorieDialog.setCancelable(false);
                selectCategorieDialog.show(getFragmentManager(),"SelectCategorieFragment");
            }
        });
    }

    @Override
    public void onPositiveButtonClicked(String[] list, int position) {
        btnSelectCategorie.setText(list[position]);
    }

    @Override
    public void onNegativeButtonClicked() {
        btnSelectCategorie.setText("Wähle aus!");
    }

    public void setUpCalenderPicker(){
        dateView = root.findViewById(R.id.dateText);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

                dateView.setText(sdf.format(myCalendar.getTime()));
            }

        };

        dateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    public void setUpTimePicker(){

        timeView = root.findViewById(R.id.timeText);
        final TimePickerDialog.OnTimeSetListener date = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {

                myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                myCalendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                timeView.setText(sdf.format(myCalendar.getTime()));
            }

        };

        timeView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(),AlertDialog.THEME_HOLO_LIGHT, date, myCalendar.get(Calendar.HOUR),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });
    }

    private void setUpEditText(){
        nameView = root.findViewById(R.id.event_name);
        shortDescView = root.findViewById(R.id.event_shortDesc);
        descView = root.findViewById(R.id.event_desc);
    }

    public void setUpCreateButton(){
        btnCreateEvent = root.findViewById(R.id.buttonCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pin_name = locationAutoCompleteTextView.getText().toString();
                String pin_id="";
                for(int i=0; i<autocomplete_ids.length; i++){
                    if(autocomplete_ids[i][1].equals(pin_name)){
                        pin_id = autocomplete_ids[i][0];
                    }
                }

                String eventName = nameView.getText().toString();
                String eventShortDesc =shortDescView.getText().toString();
                String eventDesc =descView.getText().toString();
                String eventDate = myCalendar.getTime().toString();
                String eventCategorie = btnSelectCategorie.getText().toString();

                String output = "eventName="+eventName+"\n"+
                        "eventShortDesc="+eventShortDesc+"\n"+
                        "eventDesc="+eventDesc+"\n"+
                        "eventDate="+eventDate+"\n"+
                        "eventCategorie="+eventCategorie+"\n"+
                        "pinID="+pin_id;
                Toast.makeText(getContext(),output, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setUpLocationEdit(){
        locationAutoCompleteTextView = root.findViewById(R.id.event_location);

        String [] init = {"Such doch etwas"};
        final SpecialAdapter adapter = new SpecialAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, init);

        locationAutoCompleteTextView.setAdapter(adapter);
        locationAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                Response.Listener rl = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            autocomplete = new String[array.length()];
                            autocomplete_ids = new String[array.length()][2];

                            if(array.length()==0){
                                autocomplete = new String[1];
                                autocomplete[0] = s.toString()+"...no match found";
                            }else {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject event = array.getJSONObject(i);
                                    autocomplete[i] = event.getString("name");
                                    autocomplete_ids[i][0] = event.getString("_id");
                                    autocomplete_ids[i][1] = event.getString("name");
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                };
                if(!s.toString().equals("")) {
                    StringRequest req1 = bc.getPinByName(s.toString(), rl);
                    queue.add(req1);
                }
                if(autocomplete!=null){
                    adapter.updateResults(autocomplete);
                    adapter.updateResults(autocomplete);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }





    public class SpecialAdapter extends BaseAdapter implements Filterable {

        private List<String> originalItems;
        private List<String> filteredItems;
        private int layoutResource;
        private Context context;

        public SpecialAdapter(Context context, int resource, String[] items) {
            this.context = context;
            this.layoutResource = resource;
            this.originalItems = new ArrayList<String>(Arrays.asList(items));
            this.filteredItems = new ArrayList<String>(originalItems);
        }

        @Override
        public int getCount() {
            return filteredItems.size();
        }

        @Override
        public String getItem(int position) {
            return filteredItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            String item = getItem(position);
            if(convertView == null) {
                convertView = inflater.inflate(layoutResource, parent, false);
            }
            ((TextView) convertView).setText(item);
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new SpecialFilter();
        }

        public void updateResults(String[] results) {
            originalItems.clear();
            originalItems.addAll(new ArrayList<String>(Arrays.asList(results)));
            this.notifyDataSetChanged();

        }

        private class SpecialFilter extends Filter {

            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                FilterResults results = new FilterResults();


                ArrayList<String> list = new ArrayList<String>(originalItems);
                results.values = list;
                results.count = list.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems = (List<String>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }



    }



}
