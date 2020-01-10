package mso.eventium.datastorage.util;

import java.util.List;

import mso.eventium.datastorage.entity.EventEntity;
import mso.eventium.datastorage.entity.PinEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BackendAPI {

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("pin")
    Call<PinEntity> createPin(@Body PinEntity pin, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("pin/getByName/{name}")
    Call<List<PinEntity>> getPinsByName(@Path("name") String pinName);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("pin/all")
    Call<List<PinEntity>> getAllPins(@Query("lat") double latitude, @Query("lng") double longitude, @Query("distance") double distance);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("pin/{id}")
    Call<PinEntity> getPinById(@Path("id") String id);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("event/new") //TODO im backend die alte methode löschen wenn nicht mehr verwendet
    Call<EventEntity> createEvent(@Body EventEntity event, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("event/{id}")
    Call<EventEntity> getEventById(@Path("id") String id);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("event/own")
    Call<List<EventEntity>> getCreatedEvents(@Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("event/fav")
    Call<List<EventEntity>> getFavoritedEvents(@Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("event/all")
    Call<List<EventEntity>> getAllEvents(@Query("lat") double latitude, @Query("lng") double longitude, @Query("distance") double distance);

}