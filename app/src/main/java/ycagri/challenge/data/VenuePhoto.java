package ycagri.challenge.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vayen01 on 31/10/2017.
 */

public class VenuePhoto {

    @SerializedName("id")
    private final String mPhotoId;

    @SerializedName("createdAt")
    private final long mCreatedAt;

    @SerializedName("prefix")
    private final String mPrefix;

    @SerializedName("suffix")
    private final String mSuffix;

    @SerializedName("width")
    private final int mWidth;

    @SerializedName("height")
    private final int mHeight;

    @SerializedName("visibility")
    private final String mVisibility;

    public VenuePhoto(String photoId, long createdAt, String prefix, String suffix, int width, int height, String visibility) {
        this.mPhotoId = photoId;
        this.mCreatedAt = createdAt;
        this.mPrefix = prefix;
        this.mSuffix = suffix;
        this.mWidth = width;
        this.mHeight = height;
        this.mVisibility = visibility;
    }

    public String getPhotoId() {
        return mPhotoId;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public String getPrefix() {
        return mPrefix;
    }

    public String getSuffix() {
        return mSuffix;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public String getVisibility() {
        return mVisibility;
    }
}
