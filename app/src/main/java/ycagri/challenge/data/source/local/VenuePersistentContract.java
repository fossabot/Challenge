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
    }
}
