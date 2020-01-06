package mso.eventium.datastorage.entity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class EventEntity {

    private String name;
    private String description;
    private String shortDescription;

    @SerializedName("_id")
    private String id;


    private double distance;
    private LatLng location;

}
