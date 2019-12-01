package mso.eventium.model;

public class Event {


    private String event_name;
    private String event_description;
    private String event_date;
    private String event_time;
    private int event_photo;
    private int event_icon;
    private String event_distance;

    public Event(String name, String description, String date, String event_time, String distance, int event_photo, int event_icon) {
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
