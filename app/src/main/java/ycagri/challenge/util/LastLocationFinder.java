package ycagri.challenge.util;

/**
 * Created by vayen01 on 04/04/2017.
 */

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Optimized implementation of Last Location Finder for devices running Gingerbread
 * and above.
 * <p/>
 * This class let's you find the "best" (most accurate and timely) previously
 * detected location using whatever providers are available.
 * <p/>
 * Where a timely / accurate previous location is not detected it will
 * return the newest location (where one exists) and setup a oneshot
 * location update to find the current location.
 *
 * @since 24.05.2916
 */
public class LastLocationFinder {

    protected static String TAG = "LastLocationFinder";
    private static String SINGLE_LOCATION_UPDATE_ACTION = "SINGLE_LOCATION_UPDATE_ACTION";

    private PendingIntent singleUpdatePI;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Context context;
    private Criteria criteria;

    /**
     * Construct a new Gingerbread Last Location Finder.
     *
     * @param context Context
     */
    public LastLocationFinder(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Coarse accuracy is specified here to get the fastest possible result.
        // The calling Activity will likely (or have already) request ongoing
        // updates using the Fine location provider.
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);

        // Construct the Pending Intent that will be broadcast by the oneshot
        // location update.
        Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);
        singleUpdatePI = PendingIntent.getBroadcast(context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Returns the most accurate and timely previously detected location.
     * Where the last result is beyond the specified maximum distance or
     * latency a one-off location update is returned via the {@link LocationListener}
     * specified in {@link LastLocationFinder#setChangedLocationListener}.
     *
     * @param minDistance Minimum distance before we require a location update.
     * @param minTime     Minimum time required between location updates.
     * @return The most accurate and / or timely previously detected location.
     */
    public Location getLastBestLocation(int minDistance, long minTime, boolean getOneShot) {
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Iterate through all the providers on the system, keeping
            // note of the most accurate result within the acceptable time limit.
            // If no result is found within maxTime, return the newest Location.
            List<String> matchingProviders = locationManager.getAllProviders();
            for (String provider : matchingProviders) {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    float accuracy = location.getAccuracy();
                    long time = location.getTime();

                    if (time > minTime && accuracy < bestAccuracy) {
                        bestResult = location;
                        bestAccuracy = accuracy;
                        bestTime = time;
                    } else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
                        bestResult = location;
                        bestTime = time;
                    }
                }
            }

            // If the best result is beyond the allowed time limit, or the accuracy of the
            // best result is wider than the acceptable maximum distance, request a single update.
            // This check simply implements the same conditions we set when requesting regular
            // location updates every [minTime] and [minDistance].
            if (locationListener != null && (bestTime < minTime || bestAccuracy > minDistance) && getOneShot) {
                if (locationManager.getBestProvider(criteria, true).equals(LocationManager.PASSIVE_PROVIDER)) {
                    locationListener.onProviderDisabled(LocationManager.PASSIVE_PROVIDER);
                    locationManager.removeUpdates(locationListener);
                } else {
                    IntentFilter locIntentFilter = new IntentFilter(SINGLE_LOCATION_UPDATE_ACTION);
                    context.registerReceiver(singleUpdateReceiver, locIntentFilter);
                    locationManager.requestSingleUpdate(criteria, singleUpdatePI);
                }
            }
        }

        return bestResult;
    }

    /**
     * This {@link BroadcastReceiver} listens for a single location
     * update before unregistering itself.
     * The oneshot location update is returned via the {@link LocationListener}
     * specified in {@link LastLocationFinder#setChangedLocationListener}.
     */
    private BroadcastReceiver singleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                context.unregisterReceiver(singleUpdateReceiver);
            } catch (IllegalArgumentException e) {
                // If receiver either is not registered yet nor already unregistered, there is nothing to do.
            }

            String key = LocationManager.KEY_LOCATION_CHANGED;
            Location location = (Location) intent.getExtras().get(key);

            if (locationListener != null && location != null)
                locationListener.onLocationChanged(location);

            locationManager.removeUpdates(singleUpdatePI);
        }
    };

    /**
     * {@inheritDoc}
     */
    public void setChangedLocationListener(LocationListener l) {
        locationListener = l;
    }

    /**
     * {@inheritDoc}
     */
    public void cancel() {
        locationManager.removeUpdates(singleUpdatePI);
        try {
            context.unregisterReceiver(singleUpdateReceiver);
        } catch (IllegalArgumentException e) {
            // If receiver either is not registered yet nor already unregistered, there is nothing to do.
        }
    }
}