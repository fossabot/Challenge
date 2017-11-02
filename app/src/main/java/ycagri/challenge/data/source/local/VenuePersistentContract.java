package ycagri.challenge.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by vayen01 on 06/10/2017.
 */

public final class VenuePersistentContract {

    public static final int DATABASE_VERSION = 3;

    public static final String DATABASE_NAME = "Venues.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String DOUBLE_TYPE = " DOUBLE";

    private static final String LONG_TYPE = " LONG";

    private static final String COMMA_SEP = ",";

    private VenuePersistentContract() {

    }

    public static abstract class VenueEntry implements BaseColumns {

        public static final String TABLE_NAME = "venues";

        public static final String VENUE_ID = "venue_id";

        public static final String NAME = "name";

        public static final String CHECK_IN_COUNT = "check_in_count";

        public static final String USER_COUNT = "user_count";

        public static final String TIP_COUNT = "tip_count";

        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                        VENUE_ID + TEXT_TYPE + COMMA_SEP +
                        NAME + TEXT_TYPE + COMMA_SEP +
                        CHECK_IN_COUNT + INTEGER_TYPE + COMMA_SEP +
                        USER_COUNT + INTEGER_TYPE + COMMA_SEP +
                        TIP_COUNT + INTEGER_TYPE + " );";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class LocationEntry implements BaseColumns {

        public static final String TABLE_NAME = "locations";

        public static final String VENUE_ID = "location_venue_id";

        public static final String ADDRESS = "address";

        public static final String CROSS_STREET = "cross_street";

        public static final String LATITUDE = "latitude";

        public static final String LONGITUDE = "longitude";

        public static final String COUNTRY = "country";

        public static final String FORMATTED_ADDRESS = "formatted_address";

        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                        VENUE_ID + INTEGER_TYPE + COMMA_SEP +
                        ADDRESS + TEXT_TYPE + COMMA_SEP +
                        CROSS_STREET + TEXT_TYPE + COMMA_SEP +
                        LATITUDE + DOUBLE_TYPE + COMMA_SEP +
                        LONGITUDE + DOUBLE_TYPE + COMMA_SEP +
                        COUNTRY + TEXT_TYPE + COMMA_SEP +
                        FORMATTED_ADDRESS + TEXT_TYPE + COMMA_SEP +
                        "FOREIGN KEY (" + VENUE_ID + ") REFERENCES " + VenueEntry.TABLE_NAME + "(" + VenueEntry._ID + ") ON DELETE CASCADE);";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class CategoryEntry implements BaseColumns {

        public static final String TABLE_NAME = "categories";

        public static final String VENUE_ID = "category_venue_id";

        public static final String CATEGORY_ID = "category_id";

        public static final String NAME = "category_name";

        public static final String ICON_PREFIX = "icon_prefix";

        public static final String ICON_SUFFIX = "icon_suffix";

        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                        VENUE_ID + INTEGER_TYPE + COMMA_SEP +
                        CATEGORY_ID + TEXT_TYPE + COMMA_SEP +
                        NAME + TEXT_TYPE + COMMA_SEP +
                        ICON_PREFIX + TEXT_TYPE + COMMA_SEP +
                        ICON_SUFFIX + TEXT_TYPE + COMMA_SEP +
                        "FOREIGN KEY (" + VENUE_ID + ") REFERENCES " + VenueEntry.TABLE_NAME + "(" + VenueEntry._ID + ") ON DELETE CASCADE);";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class VenuePhotoEntry implements BaseColumns {

        public static final String TABLE_NAME = "photos";

        public static final String PHOTO_ID = "photo_id";

        public static final String CREATED_AT = "created_at";

        public static final String PREFIX = "prefix";

        public static final String SUFFIX = "suffix";

        public static final String WIDTH = "width";

        public static final String HEIGHT = "height";

        public static final String VISIBILITY = "visibility";

        public static final String VENUE_ID = "venue_id";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                PHOTO_ID + TEXT_TYPE + COMMA_SEP +
                CREATED_AT + LONG_TYPE + COMMA_SEP +
                PREFIX + TEXT_TYPE + COMMA_SEP +
                SUFFIX + TEXT_TYPE + COMMA_SEP +
                WIDTH + INTEGER_TYPE + COMMA_SEP +
                HEIGHT + INTEGER_TYPE + COMMA_SEP +
                VISIBILITY + TEXT_TYPE + COMMA_SEP +
                VENUE_ID + INTEGER_TYPE + COMMA_SEP +
                "FOREIGN KEY (" + VENUE_ID + ") REFERENCES " + VenueEntry.TABLE_NAME + "(" + VenueEntry._ID + ") ON DELETE CASCADE);";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
