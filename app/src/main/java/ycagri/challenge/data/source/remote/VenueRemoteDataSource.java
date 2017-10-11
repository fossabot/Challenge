package ycagri.challenge.data.source.remote;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;
import rx.Observable;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.source.VenueDataSource;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 10/10/2017.
 */
@Singleton
public class VenueRemoteDataSource implements VenueDataSource {

    @NonNull
    private final Retrofit mRetrofit;

    @Inject
    public VenueRemoteDataSource(@NonNull Retrofit retrofit) {
        this.mRetrofit = checkNotNull(retrofit);
    }

    @NonNull
    @Override
    public Observable<List<Venue>> getVenues() {
        return null;
    }
}
