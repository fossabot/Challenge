package ycagri.challenge.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import ycagri.challenge.R;
import ycagri.challenge.util.ActivityUtils;

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
public class MainActivity extends DaggerAppCompatActivity implements VenueSelectionNavigator {

    private static final int REQUEST_CHECK_SETTINGS = 24;
    private static final int RC_LOCATION_LISTENER = 100;

    @Inject
    MasterFragment mMasterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        MasterFragment fragment = (MasterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mMasterFragment, R.id.fragment_container);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
            //mViewModel.getUserLocation(this);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onVenueSelected(String venueId) {
        if (findViewById(R.id.fragment_detail_container) != null) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), DetailFragment.newInstance(venueId), R.id.fragment_detail_container);
        } else {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), DetailFragment.newInstance(venueId), R.id.fragment_container);
        }
    }
}
