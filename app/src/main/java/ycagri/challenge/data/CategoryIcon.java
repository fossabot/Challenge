package ycagri.challenge.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vayen01 on 17/10/2017.
 */

public class CategoryIcon {

    @SerializedName("prefix")
    private final String mPrefix;

    @SerializedName("suffix")
    private final String mSuffix;

    public CategoryIcon(String prefix, String suffix) {
        this.mPrefix = prefix;
        this.mSuffix = suffix;
    }

    public String getPrefix() {
        return mPrefix;
    }

    public String getSuffix() {
        return mSuffix;
    }
}
