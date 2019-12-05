package mso.eventium.client;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mso.eventium.model.Event;

public class backendClient {

    private String server_ip;

    public backendClient(String ip){
        this.server_ip=ip;
    }

    public JsonObjectRequest createUser(final String auth_token, final String email, final String auth0_id, final String name){

        final String url = "http://"+server_ip+"/user";

        //but params here
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("name", name);
        params.put("auth0_id", auth0_id);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONPost", response.toString());
                        //pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }

        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Content-Type", "application/json; charset=UTF-8");
            params.put("Authorization", "Bearer "+auth_token);
            return params;
        }};

        return jsonObjReq;
    }

    public JsonObjectRequest getAllEvents(final double lng, final double lat, final double distance, final List<Event> EventModels){

        final String url = "http://"+server_ip+"/pin/all";

        //but params here
        Map<String, String> params = new HashMap<String, String>();
        params.put("Lng", Double.toString(lng));
        params.put("Lat",  Double.toString(lat));
        params.put("Distance",  Double.toString(distance));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response.toString());
                            if(jsonObject.has("data") && !jsonObject.isNull("data"))
                            {
                                Log.d("jsonObject", jsonObject.toString());
                            }else{
                                // get message using error key
                            }
                        }catch (JSONException e){

                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }

        });

        return jsonObjReq;
    }

    public JsonObjectRequest createPin(final String auth_token, final double lng, final double lat, final String name, final String desc, Response.Listener responseListener) {
        final String url = "http://" + server_ip + "/pin";


        Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        };

        try {
            final JSONObject requestBody = new JSONObject()
                    .put("pin", new JSONObject()
                            .put("location", new JSONObject()
                                    .put("type", "Point")
                                    .put("coordinates", new JSONArray().put(lng).put(lat)))
                            .put("name", name)
                            .put("description", desc));

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    null,
                    responseListener,
                    responseErrorListener) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + auth_token);
                    return params;
                }

                @Override
                public byte[] getBody() {
                    try {
                        return requestBody.toString().getBytes(PROTOCOL_CHARSET);
                    } catch (UnsupportedEncodingException uee) {
                        // error handling
                        return null;
                    }
                }
            };

            return jsonObjReq;
        } catch (JSONException e) {
            return null;
        }


    }

    public JsonObjectRequest createEvent(final String auth_token, final String pin_id, final String name, final String desc, final String hortDesc, final Calendar calendar, Response.Listener responseListener) {
        final String url = "http://" + server_ip + "/event";

        Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        };

        try {
            final JSONObject requestBody = new JSONObject()
                    .put("event", new JSONObject()
                            .put("pin_id", pin_id)
                            .put("name", name)
                            .put("description", desc)
                            .put("shortDescription", hortDesc)
                            .put("date", calendar.getTime().toString()));

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    null,
                    responseListener,
                    responseErrorListener) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + auth_token);
                    return params;
                }

                @Override
                public byte[] getBody() {
                    try {
                        return requestBody.toString().getBytes(PROTOCOL_CHARSET);
                    } catch (UnsupportedEncodingException uee) {
                        // error handling
                        return null;
                    }
                }
            };

            return jsonObjReq;
        } catch (JSONException e) {
            return null;
        }


    }



}
