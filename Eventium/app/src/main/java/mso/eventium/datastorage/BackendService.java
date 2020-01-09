package mso.eventium.datastorage;

import android.content.Context;

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
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import mso.eventium.datastorage.util.BackendAPI;
import mso.eventium.datastorage.util.LatLngSerializer;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * get Data from backend
 * use as singleton
 */
public class BackendService {

    private static BackendService backendService;

    private BackendAPI backendAPI;

    public Call<PinEntity> getPinByName(String name, String bearerToken) {
        return backendAPI.getPinByName(name, "Bearer " + bearerToken);
    }

    public Call<List<PinEntity>> getAllPins(final double latitude, final double longitude, final double distance) {
        return backendAPI.getAllPins(latitude, longitude, distance);
    }

    public Call<EventEntity> getEventById(String id) {
        return backendAPI.getEventById(id);
    }

    public Call<PinEntity> createPin(PinEntity pin, String bearerToken) {
        return backendAPI.createPin(pin, "Bearer " + bearerToken);
    }


    private BackendService(final Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        final Cache cache = new Cache(context.getCacheDir(), cacheSize);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        final Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngSerializer()).create();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + context.getResources().getString(R.string.IP_Server))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        backendAPI = retrofit.create(BackendAPI.class);
    }

    public static BackendService getInstance(Context context) {
        if (backendService == null) {
            backendService = new BackendService(context);
        }
        return backendService;
    }
}
