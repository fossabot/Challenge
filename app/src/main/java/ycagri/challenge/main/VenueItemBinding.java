package ycagri.challenge.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ycagri.challenge.data.CategoryIcon;
import ycagri.challenge.data.Venue;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 30/10/2017.
 */

public class VenueItemBinding extends BaseObservable {

    private final Venue mVenue;

    private final VenueSelectionNavigator mNavigator;

    public VenueItemBinding(Venue venue, VenueSelectionNavigator navigator) {
        mVenue = checkNotNull(venue);
        mNavigator = checkNotNull(navigator);
    }

    @Bindable
    public String getTitle() {
        return mVenue.getName();
    }

    @Bindable
    public String getUrl() {
        String iconUrl = "";
        if (mVenue.getCategories() != null && mVenue.getCategories().length > 0) {
            CategoryIcon icon = mVenue.getCategories()[0].getIcon();
            iconUrl = icon.getPrefix() + "88" + icon.getSuffix();
        }

        return iconUrl;
    }

    public void onVenueClick() {
        mNavigator.onVenueSelected(mVenue.getId());
    }
}
