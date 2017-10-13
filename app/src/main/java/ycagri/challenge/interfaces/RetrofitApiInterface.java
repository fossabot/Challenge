package ycagri.challenge.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ycagri.challenge.data.Venue;

/**
 * Created by vayen01 on 13/10/2017.
 */

public interface RetrofitApiInterface {

    @GET("venues/search?ll={location}&client_id={clientId}&client_secret={clientSecret}&v={date}")
    Call<Venue> getVenues(@Path("location") String location,
                          @Path("clientId") String clientId,
                          @Path("clientSecret") String clientSecret,
                          @Path("data") String date);
}
