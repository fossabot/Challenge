package ycagri.challenge.interfaces;

import ycagri.challenge.data.Venue;

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
