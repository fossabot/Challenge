package ycagri.challenge.data.source;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import ycagri.challenge.data.Venue;

/**
 * Created by vayen01 on 24/10/2017.
 */

public interface LocalDataSource {

    Observable<List<Venue>> getVenues();

    Observable insertVenue(Venue venue);

    Observable<Venue> getVenueById(String id);

    Observable<Integer> getCount();

    void clearDatabase();
}
