package ycagri.challenge.main;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import ycagri.challenge.R;
import ycagri.challenge.util.ActivityUtils;
import ycagri.challenge.util.EspressoIdlingResource;

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
    public void onVenueSelected(String venueId) {
        if (findViewById(R.id.fragment_detail_container) != null) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), DetailFragment.newInstance(venueId), R.id.fragment_detail_container);
        } else {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), DetailFragment.newInstance(venueId), R.id.fragment_container);
        }
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
