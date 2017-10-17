package ycagri.challenge.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import rx.Scheduler;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.source.VenueDataSource;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 06/10/2017.
 */
@Singleton
public class VenueLocalDataSource implements VenueDataSource {

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    @NonNull
    private Func1<Cursor, Venue> mTaskMapperFunction;

    @Inject
    public VenueLocalDataSource(@NonNull Context context,
                                @NonNull Scheduler scheduler) {
        checkNotNull(context, "context cannot be null");
        checkNotNull(scheduler, "scheduleProvider cannot be null");
        VenueDBHelper dbHelper = new VenueDBHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, scheduler);
        mTaskMapperFunction = this::getVenue;
    }

    @NonNull
    private Venue getVenue(@NonNull Cursor c) {
        return null;
    }


    @NonNull
    @Override
    public Observable<List<Venue>> getVenues(String location, String clientId, String clientSecret, String date) {
        return null;
    }
}
