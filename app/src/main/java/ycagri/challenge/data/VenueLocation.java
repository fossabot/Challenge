package ycagri.challenge.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vayen01 on 17/10/2017.
 */

public class VenueLocation {

    @SerializedName("address")
    private final String mAddress;

    @SerializedName("crossStreet")
    private final String mCrossStreet;

    @SerializedName("lat")
    private final double mLatitude;

    @SerializedName("lng")
    private final double mLongitude;

    @SerializedName("country")
    private final String mCountry;

    @SerializedName("formattedAddress")
    private final String[] mFormattedAddress;

    public VenueLocation(String address, String crossStreet, double latitude, double longitude, String country, String[] formattedAddress) {
        this.mAddress = address;
        this.mCrossStreet = crossStreet;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mCountry = country;
        this.mFormattedAddress = formattedAddress;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getCrossStreet() {
        return mCrossStreet;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getCountry() {
        return mCountry;
    }

    public String[] getFormattedAddress() {
        return mFormattedAddress;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VenueLocation && ((VenueLocation) obj).getLatitude() == mLatitude &&
                ((VenueLocation) obj).getLongitude() == mLongitude;
    }
}
