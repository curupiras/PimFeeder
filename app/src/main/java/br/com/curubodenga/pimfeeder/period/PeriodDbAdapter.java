package br.com.curubodenga.pimfeeder.period;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.curubodenga.pimfeeder.database.DatabaseHelper;
import br.com.curubodenga.pimfeeder.schedule.Schedule;


/**
 * Created by curup on 26/07/2016.
 */
public class PeriodDbAdapter extends DatabaseHelper {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_ICON = "icon";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_SECONDS = "seconds";

    private static final String TAG = "PeriodDbAdapter";
    private SQLiteDatabase mDb;

    public static final String SQLITE_TABLE = "Period";

    private final Context context;

    public static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_ICON + " text," +
                    KEY_ALIAS + " text," +
                    KEY_SECONDS + " integer);";

    public PeriodDbAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public PeriodDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createPeriod(Period period) {
        String id = period.getId();
        String icon = period.getIcon();
        String alias = period.getAlias();
        int seconds = period.getSeconds();

        if (period.getId() == null) {
            return createPeriod(icon, alias, seconds);
        } else {
            return updatePeriod(id, icon, alias, seconds);
        }
    }

    private long createPeriod(String icon, String alias, int seconds) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ICON, icon);
        initialValues.put(KEY_ALIAS, alias);
        initialValues.put(KEY_SECONDS, seconds);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    private long updatePeriod(String id, String icon, String alias, int seconds) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ICON, icon);
        initialValues.put(KEY_ALIAS, alias);
        initialValues.put(KEY_SECONDS, seconds);

        String where = KEY_ROWID + " = " + id;

        return mDb.update(SQLITE_TABLE, initialValues, where, null);
    }

    public boolean deleteAllPeriods() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public boolean deleteItem(String key) {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, KEY_ROWID + "=" + key, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchAllPeriods() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID, KEY_ICON, KEY_ALIAS,
                KEY_SECONDS}, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchPeriod(String key) {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID, KEY_ICON, KEY_ALIAS,
                KEY_SECONDS}, KEY_ROWID + " = " + key, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomePeriods() {
        createPeriod(Period.DEFAULT_ICON, "Período 1", Schedule.DEFAULT_TIME);
        createPeriod(Period.DEFAULT_ICON, "Período 2", Schedule.DEFAULT_TIME);
        createPeriod(Period.DEFAULT_ICON, "Período 3", Schedule.DEFAULT_TIME);
    }


}
