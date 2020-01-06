package mso.eventium.datastorage.entity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class EventEntity {

    @SerializedName("_id")
    private String id;
    private String name;
    private String description;
    private String shortDescription;

    @SerializedName("pin_id")
    private String pinId;
    @SerializedName("user_id")
    private String userId;

    private String category;
    private Date date;

    private List<String> usersThatLiked;
    private List<String> usersThatDisliked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPinId() {
        return pinId;
    }

    public void setPinId(String pinId) {
        this.pinId = pinId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getUsersThatLiked() {
        return usersThatLiked;
    }

    public void setUsersThatLiked(List<String> usersThatLiked) {
        this.usersThatLiked = usersThatLiked;
    }

    public List<String> getUsersThatDisliked() {
        return usersThatDisliked;
    }

    public void setUsersThatDisliked(List<String> usersThatDisliked) {
        this.usersThatDisliked = usersThatDisliked;
    }
}
