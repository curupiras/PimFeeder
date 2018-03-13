package br.com.curubodenga.pimfeeder.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.curubodenga.pimfeeder.database.DatabaseHelper;
import br.com.curubodenga.pimfeeder.period.Period;
import br.com.curubodenga.pimfeeder.period.PeriodDbAdapter;


/**
 * Created by curup on 26/07/2016.
 */
public class ScheduleDbAdapter extends DatabaseHelper {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_MONDAY = "monday";
    public static final String KEY_TUESDAY = "tuesday";
    public static final String KEY_WEDNESDAY = "wednesday";
    public static final String KEY_THURSDAY = "thursday";
    public static final String KEY_FRIDAY = "friday";
    public static final String KEY_SATURDAY = "saturday";
    public static final String KEY_SUNDAY = "sunday";
    public static final String KEY_TIME = "time";
    public static final String KEY_AUDIO = "audio";
    public static final String KEY_PERIOD_ID = PeriodDbAdapter.TABLE + "_id";

    private static final String TAG = "ScheduleDbAdapter";
    private SQLiteDatabase mDb;

    public static final String TABLE = "Schedule";

    private final Context context;

    public static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_DATE + " datetime," +
                    KEY_MONDAY + " integer," +
                    KEY_TUESDAY + " integer," +
                    KEY_WEDNESDAY + " integer," +
                    KEY_THURSDAY + " integer," +
                    KEY_FRIDAY + " integer," +
                    KEY_SATURDAY + " integer," +
                    KEY_SUNDAY + " integer," +
                    KEY_TIME + " integer," +
                    KEY_AUDIO + " integer," +
                    KEY_PERIOD_ID + " integer," +
                    " FOREIGN KEY (" + KEY_PERIOD_ID + ") REFERENCES " +
                    PeriodDbAdapter.TABLE + "(" + PeriodDbAdapter.KEY_ROWID + "));";

    public ScheduleDbAdapter(Context context) {
        super(context);
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

    public long createSchedule(Schedule schedule) {
        int sunday = schedule.getRepeatSun() ? 1 : 0;
        int monday = schedule.getRepeatMon() ? 1 : 0;
        int tuesday = schedule.getRepeatTue() ? 1 : 0;
        int wednesday = schedule.getRepeatWed() ? 1 : 0;
        int thursday = schedule.getRepeatThu() ? 1 : 0;
        int friday = schedule.getRepeatFri() ? 1 : 0;
        int saturday = schedule.getRepeatSat() ? 1 : 0;

        if (schedule.getId() == null) {
            return createSchedule(schedule.getDate(), sunday, monday, tuesday, wednesday, thursday,
                    friday, saturday, schedule.getTime(), schedule.getAudio(), schedule.getPeriod
                            ());
        } else {
            return updateSchedule(schedule.getId(), schedule.getDate(), sunday, monday, tuesday,
                    wednesday, thursday, friday, saturday, schedule.getTime(), schedule.getAudio
                            (), schedule.getPeriod());
        }
    }

    private long createSchedule(Date date, int sunday, int monday, int tuesday, int wednesday, int
            thursday, int friday, int saturday, int time, int audio, Period period) {

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
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_AUDIO, audio);
        initialValues.put(KEY_PERIOD_ID, period.getId());

        return mDb.insert(TABLE, null, initialValues);
    }

    private long updateSchedule(String id, Date date, int sunday, int monday, int tuesday, int
            wednesday, int thursday, int friday, int saturday, int time, int audio, Period period) {

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
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_AUDIO, audio);
        initialValues.put(KEY_PERIOD_ID, period.getId());

        String where = KEY_ROWID + " = " + id;

        return mDb.update(TABLE, initialValues, where, null);
    }

    public boolean deleteAllSchedules() {

        int doneDelete = 0;
        doneDelete = mDb.delete(TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public boolean deleteItem(String key) {
        int doneDelete = 0;
        doneDelete = mDb.delete(TABLE, KEY_ROWID + "=" + key, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchAllSchedules() {

        Cursor mCursor = mDb.query(TABLE, new String[]{KEY_ROWID, KEY_DATE, KEY_MONDAY,
                        KEY_TUESDAY, KEY_WEDNESDAY, KEY_THURSDAY, KEY_FRIDAY, KEY_SATURDAY,
                        KEY_SUNDAY, KEY_TIME, KEY_AUDIO, KEY_PERIOD_ID},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchSchedule(String key) {

        Cursor mCursor = mDb.query(TABLE, new String[]{KEY_ROWID, KEY_DATE, KEY_MONDAY,
                        KEY_TUESDAY, KEY_WEDNESDAY, KEY_THURSDAY, KEY_FRIDAY, KEY_SATURDAY,
                        KEY_SUNDAY, KEY_TIME, KEY_AUDIO, KEY_PERIOD_ID},
                KEY_ROWID + " = " + key, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchSchedulesByPeriodId(String periodId) {

        Cursor mCursor = mDb.query(TABLE, new String[]{KEY_ROWID, KEY_DATE, KEY_MONDAY,
                        KEY_TUESDAY, KEY_WEDNESDAY, KEY_THURSDAY, KEY_FRIDAY, KEY_SATURDAY,
                        KEY_SUNDAY, KEY_TIME, KEY_AUDIO, KEY_PERIOD_ID},
                KEY_PERIOD_ID + " = " + periodId, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeSchedules() {

        Date date = new Date();
        Period period = new Period();

        date.setHours(13);
        createSchedule(date, 1, 1, 1, 1, 1, 1, 1, Schedule.DEFAULT_TIME, Schedule.DEFAULT_AUDIO,period);
        date.setHours(14);
        createSchedule(date, 0, 1, 1, 0, 1, 1, 1, Schedule.DEFAULT_TIME, Schedule.DEFAULT_AUDIO,period);
        date.setHours(15);
        createSchedule(date, 1, 0, 1, 0, 1, 1, 0, Schedule.DEFAULT_TIME, Schedule.DEFAULT_AUDIO,period);
        date.setHours(16);
        createSchedule(date, 0, 0, 0, 0, 0, 0, 0, Schedule.DEFAULT_TIME, Schedule.DEFAULT_AUDIO,period);

    }

    public void beginTransaction(){
        mDb.beginTransaction();
    }

    public void rollback(){
        mDb.endTransaction();
    }

    public void commit(){
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }


}
