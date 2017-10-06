package ycagri.challenge.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by vayen01 on 06/10/2017.
 */

public final class VenuePersistentContract {

    private VenuePersistentContract() {

    }

    public static abstract class VenueEntry implements BaseColumns {

        public static final String TABLE_NAME = "venues";

        public static final String VENUE_ID = "venue_id";

        public static final String NAME = "name";

        public static final String URL = "url";

        public static final String PHONE = "phone";

        public static final String FACEBOOK_PROFILE = "facebook";

        public static final String LATITUDE = "lat";

        public static final String LONGITUDE = "lon";

        public static final String ICON = "icon";

        public static final String PHOTOS = "photos";
    }
}
