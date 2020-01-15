package mso.eventium.ui.user;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.auth0.android.result.UserProfile;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import mso.eventium.MainActivity;
import mso.eventium.R;
import mso.eventium.datastorage.BackendService;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import mso.eventium.helper.PinCreateHelper;
import mso.eventium.model.CategoryEnum;
import mso.eventium.model.Event;
import mso.eventium.ui.map.PinCreateDialog;
import retrofit2.Call;
import retrofit2.Callback;

//import com.auth0.android.Auth0;

public class UserFragment extends Fragment {

    private static String ARGS_TOKEN = "args_token";
    int points = 0;
    TextView TVPoints;
    TextView TVLevel;
    ProgressBar PBPoints;

    public static UserFragment newInstance(String token) {

        Bundle args = new Bundle();
        if (token != null) {
            args.putString(ARGS_TOKEN, token);
        }
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private String token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_user, container, false);

        token = getArguments().getString(ARGS_TOKEN);

        Button loginButton = root.findViewById(R.id.logout);
        Button deleteAccountButton = root.findViewById(R.id.deleteAccount);
        Button bla2Button = root.findViewById(R.id.buttonBla2);
        Button bla1Button = root.findViewById(R.id.buttonBla1);
        Button copyRightButton = root.findViewById(R.id.buttonCopyRight);
        Button personalButton = root.findViewById(R.id.buttonPersonal);
        TextView nameTV = root.findViewById(R.id.userName);
        View line = root.findViewById(R.id.line1);
        View image = root.findViewById(R.id.imageView);


        if (token != null) {
            ((MainActivity) getActivity()).getProfile();
            loginButton.setText("Abmelden");
            deleteAccountButton.setVisibility(View.VISIBLE);
            bla1Button.setVisibility(View.VISIBLE);
            bla2Button.setVisibility(View.VISIBLE);
            copyRightButton.setVisibility(View.VISIBLE);
            personalButton.setVisibility(View.VISIBLE);
            nameTV.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        } else {
            loginButton.setText("Anmelden");
            deleteAccountButton.setVisibility(View.INVISIBLE);
            bla1Button.setVisibility(View.INVISIBLE);
            bla2Button.setVisibility(View.INVISIBLE);
            copyRightButton.setVisibility(View.INVISIBLE);
            personalButton.setVisibility(View.INVISIBLE);
            nameTV.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);

        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button loginButton = root.findViewById(R.id.logout);
                Button deleteAccountButton = root.findViewById(R.id.deleteAccount);
                Button bla2Button = root.findViewById(R.id.buttonBla2);
                Button bla1Button = root.findViewById(R.id.buttonBla1);
                Button copyRightButton = root.findViewById(R.id.buttonCopyRight);
                Button personalButton = root.findViewById(R.id.buttonPersonal);
                TextView nameTV = root.findViewById(R.id.userName);
                View line = root.findViewById(R.id.line1);
                View image = root.findViewById(R.id.imageView);


