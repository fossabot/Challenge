package ycagri.challenge.interfaces;

import android.location.Location;
import android.support.v4.app.Fragment;

/**
 * Created by YigitCagri on 11.1.2015.
 */
public interface OnFragmentInteractionListener {
    void addFragment(Fragment fragment);
    Location getLastKnownLocation();
    void setToolbarTitle(String title);
}
