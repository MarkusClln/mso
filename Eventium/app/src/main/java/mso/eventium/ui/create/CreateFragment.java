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
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.adapter.AutoSuggestPinAdapter;
import mso.eventium.client.BackendClient;
import mso.eventium.datastorage.BackendService;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import mso.eventium.helper.PinCreateHelper;
import mso.eventium.model.CategoryEnum;
import mso.eventium.model.MarkerModel;
import mso.eventium.ui.map.PinCreateDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class CreateFragment extends Fragment implements SelectCategoryDialogFragment.SingleCoiceListener {
    private static String ARGS_TOKEN = "args_token";
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
    private String token;
    final Calendar myCalendar = Calendar.getInstance();

//    public CreateFragment() {
//        // Required empty public constructor
//    }

    public static CreateFragment newInstance(String token) {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        if (token != null) {
            args.putString(ARGS_TOKEN, token);
        }
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
        token = getArguments().getString(ARGS_TOKEN);
        setUpEditText();
        setUpTimePicker();
        setUpCalenderPicker();
        setupCategoriesButton();
        setUpCreateButton();
        setUpLocationEdit();

        Button dummiData = root.findViewById(R.id.buttonCreateDummyData);
        dummiData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDummyData();
            }
        });
        //dummiData.setEnabled(token != null);


        return root;
    }



    public void createDummyData() {
        final List<PinEntity> pins = new LinkedList<>();
        pins.add(createPin(49.463970, 8.517586, "SAP Arena Mannheim", "Modernes Stadion mit Glas & Alu für Eishockeyspiele & andere Sportveranstaltungen sowie große Konzerte."));
        pins.add(createPin(49.497550, 8.472584, "Capitol Mannheim", "Konzerthalle, Comedy-Club & Veranstaltungszentrum in einem ehemaligen Kino im Art-déco-Stil."));
        pins.add(createPin(49.483346, 8.461786, "Barockschloss Mannheim", "Großes Barockschloss mit restaurierten Ausstellungsräumen, 5 Flügeln & 400 m² großem Hof."));
        pins.add(createPin(49.466663, 8.259124, "MS Connexion Complex", "Tanzpartys für Freunde von Techno, Drum'n'Bass und Gothic bei Neonlicht in den Räumen einer umgebauten Fabrik."));
        pins.add(createPin(49.485700, 8.476469, "Dorint Kongresshotel Mannheim", "Konferenzhotel mit direkter Verbindung zum Congress Center Rosengarten"));
        pins.add(createPin(49.469556866111, 8.4822070597222, "Hochschule Mannheim", "Ein grauenhafter Ort"));
        final List<PinCreateHelper> pinIds = new ArrayList<PinCreateHelper>();

        final Callback<PinEntity> createPinCallback = new Callback<PinEntity>() {

            @Override
            public void onResponse(Call<PinEntity> call, retrofit2.Response<PinEntity> response) {
                //create the events when all pins created
                pinIds.add(new PinCreateHelper(response.body().getName(),response.body().getId()));

                if (pinIds.size() == 5) {
                    createDummiData3(pinIds);
                }
            }

            @Override
            public void onFailure(Call<PinEntity> call, Throwable t) {
                Log.e("PIN CREATE", "ERROR", t);

            }
        };

        final BackendService backendService = BackendService.getInstance(getContext());
        for (PinEntity pin : pins) {
            final Call<PinEntity> eventCall = backendService.createPin(pin, token);
            eventCall.enqueue(createPinCallback);
        }
    }

    private PinEntity createPin(double lat, double lng, String name, String description) {
        final PinEntity pin = new PinEntity();
        pin.setName(name);
        pin.setDescription(description);
        pin.setLocation(new LatLng(lat, lng));
        return pin;
    }

    public void createDummiData3(List<PinCreateHelper> pins){

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, Calendar.FEBRUARY);
        myCalendar.set(Calendar.DAY_OF_MONTH, 7);
        myCalendar.set(Calendar.HOUR, 11);
        myCalendar.set(Calendar.MINUTE, 00);

        //Holiday on Ice 1
        final EventEntity newEvent = new EventEntity();
        newEvent.setName("Holiday on Ice");
        newEvent.setShortDescription("New Show");
        newEvent.setDescription("Die erfolgreichste Eisrevue der Welt gastiert auch 2020 mit neuer Show in der SAP Arena. In jeder Show erfindet sich Holiday on Ice neu und bleibt sich dabei doch stets treu.");
        newEvent.setCategory(CategoryEnum.values()[3]);
        newEvent.setDate(myCalendar.getTime());
        newEvent.setPinId(getCorrectPinId(pins, "SAP Arena Mannheim"));
        createDummyEvent(newEvent);

        //Holiday on Ice 2
        myCalendar.set(Calendar.HOUR, 15);
        myCalendar.set(Calendar.MINUTE, 30);
        myCalendar.set(Calendar.DAY_OF_MONTH, 14);

        final EventEntity holidayOnIce2 = new EventEntity();
        holidayOnIce2.setName("Holiday on Ice");
        holidayOnIce2.setShortDescription("New Show");
        holidayOnIce2.setDescription("Die erfolgreichste Eisrevue der Welt gastiert auch 2020 mit neuer Show in der SAP Arena. In jeder Show erfindet sich Holiday on Ice neu und bleibt sich dabei doch stets treu.");
        holidayOnIce2.setCategory(CategoryEnum.values()[3]);
        holidayOnIce2.setDate(myCalendar.getTime());
        holidayOnIce2.setPinId(getCorrectPinId(pins, "SAP Arena Mannheim"));
        createDummyEvent(holidayOnIce2);

        // Holiday on ICE 3
        myCalendar.set(Calendar.HOUR, 19);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 14);

        final EventEntity holidayOnIce3 = new EventEntity();
        holidayOnIce3.setName("Holiday on Ice");
        holidayOnIce3.setShortDescription("New Show");
        holidayOnIce3.setDescription("Die erfolgreichste Eisrevue der Welt gastiert auch 2020 mit neuer Show in der SAP Arena. In jeder Show erfindet sich Holiday on Ice neu und bleibt sich dabei doch stets treu.");
        holidayOnIce3.setCategory(CategoryEnum.values()[3]);
        holidayOnIce3.setDate(myCalendar.getTime());
        holidayOnIce3.setPinId(getCorrectPinId(pins, "SAP Arena Mannheim"));
        createDummyEvent(holidayOnIce3);

        //Capitol 1
        myCalendar.set(Calendar.HOUR, 20);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 10);

        final EventEntity capitol1 = new EventEntity();
        capitol1.setName("\"Sascha im Quadrat");
        capitol1.setShortDescription("Die beliebte Reihe im Casino. Stargast: Michaela Tischler");
        capitol1.setDescription("Sascha im Quadrat, das sind der große und der kleine Sascha und Christof Brill. Geballtes Testosteron auf einer Bühne. Und eine Musikauswahl, die seinesgleichen sucht. Gespielt wird ALLES, was Spaß macht.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Der Stargast im Februar ist Michaela Tischler.\\n\" +\n" +
                "                \"„Gesang ist etwas sehr persönliches“");
        capitol1.setCategory(CategoryEnum.values()[0]);
        capitol1.setDate(myCalendar.getTime());
        capitol1.setPinId(getCorrectPinId(pins, "Capitol Mannheim"));
        createDummyEvent(capitol1);

        //Capitol 2
        myCalendar.set(Calendar.HOUR, 20);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 13);

        final EventEntity capitol2 = new EventEntity();
        capitol2.setName("Evita");
        capitol2.setShortDescription("Eine große Geschichte. Eine große Frau. Ein großes Musical.");
        capitol2.setDescription("Die mitreißende Geschichte der Eva Duarte, die vom argentinischen Volk liebevoll Evita genannt wurde. Andrew Lloyd Webber und Tim Rice widmeten der auch heute noch faszinierenden und zugleich höchst umstrittenen „First Lady“ Argentiniens eines ihrer spektakulärsten Musicals. Es erzählt den Aufstieg eines einfachen Provinz-Mädchens zur Schauspielerin und in die High Society von Buenos Aires, wo sie schließlich den hohen Militär Juan Perón kennenlernt.");
        capitol2.setCategory(CategoryEnum.values()[1]);
        capitol2.setDate(myCalendar.getTime());
        capitol2.setPinId(getCorrectPinId(pins, "Capitol Mannheim"));
        createDummyEvent(capitol2);

        //Schloss
        myCalendar.set(Calendar.HOUR, 18);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 8);

        final EventEntity schloss = new EventEntity();
        schloss.setName("5. Mannheimer Schlosskonzert");
        schloss.setShortDescription("Fürstliche Soiree");
        schloss.setDescription("Franz Xaver Richter: Adagio und Fuge g-Moll\n" +
                "Michael Haydn: Violoncellokonzert B-Dur\n" +
                "Peter von Winter: Concertino für Violoncello, Klarinette und Orchester\n" +
                "Carl Stamitz: Sinfonia concertante D-Dur für Violine principale und Orchester op. 2, Nr. 2\n" +
                "Julia Hagen, Violoncello\n" +
                "Paul Meyer, Dirigent");
        schloss.setCategory(CategoryEnum.values()[2]);
        schloss.setDate(myCalendar.getTime());
        schloss.setPinId(getCorrectPinId(pins, "Barockschloss Mannheim"));
        createDummyEvent(schloss);

        //Connex 1
        myCalendar.set(Calendar.HOUR, 23);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 14);

        final EventEntity connex1 = new EventEntity();
        connex1.setName("TWIZE");
        connex1.setShortDescription("TWIZE - Techno & Drum N Bass");
        connex1.setDescription("TWIZE bietet euch immer auf 2 Floors. TECHNO & DRUM N BASS mit DJ´s der Szene.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"LINE UP\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"FOLGT !");
        connex1.setCategory(CategoryEnum.values()[0]);
        connex1.setDate(myCalendar.getTime());
        connex1.setPinId(getCorrectPinId(pins, "MS Connexion Complex"));
        createDummyEvent(connex1);

        //Connex 2
        myCalendar.set(Calendar.HOUR, 23);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 29);

        final EventEntity connex2 = new EventEntity();
        connex2.setName("All Night Long");
        connex2.setShortDescription("Seimen Dexter + Audioappear");
        connex2.setDescription("Und weiter geht es mit der ALL NIGHT LONG Reihe, bei der euch\\n\" +\n" +
                "                \"SEIMEN DEXTER + AUDIOAPPEAR von DEXIT Techno Events jeweils ein 3,5 Stunden Set auf die Lauscher klatschen werden. Dieses ALL NIGHT LONG wird einen Abriss vom feinsten geben..\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"JÄGERMEISTER 1 EURO ALL NIGHT LONG\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Einlass ab 18 Jahren. Ausweispflicht - kein Muttizettel !");
        connex2.setCategory(CategoryEnum.values()[1]);
        connex2.setDate(myCalendar.getTime());
        connex2.setPinId(getCorrectPinId(pins, "MS Connexion Complex"));
        createDummyEvent(connex2);

        //Dorint
        myCalendar.set(Calendar.HOUR, 19);
        myCalendar.set(Calendar.MINUTE, 30);
        myCalendar.set(Calendar.DAY_OF_MONTH, 26);

        final EventEntity dorint = new EventEntity();
        dorint.setName("Heringsessen");
        dorint.setShortDescription("Herringsessen im Dorint");
        dorint.setDescription("Alles hat ein Ende, auch eine feuerianische Fasnachtskampagne. Aber auch der letzte Tag wird noch einmal standesgemäß begangen, zwar im trauernden Schwarz, aber nichtsdestotrotz lustig vergnügt. Bei hervorragendem Hering lassen wir unsere Kampagne noch einmal Revue passieren. Im Kartenpreis enthalten Hering satt von der Deutsche See sowie reichhaltige Beilagen.");
        dorint.setCategory(CategoryEnum.values()[2]);
        dorint.setDate(myCalendar.getTime());
        dorint.setPinId(getCorrectPinId(pins, "Dorint Kongresshotel Mannheim"));
        createDummyEvent(dorint);
    }

    private String getCorrectPinId(List<PinCreateHelper> pins, String location){
        for(int i = 0; i < pins.size(); i++){
            if(pins.get(i).getPinName().equals(location)){
                return pins.get(i).getPinId();
            }
        }
        return "";
    }

    private void createDummyEvent(EventEntity newEvent){
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
