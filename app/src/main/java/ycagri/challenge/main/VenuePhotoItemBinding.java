package ycagri.challenge.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ycagri.challenge.data.VenuePhoto;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 31/10/2017.
 */

public class VenuePhotoItemBinding extends BaseObservable {

    private final VenuePhoto mVenuePhoto;

    public VenuePhotoItemBinding(VenuePhoto venuePhoto) {
        this.mVenuePhoto = checkNotNull(venuePhoto);
    }

    @Bindable
    public String getUrl() {
        return mVenuePhoto.getPrefix() + mVenuePhoto.getWidth() + "x" + mVenuePhoto.getHeight() + mVenuePhoto.getSuffix();
    }
}
