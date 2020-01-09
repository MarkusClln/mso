package mso.eventium.datastorage.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


import java.lang.reflect.Type;

public class LatLngSerializer implements JsonSerializer<LatLng>, JsonDeserializer<LatLng> {

    public JsonElement serialize(final LatLng latLng, final Type type, final JsonSerializationContext context) {
        final JsonObject result = new JsonObject();
        result.addProperty("type", "Point");

        final JsonArray coordinates = new JsonArray();
        coordinates.add(latLng.latitude);
        coordinates.add(latLng.longitude);
        result.add("coordinates", coordinates);
        return result;
    }

    public LatLng deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) {

        final JsonObject jsonObject = json.getAsJsonObject();

        return new LatLng(
                jsonObject.getAsJsonArray("coordinates").get(0).getAsDouble(),
                jsonObject.getAsJsonArray("coordinates").get(1).getAsDouble()
        );
    }
}