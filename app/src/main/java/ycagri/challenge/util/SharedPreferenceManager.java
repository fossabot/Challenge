package ycagri.challenge.util;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.annotations.NonNull;

/**
 * Created by vayen01 on 01/11/2017.
 */
@Singleton
public class SharedPreferenceManager {

    private static final String SHARED_PREFERENCES_NAME = "challenge_shared_preferences";

    private static final String KEY_LATEST_UPDATE_TIME = "latest_update_time";
    private static final String KEY_LATEST_LATITUDE = "latest_latitude";
    private static final String KEY_LATEST_LONGITUDE = "latest_longitude";

    private final SharedPreferences mSharedPreferences;

    @Inject
    public SharedPreferenceManager(@NonNull Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public long getLatestUpdateTime() {
        return mSharedPreferences.getLong(KEY_LATEST_UPDATE_TIME, 0);
    }

    public void putLatestUpdateTime(long time) {
        mSharedPreferences.edit().putLong(KEY_LATEST_UPDATE_TIME, time).apply();
    }

    public double getLatestLatitude() {
        return mSharedPreferences.getFloat(KEY_LATEST_LATITUDE, 0);
    }

    public void putLatestLatitude(double latitude) {
        mSharedPreferences.edit().putFloat(KEY_LATEST_LATITUDE, (float) latitude).apply();
    }

    public double getLatestLongitude() {
        return mSharedPreferences.getFloat(KEY_LATEST_LONGITUDE, 0);
    }

    public void putLatestLongitude(double longitude) {
        mSharedPreferences.edit().putFloat(KEY_LATEST_LONGITUDE, (float) longitude).apply();
    }
}
