package ycagri.challenge.data;

import com.google.gson.annotations.SerializedName;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Simple Plain Old Java Object keeps information about venue.
 *
 * @author ycagri
 * @since 10.1.2015.
 */
public class Venue {

    @SerializedName("id")
    private final String mId;

    @SerializedName("name")
    private final String mName;

    @SerializedName("location")
    private final VenueLocation mLocation;

    @SerializedName("stats")
    private final VenueStat mVenueStat;

    @SerializedName("categories")
    private final VenueCategory[] mCategories;

    public Venue(String id, String name, VenueLocation location, VenueStat stat, VenueCategory[] categories) {
        mId = id;
        mName = name;
        mLocation = checkNotNull(location);
        mVenueStat = checkNotNull(stat);
        mCategories = checkNotNull(categories);
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public VenueLocation getLocation() {
        return mLocation;
    }

    public VenueStat getVenueStat() {
        return mVenueStat;
    }

    public VenueCategory[] getCategories() {
        return mCategories;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Venue && ((Venue) obj).getId().equals(mId);
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    @Override
    public String toString() {
        return mName;
    }
}
