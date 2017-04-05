package ycagri.challenge.interfaces;

import android.location.Location;
import android.support.v4.app.Fragment;

import ycagri.challenge.pojo.Venue;

/**
 * Interface provides communication between fragments and activity.
 *
 * @author ycagri
 * @since 11.1.2015.
 */
public interface OnFragmentInteractionListener {
    void addFragment(Venue venue);

    void setToolbarTitle(String title);
}
