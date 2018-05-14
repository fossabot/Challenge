package ycagri.challenge.data.source.remote;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.VenuePhoto;

/**
 * Created by vayen01 on 10/11/2017.
 */

public interface RemoteDataSource {

    Single<List<Venue>> getVenues(double latitude, double longitude);

    Single<List<VenuePhoto>> getVenuePhotos(String venueId);
}
