package mso.eventium.ui.create;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.adapter.AutoSuggestPinAdapter;
import mso.eventium.client.BackendClient;
import mso.eventium.datastorage.BackendService;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import mso.eventium.model.CategoryEnum;
import mso.eventium.model.MarkerModel;
import mso.eventium.ui.map.PinCreateDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class CreateFragment extends Fragment implements SelectCategoryDialogFragment.SingleCoiceListener {

    View root;
    MaterialButton btnSelectCategorie;
    MaterialButton btnCreateEvent;
    TextView dateView;
    TextView timeView;
    EditText nameView;
    EditText shortDescView;
    EditText descView;
    AutoCompleteTextView locationAutoCompleteTextView;
    String[][] autocomplete_ids = new String[0][0];
    private AutoSuggestPinAdapter autoSuggestPinAdapter;

    final Calendar myCalendar = Calendar.getInstance();

    public CreateFragment() {
        // Required empty public constructor
    }

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

        return root;
    }

    private void setupCategoriesButton() {
        btnSelectCategorie = root.findViewById(R.id.selectCategorieButton);
        btnSelectCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment selectCategorieDialog = new SelectCategoryDialogFragment(CreateFragment.this);
                selectCategorieDialog.setCancelable(false);
                selectCategorieDialog.show(getFragmentManager(), "SelectCategorieFragment");
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

    public void setUpCalenderPicker() {
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

    public void setUpTimePicker() {

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
                new TimePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, date, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });
    }

    private void setUpEditText() {
        nameView = root.findViewById(R.id.event_name);
        shortDescView = root.findViewById(R.id.event_shortDesc);
        descView = root.findViewById(R.id.event_desc);
    }

    public void setUpCreateButton() {
        btnCreateEvent = root.findViewById(R.id.buttonCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pin_name = locationAutoCompleteTextView.getText().toString();
                String pin_id = "";
                for (int i = 0; i < autocomplete_ids.length; i++) {
                    if (autocomplete_ids[i][1].equals(pin_name)) {
                        pin_id = autocomplete_ids[i][0];
                    }
                }

                String eventName = nameView.getText().toString();
                String eventShortDesc = shortDescView.getText().toString();
                String eventDesc = descView.getText().toString();
                String eventDate = myCalendar.getTime().toString();
                String eventCategorie = btnSelectCategorie.getText().toString();

                String output = "eventName=" + eventName + "\n" +
                        "eventShortDesc=" + eventShortDesc + "\n" +
                        "eventDesc=" + eventDesc + "\n" +
                        "eventDate=" + eventDate + "\n" +
                        "eventCategorie=" + eventCategorie + "\n" +
                        "pinID=" + pin_id;
                Toast.makeText(getContext(), output, Toast.LENGTH_LONG).show();
                if (eventName != "" && eventShortDesc != "" && eventDesc != "" && !eventCategorie.equals("Wähle aus!") && pin_id != "") {

                    final EventEntity newEvent = new EventEntity();
                    newEvent.setName(eventName);
                    newEvent.setShortDescription(eventShortDesc);
                    newEvent.setDescription(eventDesc);
                    newEvent.setCategory(CategoryEnum.valueOf(eventCategorie));
                    newEvent.setDate(myCalendar.getTime());
                    newEvent.setPinId(pin_id);

                    final BackendService backendService = BackendService.getInstance(getContext());
                    final Call<EventEntity> eventCall = backendService.createEvent(newEvent, ((MainActivity) getActivity()).getToken());
                    eventCall.enqueue(new Callback<EventEntity>() {

                        @Override
                        public void onResponse(Call<EventEntity> call, retrofit2.Response<EventEntity> response) {
                            Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_LONG).show();
                            Log.d("PIN CREATE", "created pin:" + response.body().toString());
                        }

                        @Override
                        public void onFailure(Call<EventEntity> call, Throwable t) {
                            Log.e("EVENT CREATE", "ERROR", t);

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Pflichtfelder bitte ausfüllen.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;

    public void setUpLocationEdit() {

        locationAutoCompleteTextView = root.findViewById(R.id.event_location);
        autoSuggestPinAdapter = new AutoSuggestPinAdapter(this.getContext(), android.R.layout.simple_dropdown_item_1line);
        locationAutoCompleteTextView.setThreshold(2);
        locationAutoCompleteTextView.setAdapter(autoSuggestPinAdapter);
        locationAutoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        locationAutoCompleteTextView.setText(autoSuggestPinAdapter.getObject(position));
                    }
                });

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(locationAutoCompleteTextView.getText())) {
                        makeApiCall(locationAutoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        locationAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void makeApiCall(final String text) {
        final Call<List<PinEntity>> pin = BackendService.getInstance(getContext()).getPinsByName(text);
        pin.enqueue(new Callback<List<PinEntity>>() {
            @Override
            public void onResponse(Call<List<PinEntity>> call, retrofit2.Response<List<PinEntity>> response) {
                final List<PinEntity> pins = response.body();
                final List<String> stringList = new ArrayList<>();

                autocomplete_ids = new String[pins.size()][2];
                for (int i = 0; i < pins.size(); i++) {
                    final PinEntity pin = pins.get(i);
                    autocomplete_ids[i][0] = pin.getId();
                    autocomplete_ids[i][1] = pin.getName();
                    stringList.add(pin.getName());
                }

                if (pins.size() == 0) {
                    stringList.add(text + " exisitert nicht");
                }

                //IMPORTANT: set data here and notify
                autoSuggestPinAdapter.setData(stringList);
                autoSuggestPinAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<PinEntity>> call, Throwable t) {
            }
        });
    }
}
