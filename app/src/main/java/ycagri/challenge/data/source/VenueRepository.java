package ycagri.challenge.data.source;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import ycagri.challenge.data.Venue;

/**
 * Created by vayen01 on 06/10/2017.
 */

public class VenueRepository implements VenueDataSource {

    private final VenueDataSource mVenueRemoteDataSource;

    private final LocalDataSource mVenueLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Venue> mCachedVenues;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    /**
     * By marking the constructor with {@code @Inject}, Dagger will try to inject the dependencies
     * required to create an instance of the TasksRepository. Because {@link VenueDataSource} is an
     * interface, we must provide to Dagger a way to build those arguments, this is done in
     * {@link VenueRepositoryModule}.
     * <p>
     * When two arguments or more have the same type, we must provide to Dagger a way to
     * differentiate them. This is done using a qualifier.
     * <p>
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    VenueRepository(@Remote VenueDataSource venueRemoteDataSource,
                    @Local LocalDataSource venueLocalDataSource) {
        mVenueRemoteDataSource = venueRemoteDataSource;
        mVenueLocalDataSource = venueLocalDataSource;
    }

    /**
     * Gets tasks from  local data source (SQLite).
     */
    @Override
    public Observable<List<Venue>> getVenues(String location, String clientId, String clientSecret, String date) {
        return mVenueLocalDataSource.getVenues();
    }
}
