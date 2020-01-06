package mso.eventium.datastorage.entity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class PinEntity {

    private String name;
    private String description;
    @SerializedName("_id")
    private String id;
    private double distance;
    private LatLng location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
