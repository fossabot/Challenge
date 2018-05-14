package ycagri.challenge.data.source.local;

import java.util.List;

import io.reactivex.Observable;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.VenueLocation;
import ycagri.challenge.data.VenuePhoto;

/**
 * Created by vayen01 on 24/10/2017.
 */

public interface LocalDataSource {

    Observable<List<Venue>> getVenues();

    void insertVenues(List<Venue> venue);

    Observable<VenueLocation> getVenueLocation(String id);

    Observable<List<VenuePhoto>> getVenuePhotos(String venueId);

    void insertVenuePhotos(List<VenuePhoto> venuePhotos, String venueId);

    Observable<Integer> getPhotoCount(String venueId);

    void clearDatabase();
}
