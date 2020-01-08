package mso.eventium.datastorage;

import android.content.Context;
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
import java.util.HashMap;
import java.util.List;

import mso.eventium.R;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

/**
 * get Data from backend
 * use as singleton
 * <p>
 * //TODO caching
 */
public class BackendService {

    private BackendAPI backendAPI;
    private static HashMap<String, EventEntity> eventCache = new HashMap<>();

    private static BackendService backendService;


    public Call<PinEntity> getPinByName(String name, String bearerToken) {
        return backendAPI.getPinByName(name, bearerToken);
    }

    public Call<List<PinEntity>> getAllPins(final double latitude, final double longitude, final double distance) {
        return backendAPI.getAllPins(latitude, longitude, distance);
    }

    public Call<EventEntity> getEventById(String id) {
        return backendAPI.getEventById(id);
    }


    private BackendService(final Context context) {
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

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        final Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, latLngJsonDeserializer).create();
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
