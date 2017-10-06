package ycagri.challenge.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.source.VenueDataSource;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 06/10/2017.
 */
@Singleton
public class VenueLocalDataSource implements VenueDataSource {

    private VenueDBHelper mDbHelper;

    @Inject
    public VenueLocalDataSource(@NonNull Context context) {
        mDbHelper = new VenueDBHelper(checkNotNull(context));
    }

    @NonNull
    @Override
    public Observable<List<Venue>> getVenues() {
        return null;
    }
}
