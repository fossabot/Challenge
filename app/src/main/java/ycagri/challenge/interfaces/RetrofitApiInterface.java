package ycagri.challenge.interfaces;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.VenuePhoto;

/**
 * Created by vayen01 on 13/10/2017.
 */

public interface RetrofitApiInterface {

    @GET("venues/search")
    Single<List<Venue>> getVenues(@Query("ll") String location,
                                  @Query("client_id") String clientId,
                                  @Query("client_secret") String clientSecret,
                                  @Query("v") String date);

    @GET("venues/{id}/photos")
    Single<List<VenuePhoto>> getVenuePhotos(@Path("id") String venueId,
                                            @Query("client_id") String clientId,
                                            @Query("client_secret") String clientSecret,
                                            @Query("v") String date);

}
