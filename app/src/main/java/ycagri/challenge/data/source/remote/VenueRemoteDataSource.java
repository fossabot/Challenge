package ycagri.challenge.data.source.remote;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.source.VenueDataSource;
import ycagri.challenge.interfaces.RetrofitApiInterface;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 10/10/2017.
 */
@Singleton
public class VenueRemoteDataSource implements VenueDataSource {

    @NonNull
    private final RetrofitApiInterface mRetrofit;

    @Inject
    public VenueRemoteDataSource(@NonNull RetrofitApiInterface retrofit) {
        this.mRetrofit = checkNotNull(retrofit);
    }

    @NonNull
    @Override
    public Observable<List<Venue>> getVenues(String location, String clientId, String clientSecret, String date) {
        Call<Venue> call = mRetrofit.getVenues(location, clientId, clientSecret, date);
        try {
            Response<Venue> venue = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
