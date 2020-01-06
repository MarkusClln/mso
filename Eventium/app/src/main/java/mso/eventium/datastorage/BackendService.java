package mso.eventium.datastorage;

import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import mso.eventium.R;
import mso.eventium.datastorage.entity.PinEntity;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * get Data from backend
 * use as singleton
 *
 *       //TODO caching
 */
public class BackendService {

    private BackendAPI backendAPI;
    private static BackendService backendService;

    public Call<PinEntity> getPinByName(String name, String bearerToken) {
        return backendAPI.getPinByName(name, bearerToken);
    }

    public Call<List<PinEntity>> getAllPins(final double latitude, final double longitude, final double distance) {
        return backendAPI.getAllPins(latitude, longitude, distance);
    }


    private BackendService(String serverIp) {
        //custom converter
        final JsonDeserializer<LatLng> latLngJsonDeserializer = new JsonDeserializer<LatLng>() {
            @Override
            public LatLng deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                final JsonObject jsonObject = json.getAsJsonObject();

                return new LatLng(
                        jsonObject.getAsJsonArray("coordinates").get(0).getAsDouble(),
                        jsonObject.getAsJsonArray("coordinates").get(1).getAsDouble()
                );
            }
        };

        final Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, latLngJsonDeserializer).create();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverIp)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        backendAPI = retrofit.create(BackendAPI.class);
    }

    public static BackendService getInstance(Resources resources) {
        if (backendService == null) {
            backendService = new BackendService(resources.getString(R.string.IP_Server));
        }
        return backendService;
    }
}
