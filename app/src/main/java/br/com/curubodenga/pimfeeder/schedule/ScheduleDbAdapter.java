package br.com.curubodenga.pimfeeder.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by curup on 26/07/2016.
 */
public class ScheduleDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_MONDAY = "monday";
    public static final String KEY_TUESDAY = "tuesday";
    public static final String KEY_WEDNESDAY = "wednesday";
    public static final String KEY_THURSDAY = "thursday";
    public static final String KEY_FRIDAY = "friday";
    public static final String KEY_SATURDAY = "saturday";
    public static final String KEY_SUNDAY = "sunday";

    private static final String TAG = "ScheduleDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "PimFeeder";
    private static final String SQLITE_TABLE = "Schedule";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_DATE + " datetime," +
                    KEY_MONDAY + " integer," +
                    KEY_TUESDAY + " integer," +
                    KEY_WEDNESDAY + " integer," +
                    KEY_THURSDAY + " integer," +
                    KEY_FRIDAY + " integer," +
                    KEY_SATURDAY + " integer," +
                    KEY_SUNDAY + " integer);";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public ScheduleDbAdapter(Context context) {
        this.context = context;
    }

    public ScheduleDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createSchedule(Date date, int sunday, int monday, int tuesday, int wednesday, int
            thursday, int friday, int saturday) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, dateFormat.format(date));
        initialValues.put(KEY_MONDAY, monday);
        initialValues.put(KEY_TUESDAY, tuesday);
        initialValues.put(KEY_WEDNESDAY, wednesday);
        initialValues.put(KEY_THURSDAY, thursday);
        initialValues.put(KEY_FRIDAY, friday);
        initialValues.put(KEY_SATURDAY, saturday);
        initialValues.put(KEY_SUNDAY, sunday);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllSchedules() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchAllSchedules() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID, KEY_DATE, KEY_MONDAY,
                        KEY_TUESDAY, KEY_WEDNESDAY, KEY_THURSDAY, KEY_FRIDAY, KEY_SATURDAY,
                        KEY_SUNDAY},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeSchedules() {

        Date date = new Date();

        date.setHours(13);
        createSchedule(date, 1, 1, 1, 1, 1, 1, 1);
        date.setHours(14);
        createSchedule(date, 0, 1, 1, 0, 1, 1, 1);
        date.setHours(15);
        createSchedule(date, 1, 0, 1, 0, 1, 1, 0);

    }


}