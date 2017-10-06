package ycagri.challenge.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import ycagri.challenge.R;
import ycagri.challenge.fragments.DetailFragment;
import ycagri.challenge.fragments.MasterFragment;
import ycagri.challenge.interfaces.OnFragmentInteractionListener;
import ycagri.challenge.data.Venue;

/**
 * This class is the only activity that holds fragments. It has different layout implementations
 * for landscape and portrait orientations.
 * <p>
 * Uses {@link GoogleApiClient} to access user's location as described in
 * <a href="https://developer.android.com/training/location/index.html">https://developer.android.com/training/location/index.html</a>
 *
 * @author ycagri
 * @since 05.04.2017
 */
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String KEY_SELECTED_VENUE = "selected_venue";

    private static final int REQUEST_CHECK_SETTINGS = 24;
    private static final int RC_LOCATION_LISTENER = 100;

    private Venue mSelectedVenue = null;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
            }

            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (f != null && f instanceof MasterFragment) {
                ((MasterFragment) f).updateLocation(location);
            }
        }
    };

    private final ResultCallback<LocationSettingsResult> mLocationSettingsCallback = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
            final Status status = locationSettingsResult.getStatus();

            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can
                    // initialize location requests here.
                    if (mGoogleApiClient.isConnected()) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
                        }
                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way
                    // to fix the settings so we won't show the dialog.
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (savedInstanceState == null) {
            int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (rc == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                requestLocationPermission();

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    MasterFragment.newInstance()).commit();
        } else {
            mSelectedVenue = savedInstanceState.getParcelable(KEY_SELECTED_VENUE);
            if (mSelectedVenue != null && findViewById(R.id.fragment_detail_container) == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, DetailFragment.newInstance(mSelectedVenue))
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        outState.putParcelable(KEY_SELECTED_VENUE, mSelectedVenue);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            mSelectedVenue = savedInstanceState.getParcelable(KEY_SELECTED_VENUE);
    }

    @Override
    public void addFragment(Venue venue) {
        mSelectedVenue = venue;
        if (findViewById(R.id.fragment_detail_container) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, DetailFragment.newInstance(venue))
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_detail_container, DetailFragment.newInstance(venue))
                    .commit();
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        setTitle(title);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
            getUserLocation();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getUserLocation() {
        if (mLocationRequest == null) {
            createLocationRequest();
        }
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(mLocationSettingsCallback);
    }

    /**
     * Handles the requesting of the location permission.
     */
    private void requestLocationPermission() {
        final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, permissions, RC_LOCATION_LISTENER);
        }
    }
}
