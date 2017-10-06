package ycagri.challenge.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vayen01 on 06/10/2017.
 */

public class VenueDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Venues.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String DOUBLE_TYPE = " DOUBLE";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + VenuePersistentContract.VenueEntry.TABLE_NAME + " (" +
                    VenuePersistentContract.VenueEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    VenuePersistentContract.VenueEntry.VENUE_ID + TEXT_TYPE + COMMA_SEP +
                    VenuePersistentContract.VenueEntry.NAME + TEXT_TYPE + COMMA_SEP +
                    VenuePersistentContract.VenueEntry.URL + TEXT_TYPE + COMMA_SEP +
                    VenuePersistentContract.VenueEntry.PHONE + TEXT_TYPE + COMMA_SEP +
                    VenuePersistentContract.VenueEntry.FACEBOOK_PROFILE + TEXT_TYPE + COMMA_SEP +
                    VenuePersistentContract.VenueEntry.LATITUDE + DOUBLE_TYPE + COMMA_SEP +
                    VenuePersistentContract.VenueEntry.LONGITUDE + DOUBLE_TYPE + COMMA_SEP +
                    VenuePersistentContract.VenueEntry.ICON + TEXT_TYPE + COMMA_SEP +
                    VenuePersistentContract.VenueEntry.PHOTOS + TEXT_TYPE + COMMA_SEP +
                    " )";


    public VenueDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
