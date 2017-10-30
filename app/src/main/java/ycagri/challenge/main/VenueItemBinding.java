package ycagri.challenge.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ycagri.challenge.data.Venue;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 30/10/2017.
 */

public class VenueItemBinding extends BaseObservable {

    private final Venue mVenue;

    public VenueItemBinding(Venue venue) {
        this.mVenue = checkNotNull(venue);
    }

    @Bindable
    public String getTitle() {
        return mVenue.getName();
    }
}
