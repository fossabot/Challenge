package ycagri.challenge.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Simple Plain Old Java Object keeps information about venue.
 *
 * @author ycagri
 * @since 10.1.2015.
 */
public class Venue implements Parcelable {

    private final String mId;

    private final String mName;

    private final String mUrl;

    private final String mFormattedPhone;

    private final String mFacebookProfile;

    private final double mLatitude;

    private final double mLongitude;

    private final String mIconUrl = "";

    private final ArrayList<String> mPhotosList = new ArrayList<>();

    public Venue(String id, String name, String url, String phone, String facebookProfile, double lat, double lon) {
        mId = id;
        mName = name;
        mUrl = url;
        mFormattedPhone = phone;
        mFacebookProfile = facebookProfile;
        mLatitude = lat;
        mLongitude = lon;
    }

    private Venue(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mUrl = in.readString();
        mFormattedPhone = in.readString();
        mFacebookProfile = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        //mIconUrl = in.readString();
        //in.readStringList(mPhotosList);
    }

    public Venue(JSONObject jsonObject) {
        mId = jsonObject.optString("id");
        mName = jsonObject.optString("name");
        mUrl = jsonObject.optString("url");

        JSONObject contact = jsonObject.optJSONObject("contact");
        if (contact != null) {
            mFormattedPhone = contact.optString("formattedPhone");
            mFacebookProfile = "http://www.facebook.com/" + contact.optString("facebook");
        } else {
            mFormattedPhone = "";
            mFacebookProfile = "";
        }

        JSONObject location = jsonObject.optJSONObject("location");
        if (location != null) {
            mLatitude = location.optDouble("lat");
            mLongitude = location.optDouble("lng");
        } else {
            mLatitude = 0;
            mLongitude = 0;
        }

        JSONArray categories = jsonObject.optJSONArray("categories");
        if (categories != null) {
            for (int i = 0; i < categories.length(); i++) {
                JSONObject category = categories.optJSONObject(i);

                if (category.optBoolean("primary")) {
                    JSONObject icon = category.optJSONObject("icon");
                    //mIconUrl = icon.optString("prefix") + "88" + icon.optString("suffix");
                    break;
                }
            }
        }
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFormattedPhone() {
        return mFormattedPhone;
    }

    public String getFacebookProfile() {
        return mFacebookProfile;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public ArrayList<String> getPhotosList() {
        return mPhotosList;
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mUrl);
        dest.writeString(mFormattedPhone);
        dest.writeString(mFacebookProfile);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeString(mIconUrl);
        dest.writeStringList(mPhotosList);
    }

    public static Creator<Venue> CREATOR = new Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel source) {
            return new Venue(source);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };
}
