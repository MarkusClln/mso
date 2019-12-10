package mso.eventium.model;

import android.widget.Switch;

import mso.eventium.R;

public class Event {


    private String event_name;
    private String event_description;
    private String pin_id;
    private String event_id;
    private String event_short_description;
    private String event_date;
    private int event_photo;
    private int event_icon;
    private String event_distance;
    private boolean saved;
    private String category;

    public static final String[] categories = {"Bar", "Disco", "Essen"};

    public static int getCategorieIcon(String category){
        switch (category){
            case "Bar":
                return R.drawable.ic_flaschen;
            case "Disco":
                return R.drawable.ic_cocktails;
            case "Essen":
                return R.drawable.ic_best_choice;
        }
        return 0;
    }




    public Event(String name, String description, String event_short_description, String date, String distance, int event_photo, String pin_id, boolean saved, String event_id, String category) {
        this.event_name = name;
        this.event_description = description;
        this.event_date = date;
        this.event_photo = event_photo;
        this.event_icon = getCategorieIcon(category);
        this.event_distance = distance;
        this.event_short_description = event_short_description;
        this.pin_id = pin_id;
        this.saved = saved;
        this.event_id = event_id;
        this.category = category;
    }

    public String getName() {
        return event_name;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getPin_id() {
        return pin_id;
    }

    public int getEvent_photo() {
        return event_photo;
    }

    public int getEvent_icon() {
        return event_icon;
    }

    public String getEvent_distance() {
        return event_distance;
    }

    public String getEvent_short_description() {
        return event_short_description;
    }

    public boolean isSaved() {
        return saved;
    }

    public String getEvent_id() {
        return event_id;
    }


    public void setSaved(boolean saved) {
        this.saved = saved;
    }

}
