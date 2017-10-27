package ycagri.challenge.interfaces;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ycagri.challenge.data.Venue;

/**
 * Created by vayen01 on 13/10/2017.
 */

public interface RetrofitApiInterface {

    @GET("venues/search")
    Observable<List<Venue>> getVenues(@Query("ll") String location,
                                      @Query("client_id") String clientId,
                                      @Query("client_secret") String clientSecret,
                                      @Query("v") String date);
}
