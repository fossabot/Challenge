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
import io.reactivex.schedulers.Schedulers;
import ycagri.challenge.data.CategoryIcon;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.VenueCategory;
import ycagri.challenge.data.VenueLocation;
import ycagri.challenge.data.VenuePhoto;
import ycagri.challenge.data.VenueStat;

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
    private Function<Cursor, VenuePhoto> mPhotoMapperFunction;

    @NonNull
    private Function<Cursor, VenueLocation> mLocationMapperFunction;

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
        mPhotoMapperFunction = this::getPhoto;
        mLocationMapperFunction = this::getLocation;
        mCountMapperFunction = this::getCount;
    }

    @NonNull
    private Venue getVenue(@NonNull Cursor c) {
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
                null, stat, new VenueCategory[]{category});
    }

    @NonNull
    private VenuePhoto getPhoto(@NonNull Cursor c) {
        return new VenuePhoto(
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.VenuePhotoEntry.PHOTO_ID)),
                c.getLong(c.getColumnIndexOrThrow(VenuePersistentContract.VenuePhotoEntry.CREATED_AT)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.VenuePhotoEntry.PREFIX)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.VenuePhotoEntry.SUFFIX)),
                c.getInt(c.getColumnIndexOrThrow(VenuePersistentContract.VenuePhotoEntry.WIDTH)),
                c.getInt(c.getColumnIndexOrThrow(VenuePersistentContract.VenuePhotoEntry.HEIGHT)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.VenuePhotoEntry.VISIBILITY)));
    }

    @NonNull
    private VenueLocation getLocation(@NonNull Cursor c) {
        return new VenueLocation(
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.ADDRESS)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.CROSS_STREET)),
                c.getDouble(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.LATITUDE)),
                c.getDouble(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.LONGITUDE)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.COUNTRY)),
                c.getString(c.getColumnIndexOrThrow(VenuePersistentContract.LocationEntry.FORMATTED_ADDRESS)).split("\\|"));
    }

    @NonNull
    private Integer getCount(Cursor c) {
        return c.getInt(0);
    }

    @Override
    public Observable<List<Venue>> getVenues() {
        String tableName = VenuePersistentContract.VenueEntry.TABLE_NAME + " INNER JOIN " +
                VenuePersistentContract.CategoryEntry.TABLE_NAME + " ON " +
                VenuePersistentContract.VenueEntry.TABLE_NAME + "." + VenuePersistentContract.VenueEntry.VENUE_ID
                + "=" + VenuePersistentContract.CategoryEntry.TABLE_NAME + "." + VenuePersistentContract.CategoryEntry.VENUE_ID;

        String sql = String.format("SELECT * FROM %s", tableName);
        return mDatabaseHelper.createQuery(VenuePersistentContract.VenueEntry.TABLE_NAME, sql).mapToList(mVenueMapperFunction);
    }

    @Override
    public void insertVenues(List<Venue> venues) {
        Observable.using(mDatabaseHelper::newTransaction,
                transaction -> inTransactionVenueInsert(venues, transaction),
                BriteDatabase.Transaction::end)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public Observable<VenueLocation> getVenueLocation(String id) {
        String sql = String.format("SELECT * FROM %s WHERE %s", VenuePersistentContract.LocationEntry.TABLE_NAME,
                VenuePersistentContract.LocationEntry.VENUE_ID + "=?");
        return mDatabaseHelper.createQuery(VenuePersistentContract.LocationEntry.TABLE_NAME, sql, id)
                .mapToOne(mLocationMapperFunction);
    }

    @Override
    public Observable<List<VenuePhoto>> getVenuePhotos(String venueId) {
        String sql = String.format("SELECT * FROM %s WHERE %s",
                VenuePersistentContract.VenuePhotoEntry.TABLE_NAME,
                VenuePersistentContract.VenuePhotoEntry.VENUE_ID + "=?");

        return mDatabaseHelper.createQuery(VenuePersistentContract.VenuePhotoEntry.TABLE_NAME,
                sql, venueId).mapToList(mPhotoMapperFunction);
    }

    @Override
    public void insertVenuePhotos(List<VenuePhoto> venuePhotos, String venueId) {
        Observable.using(mDatabaseHelper::newTransaction,
                transaction -> inTransactionPhotoInsert(venuePhotos, venueId, transaction),
                BriteDatabase.Transaction::end)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public Observable<Integer> getPhotoCount(String venueId) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s",
                VenuePersistentContract.VenuePhotoEntry.TABLE_NAME,
                VenuePersistentContract.VenuePhotoEntry.VENUE_ID + "=?");

        return mDatabaseHelper.createQuery(VenuePersistentContract.VenuePhotoEntry.TABLE_NAME,
                sql, venueId).mapToOne(mCountMapperFunction);
    }

    @Override
    public void clearDatabase() {
        mDatabaseHelper.delete(VenuePersistentContract.VenueEntry.TABLE_NAME, null);
    }

    @NonNull
    private Observable<List<Venue>> inTransactionVenueInsert(@NonNull List<Venue> venues,
                                                             @NonNull BriteDatabase.Transaction transaction) {
        checkNotNull(venues);
        checkNotNull(transaction);

        return Observable.fromArray(venues.toArray(new Venue[venues.size()]))
                .doOnNext(v -> {
                    ContentValues values = toContentValues(v);
                    mDatabaseHelper.insert(VenuePersistentContract.VenueEntry.TABLE_NAME, values);
                    mDatabaseHelper.insert(VenuePersistentContract.LocationEntry.TABLE_NAME, toContentValues(v.getLocation(), v.getId()));
                    for (VenueCategory c : v.getCategories())
                        if (c.isPrimary())
                            mDatabaseHelper.insert(VenuePersistentContract.CategoryEntry.TABLE_NAME, toContentValues(c, v.getId()));
                })
                .doOnComplete(transaction::markSuccessful)
                .toList().toObservable();
    }

    @NonNull
    private Observable<List<VenuePhoto>> inTransactionPhotoInsert(@NonNull List<VenuePhoto> photos,
                                                                  @NonNull String venueId,
                                                                  @NonNull BriteDatabase.Transaction transaction) {
        checkNotNull(photos);
        checkNotNull(transaction);

        return Observable.fromArray(photos.toArray(new VenuePhoto[photos.size()]))
                .doOnNext(v -> {
                    ContentValues values = toContentValues(v, venueId);
                    mDatabaseHelper.insert(VenuePersistentContract.VenuePhotoEntry.TABLE_NAME, values);
                })
                .doOnComplete(transaction::markSuccessful)
                .toList().toObservable();
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

    private ContentValues toContentValues(VenueLocation location, String venueId) {
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

    private ContentValues toContentValues(VenueCategory category, String venueId) {
        ContentValues values = new ContentValues();
        values.put(VenuePersistentContract.CategoryEntry.VENUE_ID, venueId);
        values.put(VenuePersistentContract.CategoryEntry.CATEGORY_ID, category.getId());
        values.put(VenuePersistentContract.CategoryEntry.NAME, category.getName());
        values.put(VenuePersistentContract.CategoryEntry.ICON_PREFIX, category.getIcon().getPrefix());
        values.put(VenuePersistentContract.CategoryEntry.ICON_SUFFIX, category.getIcon().getSuffix());
        return values;
    }

    private ContentValues toContentValues(VenuePhoto photo, String mVenueId) {
        ContentValues values = new ContentValues();
        values.put(VenuePersistentContract.VenuePhotoEntry.PHOTO_ID, photo.getPhotoId());
        values.put(VenuePersistentContract.VenuePhotoEntry.CREATED_AT, photo.getCreatedAt());
        values.put(VenuePersistentContract.VenuePhotoEntry.PREFIX, photo.getPrefix());
        values.put(VenuePersistentContract.VenuePhotoEntry.SUFFIX, photo.getSuffix());
        values.put(VenuePersistentContract.VenuePhotoEntry.WIDTH, photo.getWidth());
        values.put(VenuePersistentContract.VenuePhotoEntry.HEIGHT, photo.getHeight());
        values.put(VenuePersistentContract.VenuePhotoEntry.VISIBILITY, photo.getVisibility());
        values.put(VenuePersistentContract.VenuePhotoEntry.VENUE_ID, mVenueId);

        return values;
    }
}
