package br.com.curubodenga.pimfeeder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.curubodenga.pimfeeder.period.PeriodDbAdapter;
import br.com.curubodenga.pimfeeder.schedule.ScheduleDbAdapter;

/**
 * Created by curup on 03/09/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbAdapter";
    private static final String DATABASE_NAME = "PimFeeder";
    protected DatabaseHelper mDbHelper;
    private static final int DATABASE_VERSION = 8;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w(TAG, ScheduleDbAdapter.DATABASE_CREATE);
        Log.w(TAG, PeriodDbAdapter.DATABASE_CREATE);
        db.execSQL(ScheduleDbAdapter.DATABASE_CREATE);
        db.execSQL(PeriodDbAdapter.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + PeriodDbAdapter.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleDbAdapter.TABLE);
        onCreate(db);
    }
}
