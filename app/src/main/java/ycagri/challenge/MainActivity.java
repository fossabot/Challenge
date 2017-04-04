package ycagri.challenge;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ycagri.challenge.fragments.MasterFragment;
import ycagri.challenge.interfaces.OnFragmentInteractionListener;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                MasterFragment.newInstance()).commit();
    }

    @Override
    public void addFragment(Fragment fragment) {
        if (findViewById(R.id.fragment_detail_container) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        } else {
            if (isNetworkEnabled) {
                if (locationManager != null) {
                    return locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (locationManager != null) {
                    return locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
        return null;
    }

    @Override
    public void setToolbarTitle(String title) {
        setTitle(title);
    }
}
