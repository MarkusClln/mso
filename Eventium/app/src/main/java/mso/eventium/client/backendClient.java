package mso.eventium.client;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

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

    public StringRequest getAllEvents(final double lng, final double lat, final double distance, final Response.Listener<String> responseListener){

        final String url = "http://"+server_ip+"/event/all";

        //but params here
        Map<String, String> params = new HashMap<String, String>();
        params.put("lng", Double.toString(lng));
        params.put("lat",  Double.toString(lat));
        params.put("distance",  Double.toString(distance));

        Log.i("TESTEN", params.toString());



        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                url,
                responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            // here is params will add to your url using post method
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lng", Double.toString(lng));
                params.put("lat",  Double.toString(lat));
                params.put("distance",  Double.toString(distance));
                return params;
            }
        };

        return postRequest;
    }

    public StringRequest getAllPins(final double lat, final double lng, final double distance, final Response.Listener<String> responseListener, final Response.ErrorListener errorListener){

        final String url = "http://"+server_ip+"/pin/all";

        //but params here
        Map<String, String> params = new HashMap<String, String>();
        params.put("lng", Double.toString(lng));
        params.put("lat",  Double.toString(lat));
        params.put("distance",  Double.toString(distance));

        StringRequest getRequest = new StringRequest(
                Request.Method.POST,
                url,
                responseListener,
                errorListener

        ) {
            // here is params will add to your url using post method
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lng", Double.toString(lng));
                params.put("lat",  Double.toString(lat));
                params.put("distance",  Double.toString(distance));
                return params;
            }
        };

        return getRequest;
    }

    public JsonObjectRequest createPin(final String auth_token, final double lat, final double lng, final String name, final String desc, Response.Listener responseListener) {
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
                                    .put("coordinates", new JSONArray().put(lat).put(lng)))
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

    public JsonObjectRequest createEvent(final String auth_token, final String pin_id, final String name, final String desc, final String shortDesc, final Calendar calendar, final String category, Response.Listener responseListener) {
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
                            .put("shortDescription", shortDesc)
                            .put("date", calendar.getTime().toString())
                            .put("category", category));

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

    public StringRequest getPin(final String pin_id, final Response.Listener<String> responseListener){

        final String url = "http://"+server_ip+"/pin/"+pin_id;

        StringRequest getRequest = new StringRequest(
                Request.Method.GET,
                url,
                responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        return getRequest;
    }

    public JsonObjectRequest pushFavEvent(final String auth_token, final String pin_id, Response.Listener responseListener) {
        final String url = "http://" + server_ip + "/user/fav/"+pin_id;

        Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        };

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

        };

        return jsonObjReq;



    }

    public JsonObjectRequest deleteFavEvent(final String auth_token, final String pin_id, Response.Listener responseListener) {
        final String url = "http://" + server_ip + "/user/fav/"+pin_id;

        Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        };

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
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

        };

        return jsonObjReq;



    }

    public StringRequest getFavEvents(final String auth_token , final Response.Listener<String> responseListener){

        final String url = "http://"+server_ip+"/event/fav";



        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                url,
                responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + auth_token);
                return params;
            }
        };

        return postRequest;
    }

    public StringRequest getFavEventIDs(final String auth_token , final Response.Listener<String> responseListener, final Response.ErrorListener errorListener){

        final String url = "http://"+server_ip+"/user/fav_ids";


        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                url,
                responseListener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + auth_token);
                return params;
            }
        };

        return postRequest;
    }

    public StringRequest getOwnEvents(final String auth_token , final Response.Listener<String> responseListener){

        final String url = "http://"+server_ip+"/event/own";



        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                url,
                responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + auth_token);
                return params;
            }
        };

        return postRequest;
    }

    public StringRequest getPinByName(final String name , final Response.Listener<String> responseListener){

        final String url = "http://"+server_ip+"/pin/getByName/"+name;

        StringRequest getRequest = new StringRequest(
                Request.Method.GET,
                url,
                responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        return getRequest;
    }


    public StringRequest getAuth(final String auth_token , final Response.Listener<String> responseListener, Response.ErrorListener errorListener){

        final String url = "http://"+server_ip+"/user/auth0";

        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                url,
                responseListener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + auth_token);
                return params;
            }
        };

        return postRequest;
    }
}
