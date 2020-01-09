package mso.eventium.model;

import com.google.android.gms.maps.model.LatLng;

import com.google.maps.android.clustering.ClusterItem;

public class MarkerModel implements ClusterItem {

    private final LatLng position;
    private final String title;
    private final String snippet; //name on cluster

    public MarkerModel(LatLng position, String title, String snippet) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

}