                if (!((MainActivity) getActivity()).isLoggedIn()) {
                    ((MainActivity) getActivity()).login();

                    loginButton.setText("Abmelden");
                    deleteAccountButton.setVisibility(View.VISIBLE);
                    bla1Button.setVisibility(View.VISIBLE);
                    bla2Button.setVisibility(View.VISIBLE);
                    copyRightButton.setVisibility(View.VISIBLE);
                    personalButton.setVisibility(View.VISIBLE);
                    nameTV.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    image.setVisibility(View.VISIBLE);

                } else {

                    ((MainActivity) getActivity()).logout();

                    loginButton.setText("Anmelden");
                    deleteAccountButton.setVisibility(View.INVISIBLE);
                    bla1Button.setVisibility(View.INVISIBLE);
                    bla2Button.setVisibility(View.INVISIBLE);
                    copyRightButton.setVisibility(View.INVISIBLE);
                    personalButton.setVisibility(View.INVISIBLE);
                    nameTV.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.INVISIBLE);

                }
            }
        });

       /* Button dummiData = root.findViewById(R.id.dummiData);
        dummiData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDummiData();
            }
        });
        dummiData.setEnabled(token != null);*/

        return root;
    }


   /* public void createDummiData2(List<String> list) {

        Response.Listener responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("JSONPost", response.toString());
                //pDialog.hide();
            }
        };
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, Calendar.FEBRUARY);
        myCalendar.set(Calendar.DAY_OF_MONTH, 7);
        myCalendar.set(Calendar.HOUR, 11);
        myCalendar.set(Calendar.MINUTE, 00);


        // Holiday on ICE 1
        JsonObjectRequest req1 = ((MainActivity) getActivity()).backendClient.createEvent(token, Integer.toString(sapArena), "Holiday on Ice", "Die erfolgreichste Eisrevue der Welt gastiert auch 2020 mit neuer Show in der SAP Arena. In jeder Show erfindet sich Holiday on Ice neu und bleibt sich dabei doch stets treu.", "New Show", myCalendar, CategoryEnum.values()[3], responseListener);
        ((MainActivity) getActivity()).queue.add(req1);

        // Holiday on ICE 2
        myCalendar.set(Calendar.HOUR, 15);
        myCalendar.set(Calendar.MINUTE, 30);
        myCalendar.set(Calendar.DAY_OF_MONTH, 14);

        JsonObjectRequest req2 = ((MainActivity) getActivity()).backendClient.createEvent(token, Integer.toString(sapArena), "Holiday on Ice", "Die erfolgreichste Eisrevue der Welt gastiert auch 2020 mit neuer Show in der SAP Arena. In jeder Show erfindet sich Holiday on Ice neu und bleibt sich dabei doch stets treu.", "New Show", myCalendar, CategoryEnum.values()[3], responseListener);
        ((MainActivity) getActivity()).queue.add(req2);

        // Holiday on ICE 3
        myCalendar.set(Calendar.HOUR, 19);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 14);
        JsonObjectRequest req3 = ((MainActivity) getActivity()).backendClient.createEvent(token, Integer.toString(sapArena), "Holiday on Ice", "Die erfolgreichste Eisrevue der Welt gastiert auch 2020 mit neuer Show in der SAP Arena. In jeder Show erfindet sich Holiday on Ice neu und bleibt sich dabei doch stets treu.", "New Show", myCalendar, CategoryEnum.values()[3], responseListener);
        ((MainActivity) getActivity()).queue.add(req3);


        //Capitol 1
        myCalendar.set(Calendar.HOUR, 20);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 10);
        JsonObjectRequest req4 = ((MainActivity) getActivity()).backendClient.createEvent(token, Integer.toString(capitolMannheim), "Sascha im Quadrat", "Sascha im Quadrat, das sind der große und der kleine Sascha und Christof Brill. Geballtes Testosteron auf einer Bühne. Und eine Musikauswahl, die seinesgleichen sucht. Gespielt wird ALLES, was Spaß macht.\n" +
                "\n" +
                "Der Stargast im Februar ist Michaela Tischler.\n" +
                "„Gesang ist etwas sehr persönliches“", "Die beliebte Reihe im Casino. Stargast: Michaela Tischler", myCalendar, CategoryEnum.values()[0], responseListener);
        ((MainActivity) getActivity()).queue.add(req4);

        //Capitol 2
        myCalendar.set(Calendar.HOUR, 20);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 13);
        JsonObjectRequest req5 = ((MainActivity) getActivity()).backendClient.createEvent(token, Integer.toString(capitolMannheim), "Evita", "Die mitreißende Geschichte der Eva Duarte, die vom argentinischen Volk liebevoll Evita genannt wurde. Andrew Lloyd Webber und Tim Rice widmeten der auch heute noch faszinierenden und zugleich höchst umstrittenen „First Lady“ Argentiniens eines ihrer spektakulärsten Musicals. Es erzählt den Aufstieg eines einfachen Provinz-Mädchens zur Schauspielerin und in die High Society von Buenos Aires, wo sie schließlich den hohen Militär Juan Perón kennenlernt.", "Eine große Geschichte. Eine große Frau. Ein großes Musical.", myCalendar, CategoryEnum.values()[1], responseListener);
        ((MainActivity) getActivity()).queue.add(req5);

        //Schloss
        myCalendar.set(Calendar.HOUR, 18);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 8);
        JsonObjectRequest req6 = ((MainActivity) getActivity()).backendClient.createEvent(token, Integer.toString(schlossMannheim), "5. Mannheimer Schlosskonzert", "Franz Xaver Richter: Adagio und Fuge g-Moll\n" +
                "Michael Haydn: Violoncellokonzert B-Dur\n" +
                "Peter von Winter: Concertino für Violoncello, Klarinette und Orchester\n" +
                "Carl Stamitz: Sinfonia concertante D-Dur für Violine principale und Orchester op. 2, Nr. 2\n" +
                "Julia Hagen, Violoncello\n" +
                "Paul Meyer, Dirigent", "Fürstliche Soiree", myCalendar, CategoryEnum.values()[2], responseListener);
        ((MainActivity) getActivity()).queue.add(req6);

        //Connex 1
        myCalendar.set(Calendar.HOUR, 23);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 14);
        JsonObjectRequest req7 = ((MainActivity) getActivity()).backendClient.createEvent(token,Integer.toString(msConnexion), "TWIZE", "TWIZE bietet euch immer auf 2 Floors. TECHNO & DRUM N BASS mit DJ´s der Szene.\n" +
                "\n" +
                "LINE UP\n" +
                "\n" +
                "FOLGT !", "TWIZE - Techno & Drum N Bass", myCalendar, CategoryEnum.values()[0], responseListener);
        ((MainActivity) getActivity()).queue.add(req7);

        //Connex 2
        myCalendar.set(Calendar.HOUR, 23);
        myCalendar.set(Calendar.MINUTE, 00);
        myCalendar.set(Calendar.DAY_OF_MONTH, 29);
        JsonObjectRequest req8 = ((MainActivity) getActivity()).backendClient.createEvent(token, Integer.toString(msConnexion), "All Night Long", "Und weiter geht es mit der ALL NIGHT LONG Reihe, bei der euch\n" +
                "SEIMEN DEXTER + AUDIOAPPEAR von DEXIT Techno Events jeweils ein 3,5 Stunden Set auf die Lauscher klatschen werden. Dieses ALL NIGHT LONG wird einen Abriss vom feinsten geben..\n" +
                "\n" +
                "\n" +
                "JÄGERMEISTER 1 EURO ALL NIGHT LONG\n" +
                "\n" +
                "Einlass ab 18 Jahren. Ausweispflicht - kein Muttizettel !", "Seimen Dexter + Audioappear", myCalendar, CategoryEnum.values()[1], responseListener);
        ((MainActivity) getActivity()).queue.add(req8);


        //Dorint
        myCalendar.set(Calendar.HOUR, 19);
        myCalendar.set(Calendar.MINUTE, 30);
        myCalendar.set(Calendar.DAY_OF_MONTH, 26);
        JsonObjectRequest req9 = ((MainActivity) getActivity()).backendClient.createEvent(token, Integer.toString(dorint), "Heringsessen", "Alles hat ein Ende, auch eine feuerianische Fasnachtskampagne. Aber auch der letzte Tag wird noch einmal standesgemäß begangen, zwar im trauernden Schwarz, aber nichtsdestotrotz lustig vergnügt. Bei hervorragendem Hering lassen wir unsere Kampagne noch einmal Revue passieren. Im Kartenpreis enthalten Hering satt von der Deutsche See sowie reichhaltige Beilagen.", "Herringsessen im Dorint", myCalendar, CategoryEnum.values()[2], responseListener);
        ((MainActivity) getActivity()).queue.add(req9);

    }
*/
    private void getPoints() {

        Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int tmp = 0;
                try {
                    JSONArray events = new JSONArray(response);
                    for (int j = 0; j < events.length(); j++) {
                        JSONObject event = events.getJSONObject(j);
                        tmp += event.getJSONArray("likedUsers").length();
                        tmp -= event.getJSONArray("dislikedUsers").length();
                    }
                    points = tmp;
                    setPoints();
                } catch (JSONException e) {

                }

            }
        };
        StringRequest req1 = ((MainActivity) getActivity()).backendClient.getOwnEvents(token, responseListener);
        ((MainActivity) getActivity()).queue.add(req1);
    }

    private void setPoints() {
        int level_2 = (int) Math.exp(2); // 7
        int level_3 = (int) Math.exp(3); // 20
        int level_4 = (int) Math.exp(4); // 54
        int level_5 = (int) Math.exp(5); // 148
        int level_6 = (int) Math.exp(6); // 403
        int level_7 = (int) Math.exp(7); // 1096
        int level_8 = (int) Math.exp(8); // 2980
        int level_9 = (int) Math.exp(9); // 8103
        int level_10 = (int) Math.exp(10); //22026


        if (points < level_2) {
            TVLevel.setText("Level 1");
            double progress = (double) points / level_2 * 100;
            PBPoints.setProgress((int) progress);
        } else {
            if (points < level_3) {
                TVLevel.setText("Level 2");
                double progress = (double) (points - level_2) / (level_3 - level_2) * 100;
                PBPoints.setProgress((int) progress);
            } else {
                if (points < level_4) {
                    TVLevel.setText("Level 3");
                    double progress = (double) (points - level_3) / (level_4 - level_3) * 100;
                    PBPoints.setProgress((int) progress);
                } else {
                    if (points < level_5) {
                        TVLevel.setText("Level 4");
                        double progress = (double) (points - level_4) / (level_5 - level_4) * 100;
                        PBPoints.setProgress((int) progress);
                    } else {
                        if (points < level_6) {
                            TVLevel.setText("Level 5");
                            double progress = (double) (points - level_5) / (level_6 - level_5) * 100;
                            PBPoints.setProgress((int) progress);
                        } else {
                            if (points < level_7) {
                                TVLevel.setText("Level 6");
                                double progress = (double) (points - level_6) / (level_7 - level_6) * 100;
                                PBPoints.setProgress((int) progress);
                            } else {
                                if (points < level_8) {
                                    TVLevel.setText("Level 7");
                                    double progress = (double) (points - level_7) / (level_8 - level_7) * 100;
                                    PBPoints.setProgress((int) progress);
                                } else {
                                    if (points < level_9) {
                                        TVLevel.setText("Level 8");
                                        double progress = (double) (points - level_8) / (level_9 - level_8) * 100;
                                        PBPoints.setProgress((int) progress);
                                    } else {
                                        if (points < level_10) {
                                            TVLevel.setText("Level 9");
                                            double progress = (double) (points - level_9) / (level_10 - level_9) * 100;
                                            PBPoints.setProgress((int) progress);
                                        } else {
                                            TVLevel.setText("Level 10");
                                            PBPoints.setProgress(100);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        TVPoints.setText(points + " Punkte");
    }


}
