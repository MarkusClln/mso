package mso.eventium.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CommonHelper {

    public CommonHelper() {
    }

    public double CalculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return distance(lat1, lon1, lat2, lon2);
    }

    public String FormatDate(Date dateString) {
        final DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
        return formatter.format(dateString);
    }

    public String FormatTime(Date dateString) {
        final DateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(dateString);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
