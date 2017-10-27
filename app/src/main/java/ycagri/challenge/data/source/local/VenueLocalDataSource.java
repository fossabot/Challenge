package ycagri.challenge.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import ycagri.challenge.data.CategoryIcon;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.VenueCategory;
import ycagri.challenge.data.VenueLocation;
import ycagri.challenge.data.VenueStat;
import ycagri.challenge.data.source.LocalDataSource;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 06/10/2017.
 */
@Singleton
public class VenueLocalDataSource implements LocalDataSource {

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    @NonNull
    private Function<Cursor, Venue> mVenueMapperFunction;

    @NonNull
    private Function<Cursor, Integer> mCountMapperFunction;

    @Inject
    public VenueLocalDataSource(@NonNull Context context,
                                @NonNull Scheduler scheduler) {
        checkNotNull(context, "context cannot be null");
        checkNotNull(scheduler, "scheduleProvider cannot be null");
        VenueDBHelper dbHelper = new VenueDBHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, scheduler);
        mVenueMapperFunction = this::getVenue;
        mCountMapperFunction = this::getCount;
    }

    @NonNull
    private Venue getVenue(@NonNull Cursor c) {
        VenueLocation location = new VenueLocation(
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.ADDRESS)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.CROSS_STREET)),
                c.getDouble(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.LATITUDE)),
                c.getDouble(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.LONGITUDE)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.COUNTRY)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.FORMATTED_ADDRESS)).split("\\|"));

        VenueStat stat = new VenueStat(
                c.getInt(c.getColumnIndexOrThrow(VenuePersistentContract.VenueEntry.CHECK_IN_COUNT)),
                c.getInt(c.getColumnIndexOrThrow(VenuePersistentContract.VenueEntry.USER_COUNT)),
                c.getInt(c.getColumnIndexOrThrow(VenuePersistentContract.VenueEntry.TIP_COUNT)));

        VenueCategory category = new VenueCategory(
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.CategoryEntry.CATEGORY_ID)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.CategoryEntry.NAME)),
                new CategoryIcon(
                        c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.CategoryEntry.ICON_PREFIX)),
                        c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.CategoryEntry.ICON_SUFFIX))
                ),
                true
        );

        return new Venue(
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.VenueEntry.VENUE_ID)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.VenueEntry.NAME)),
                location, stat, new VenueCategory[]{category});
    }

    @NonNull
    private Integer getCount(@NonNull Cursor c) {
        return c.getCount();
    }

    @Override
    public Observable<List<Venue>> getVenues() {
        String tableName = VenuePersistentContract.VenueEntry.TABLE_NAME + "," +
                VenuePersistentContract.CategoryEntry.TABLE_NAME + "," +
                VenuePersistentContract.LocationEntry.TABLE_NAME;
        String sql = String.format("SELECT * FROM %s WHERE %s", tableName,
                VenuePersistentContract.VenueEntry.TABLE_NAME + "." + VenuePersistentContract.VenueEntry._ID + "=" + VenuePersistentContract.LocationEntry.VENUE_ID + " AND " +
                        VenuePersistentContract.VenueEntry.TABLE_NAME + "." + VenuePersistentContract.VenueEntry._ID + "=" + VenuePersistentContract.CategoryEntry.VENUE_ID);
        return mDatabaseHelper.createQuery(tableName, sql).mapToList(mVenueMapperFunction);
    }

    @Override
    public Observable insertVenue(Venue venue) {
        return Observable.using(mDatabaseHelper::newTransaction,
                transaction -> inTransactionInsert(venue, transaction),
                BriteDatabase.Transaction::end);
    }

    @Override
    public Observable<Venue> getVenueById(String id) {
        String tableName = VenuePersistentContract.VenueEntry.TABLE_NAME + "," +
                VenuePersistentContract.CategoryEntry.TABLE_NAME + "," +
                VenuePersistentContract.LocationEntry.TABLE_NAME;
        String sql = String.format("SELECT * FROM %s WHERE %s", tableName,
                VenuePersistentContract.VenueEntry.TABLE_NAME + "." + VenuePersistentContract.VenueEntry._ID + "=" + VenuePersistentContract.LocationEntry.VENUE_ID + " AND " +
                        VenuePersistentContract.VenueEntry.TABLE_NAME + "." + VenuePersistentContract.VenueEntry._ID + "=" + VenuePersistentContract.CategoryEntry.VENUE_ID + " AND " +
                        VenuePersistentContract.VenueEntry.VENUE_ID + "=?");
        return mDatabaseHelper.createQuery(tableName, sql, new String[]{id}).mapToOne(mVenueMapperFunction);
    }

    @Override
    public Observable<Integer> getCount() {
        return mDatabaseHelper.createQuery(VenuePersistentContract.VenueEntry.TABLE_NAME,
                String.format("SELECT COUNT(*) FROM %s", VenuePersistentContract.VenueEntry.TABLE_NAME))
                .mapToOne(mCountMapperFunction);
    }

    @Override
    public void clearDatabase() {
        mDatabaseHelper.delete(VenuePersistentContract.VenueEntry.TABLE_NAME, null);
    }

    @NonNull
    private Observable<Venue> inTransactionInsert(@NonNull Venue venue,
                                                  @NonNull BriteDatabase.Transaction transaction) {
        checkNotNull(venue);
        checkNotNull(transaction);

        return Observable.just(venue)
                .doOnNext(v -> {
                    ContentValues values = toContentValues(v);
                    long id = mDatabaseHelper.insert(VenuePersistentContract.VenueEntry.TABLE_NAME, values);
                    mDatabaseHelper.insert(VenuePersistentContract.LocationEntry.TABLE_NAME, toContentValues(venue.getLocation(), id));
                    for (VenueCategory c : venue.getCategories())
                        if (c.isPrimary())
                            mDatabaseHelper.insert(VenuePersistentContract.CategoryEntry.TABLE_NAME, toContentValues(c, id));
                })
                .doOnComplete(transaction::markSuccessful);
    }

    private ContentValues toContentValues(Venue venue) {
        ContentValues values = new ContentValues();
        values.put(VenuePersistentContract.VenueEntry.VENUE_ID, venue.getId());
        values.put(VenuePersistentContract.VenueEntry.NAME, venue.getName());
        values.put(VenuePersistentContract.VenueEntry.CHECK_IN_COUNT, venue.getVenueStat().getCheckInCount());
        values.put(VenuePersistentContract.VenueEntry.USER_COUNT, venue.getVenueStat().getUserCount());
        values.put(VenuePersistentContract.VenueEntry.TIP_COUNT, venue.getVenueStat().getTipCount());
        return values;
    }

    private ContentValues toContentValues(VenueLocation location, long venueId) {
        ContentValues values = new ContentValues();
        values.put(VenuePersistentContract.LocationEntry.VENUE_ID, venueId);
        values.put(VenuePersistentContract.LocationEntry.ADDRESS, location.getAddress());
        values.put(VenuePersistentContract.LocationEntry.CROSS_STREET, location.getCrossStreet());
        values.put(VenuePersistentContract.LocationEntry.LATITUDE, location.getLatitude());
        values.put(VenuePersistentContract.LocationEntry.LONGITUDE, location.getLongitude());
        values.put(VenuePersistentContract.LocationEntry.COUNTRY, location.getCountry());
        values.put(VenuePersistentContract.LocationEntry.FORMATTED_ADDRESS, StringUtils.join(location.getFormattedAddress(), "|"));
        return values;
    }

    private ContentValues toContentValues(VenueCategory category, long venueId) {
        ContentValues values = new ContentValues();
        values.put(VenuePersistentContract.CategoryEntry.VENUE_ID, venueId);
        values.put(VenuePersistentContract.CategoryEntry.CATEGORY_ID, category.getId());
        values.put(VenuePersistentContract.CategoryEntry.NAME, category.getName());
        values.put(VenuePersistentContract.CategoryEntry.ICON_PREFIX, category.getIcon().getPrefix());
        values.put(VenuePersistentContract.CategoryEntry.ICON_SUFFIX, category.getIcon().getSuffix());
        return values;
    }
}
