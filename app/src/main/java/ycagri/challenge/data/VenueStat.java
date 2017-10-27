package ycagri.challenge.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vayen01 on 17/10/2017.
 */

public class VenueStat {

    @SerializedName("checkinsCount")
    private final int mCheckInCount;

    @SerializedName("usersCount")
    private final int mUserCount;

    @SerializedName("tipCount")
    private final int mTipCount;

    public VenueStat(int checkInCount, int userCount, int tipCount) {
        this.mCheckInCount = checkInCount;
        this.mUserCount = userCount;
        this.mTipCount = tipCount;
    }

    public int getCheckInCount() {
        return mCheckInCount;
    }

    public int getUserCount() {
        return mUserCount;
    }

    public int getTipCount() {
        return mTipCount;
    }
}
