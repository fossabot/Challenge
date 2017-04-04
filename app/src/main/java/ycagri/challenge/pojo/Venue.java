package ycagri.challenge.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by YigitCagri on 10.1.2015.
 */
public class Venue implements Parcelable {

    private String mId;
    private String mName;
    private String mUrl;
    private String mFormattedPhone;
    private String mFacebookProfile;
    private double mLatitude;
    private double mLongitude;
    private String mIconUrl;
    private ArrayList<String> mPhotosList;

    private Venue(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mUrl = in.readString();
        mFormattedPhone = in.readString();
        mFacebookProfile = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mIconUrl = in.readString();
        in.readStringList(mPhotosList);
    }

    public Venue(JSONObject jsonObject) {
        mId = jsonObject.optString("id");
        mName = jsonObject.optString("name");
        mUrl = jsonObject.optString("url");

        JSONObject contact = jsonObject.optJSONObject("contact");
        if (contact != null) {
            mFormattedPhone = contact.optString("formattedPhone");
            mFacebookProfile = "http://www.facebook.com/" + contact.optString("facebook");
        }

        JSONObject location = jsonObject.optJSONObject("location");
        if (location != null) {
            mLatitude = location.optDouble("lat");
            mLongitude = location.optDouble("lng");
        }

        JSONArray categories = jsonObject.optJSONArray("categories");
        if (categories != null) {

            for (int i = 0; i < categories.length(); i++) {
                JSONObject category = categories.optJSONObject(i);

                if (category.optBoolean("primary")) {
                    JSONObject icon = category.optJSONObject("icon");
                    mIconUrl = icon.optString("prefix") + "88" + icon.optString("suffix");
                    break;
                }
            }
        }
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getFormattedPhone() {
        return mFormattedPhone;
    }

    public void setFormattedPhone(String formattedPhone) {
        this.mFormattedPhone = formattedPhone;
    }

    public String getFacebookProfile() {
        return mFacebookProfile;
    }

    public void setFacebookProfile(String facebookProfile) {
        this.mFacebookProfile = facebookProfile;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
    }

    public ArrayList<String> getPhotosList() {
        return mPhotosList;
    }

    public void setPhotosList(ArrayList<String> photosList) {
        this.mPhotosList = photosList;
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
