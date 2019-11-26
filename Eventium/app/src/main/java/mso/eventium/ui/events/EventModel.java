package mso.eventium.ui.events;

import java.util.Date;

public class EventModel {



    String event_name;
    String event_description;
    String event_date;
    String event_time;
    int event_photo;
    int event_icon;
    String event_distance;
    EventModel(String name,String description, String date, String event_time, String distance, int event_photo, int event_icon) {
        this.event_name = name;
        this.event_description = description;
        this.event_date = date;
        this.event_photo = event_photo;
        this.event_icon = event_icon;
        this.event_distance = distance;
        this.event_time = event_time;
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

    public String getEvent_time() {
        return event_time;
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
}
