package ycagri.challenge.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ycagri.challenge.data.VenuePhoto;

/**
 * Created by vayen01 on 31/10/2017.
 */

public class PhotoDeserializer implements JsonDeserializer<List<VenuePhoto>> {
    @Override
    public List<VenuePhoto> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject response = (JsonObject) json;

        List<VenuePhoto> photos = null;

        JsonObject r = response.getAsJsonObject("response");
        if (r != null) {
            Gson gson = new Gson();
            JsonObject photo = r.getAsJsonObject("photos");
            if (photo != null)
                photos = gson.fromJson(photo.getAsJsonArray("items"), new TypeToken<List<VenuePhoto>>() {
                }.getType());

        }
        return photos;
    }
}
