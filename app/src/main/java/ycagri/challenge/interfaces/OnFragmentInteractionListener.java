package ycagri.challenge.interfaces;

import android.location.Location;
import android.support.v4.app.Fragment;

/**
 * Created by YigitCagri on 11.1.2015.
 */
public interface OnFragmentInteractionListener {
    public void addFragment(Fragment fragment);
    public Location getLastKnownLocation();
    public void setTitle(String title);
}
