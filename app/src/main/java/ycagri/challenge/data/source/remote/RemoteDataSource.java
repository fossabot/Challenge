package ycagri.challenge.data.source.remote;

import java.util.List;

import io.reactivex.Observable;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.VenuePhoto;

/**
 * Created by vayen01 on 10/11/2017.
 */

public interface RemoteDataSource {

    Observable<List<Venue>> getVenues(double latitude, double longitude);

    Observable<List<VenuePhoto>> getVenuePhotos(String venueId);
}
