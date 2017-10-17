package ycagri.challenge.data.source;

import java.util.List;

import io.reactivex.Observable;
import ycagri.challenge.data.Venue;

/**
 * Created by vayen01 on 06/10/2017.
 */

public interface VenueDataSource {

    Observable<List<Venue>> getVenues(String location, String clientId, String clientSecret, String date);
}
