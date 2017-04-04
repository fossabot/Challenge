package ycagri.challenge.interfaces;

import android.location.Location;
import android.support.v4.app.Fragment;

import ycagri.challenge.pojo.Venue;

/**
 * Created by YigitCagri on 11.1.2015.
 */
public interface OnFragmentInteractionListener {
    void addFragment(Venue venue);

    void setToolbarTitle(String title);
}
