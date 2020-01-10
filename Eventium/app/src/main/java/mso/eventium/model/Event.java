package mso.eventium.model;

import android.widget.Switch;

import java.util.Date;

import mso.eventium.R;

public class Event {

    private String event_name;
    private String event_description;
    private String pin_id;
    private String event_id;
    private String event_short_description;
    private Date event_date;
    private int event_photo;
    private int event_icon;
    private String event_distance;
    private boolean upvoted;
    private int event_points;

    private boolean downvoted;


    public Event(String name, String description, String event_short_description, Date date, String distance, int event_photo, String pin_id, boolean upvoted, boolean downvoted, int event_points, String event_id, CategoryEnum category) {
        this.event_name = name;
        this.event_description = description;
        this.event_date = date;
        this.event_photo = event_photo;
        this.event_icon = category == null ? 0 : category.getIcon();
        this.event_distance = distance;
        this.event_short_description = event_short_description;
        this.pin_id = pin_id;
        this.upvoted = upvoted;
        this.downvoted = downvoted;
        this.event_id = event_id;
        this.event_points = event_points;
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

    public Date getEvent_date() {
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

    public String getEvent_id() {
        return event_id;
    }

    public boolean isUpvoted() {
        return upvoted;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
    }

    public boolean isDownvoted() {
        return downvoted;
    }

    public void setDownvoted(boolean downvoted) {
        this.downvoted = downvoted;
    }

    public int getEvent_points() {
        return event_points;
    }

    public void setEvent_points(int event_points) {
        this.event_points = event_points;
    }

}
