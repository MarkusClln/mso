package mso.eventium.datastorage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import mso.eventium.R;
import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import mso.eventium.datastorage.util.BackendAPI;
import mso.eventium.datastorage.util.LatLngSerializer;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
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

    public Call<PinEntity> createPin(PinEntity pin, String bearerToken) {
        return backendAPI.createPin(pin, "Bearer " + bearerToken);
    }

    public Call<List<PinEntity>> getPinsByName(final String name) {
        return backendAPI.getPinsByName(name);
    }

    public Call<List<PinEntity>> getAllPins(final double latitude, final double longitude, final double distance) {
        return backendAPI.getAllPins(latitude, longitude, distance);
    }

    public Call<EventEntity> createEvent(EventEntity event, String bearerToken) {
        return backendAPI.createEvent(event, "Bearer " + bearerToken);
    }

    public Call<EventEntity> getEventById(String id) {
        return backendAPI.getEventById(id);
    }

    public Call<List<EventEntity>> getFavoritedEvents(final String bearerToken) {
        return backendAPI.getFavoritedEvents("Bearer " + bearerToken);
    }

    public Call<List<EventEntity>> getCreatedEvents(final String bearerToken) {
        return backendAPI.getCreatedEvents("Bearer " + bearerToken);
    }

    public Call<List<EventEntity>> getAllEvents(final double latitude, final double longitude, final double distance) {
        return backendAPI.getAllEvents(latitude, longitude, distance);
    }


    private BackendService(final Context context) {
        Interceptor rewriteCacheControlInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (hasNetwork(context)) { //currently no caching when online because not sure what happens after new event creation //todo uncomment and check if new events are fetched
//                    int maxAge = 60; // read from cache for 1 minute
//                    return originalResponse.newBuilder()
//                            .header("Cache-Control", "public, max-age=" + maxAge)
//                            .build();
                    return originalResponse;
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };

        int cacheSize = 5 * 1024 * 1024; // 10 MB
        final Cache cache = new Cache(context.getCacheDir(), cacheSize);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(rewriteCacheControlInterceptor)
                .build();

        final Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngSerializer()).create();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + context.getResources().getString(R.string.IP_Server))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        backendAPI = retrofit.create(BackendAPI.class);
    }


    private boolean hasNetwork(Context context) {
        boolean isConnected = false; // Initial Value
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            isConnected = true;
        }
        return isConnected;
    }

    public static BackendService getInstance(Context context) {
        if (backendService == null) {
            backendService = new BackendService(context);
        }
        return backendService;
    }
}
