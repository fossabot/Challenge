package ycagri.challenge;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ycagri.challenge.data.Venue;

/**
 * Created by vayen01 on 17/10/2017.
 */

public class VenueDeserializer implements JsonDeserializer<List<Venue>> {
    @Override
    public List<Venue> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject response = (JsonObject) json;

        List<Venue> venues = null;

        JsonObject r = response.getAsJsonObject("response");
        if (r != null) {
            Gson gson = new Gson();
            venues = gson.fromJson(r.getAsJsonArray("venues"), new TypeToken<List<Venue>>() {
            }.getType());

        }
        return venues;
    }
}
