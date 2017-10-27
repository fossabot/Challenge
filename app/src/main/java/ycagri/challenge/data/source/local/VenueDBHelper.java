package ycagri.challenge.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vayen01 on 06/10/2017.
 */

public class VenueDBHelper extends SQLiteOpenHelper {

    public VenueDBHelper(Context context) {
        super(context, VenuePersistentContract.DATABASE_NAME, null, VenuePersistentContract.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(VenuePersistentContract.VenueEntry.CREATE_TABLE);
        sqLiteDatabase.execSQL(VenuePersistentContract.LocationEntry.CREATE_TABLE);
        sqLiteDatabase.execSQL(VenuePersistentContract.CategoryEntry.CREATE_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(VenuePersistentContract.CategoryEntry.DROP_TABLE);
        sqLiteDatabase.execSQL(VenuePersistentContract.LocationEntry.DROP_TABLE);
        sqLiteDatabase.execSQL(VenuePersistentContract.VenueEntry.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
