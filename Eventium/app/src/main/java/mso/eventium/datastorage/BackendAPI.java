package mso.eventium.datastorage;

import java.util.List;

import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BackendAPI {

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("pin/getByName/{name}")
    Call<PinEntity> getPinByName(@Path("name") String pinName, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("pin/all")
    Call<List<PinEntity>> getAllPins(@Query("lat") double latitude, @Query("lng") double longitude, @Query("distance") double distance);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("event/{id}")
    Call<EventEntity> getEventById(@Path("id") String id);
}