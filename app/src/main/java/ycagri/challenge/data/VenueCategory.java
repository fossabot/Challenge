package ycagri.challenge.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vayen01 on 17/10/2017.
 */

public class VenueCategory {

    @SerializedName("id")
    private final String mId;

    @SerializedName("name")
    private final String mName;

    @SerializedName("icon")
    private final CategoryIcon mIcon;

    @SerializedName("primary")
    private final boolean mPrimary;

    public VenueCategory(String id, String name, CategoryIcon icon, boolean primary) {
        this.mId = id;
        this.mName = name;
        this.mIcon = icon;
        this.mPrimary = primary;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public CategoryIcon getIcon() {
        return mIcon;
    }

    public boolean isPrimary() {
        return mPrimary;
    }
}
