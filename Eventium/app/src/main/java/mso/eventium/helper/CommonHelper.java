package mso.eventium.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CommonHelper {

    public CommonHelper(){ }

    public double CalculateDistance(double lat1, double lon1, double lat2, double lon2){
        return distance(lat1, lon1, lat2, lon2);
    }

    public String FormatDate(String dateString){
        try {
            Calendar cal = this.GetCalendarFromDateString(dateString);
            return cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String FormatTime(String dateString){
        try {
            Calendar cal = this.GetCalendarFromDateString(dateString);
            return String.format("%02d:%02d", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private Calendar GetCalendarFromDateString(String dateString){
        try{
            //TODO: 24h Scheiß geht ned mit HH
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            Date date = formatter.parse(dateString);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
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
