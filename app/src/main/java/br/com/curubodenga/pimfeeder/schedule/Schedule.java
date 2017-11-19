package br.com.curubodenga.pimfeeder.schedule;

import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;

import br.com.curubodenga.pimfeeder.utils.DateUtils;

/**
 * Created by curupiras on 18/07/16.
 */
public class Schedule {

    public static final String SCHEDULE = "SCHEDULE";
    public static final int SCHEDULE_BYTE_SIZE = 24;

    public static final int DAY_POSITION_1 = 0;
    public static final int DAY_POSITION_2 = 1;

    public static final int MONTH_POSITION_1 = 2;
    public static final int MONTH_POSITION_2 = 3;

    public static final int YAR_POSITION_1 = 4;
    public static final int YAR_POSITION_2 = 5;
    public static final int YAR_POSITION_3 = 6;
    public static final int YAR_POSITION_4 = 7;

    public static final int HOUR_POSITION_1 = 8;
    public static final int HOUR_POSITION_2 = 9;

    public static final int MINUTE_POSITION_1 = 10;
    public static final int MINUTE_POSITION_2 = 11;

    public static final int WEEK_DAY_POSITION_SUNDAY = 12;
    public static final int WEEK_DAY_POSITION_MONDAY = 13;
    public static final int WEEK_DAY_POSITION_TUESDAY = 14;
    public static final int WEEK_DAY_POSITION_WEDNESDAY = 15;
    public static final int WEEK_DAY_POSITION_THURSDAY = 16;
    public static final int WEEK_DAY_POSITION_FRIDAY = 17;
    public static final int WEEK_DAY_POSITION_SATURDAY = 18;

    public static final int TIME_POSITION_1 = 19;
    public static final int TIME_POSITION_2 = 20;
    public static final int TIME_POSITION_3 = 21;

    public static final int AUDIO_POSITION_1 = 22;
    public static final int AUDIO_POSITION_2 = 23;

    public static final int DEFAULT_AUDIO = 0;
    public static final int DEFAULT_TIME = 18;


    private String id;
    private Date date;
    private Boolean repeatMon;
    private Boolean repeatTue;
    private Boolean repeatWed;
    private Boolean repeatThu;
    private Boolean repeatFri;
    private Boolean repeatSat;
    private Boolean repeatSun;
    private int audio;
    private int time;

    public Schedule() {
        this.date = new Date();
        this.repeatMon = true;
        this.repeatTue = true;
        this.repeatWed = true;
        this.repeatThu = true;
        this.repeatFri = true;
        this.repeatSat = true;
        this.repeatSun = true;
        this.id = null;
        this.audio = DEFAULT_AUDIO;
        this.time = DEFAULT_TIME;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getRepeatMon() {
        return repeatMon;
    }

    public void setRepeatMon(Boolean repeatMon) {
        this.repeatMon = repeatMon;
    }

    public Boolean getRepeatTue() {
        return repeatTue;
    }

    public void setRepeatTue(Boolean repeatTue) {
        this.repeatTue = repeatTue;
    }

    public Boolean getRepeatWed() {
        return repeatWed;
    }

    public void setRepeatWed(Boolean repeatWed) {
        this.repeatWed = repeatWed;
    }

    public Boolean getRepeatThu() {
        return repeatThu;
    }

    public void setRepeatThu(Boolean repeatThu) {
        this.repeatThu = repeatThu;
    }

    public Boolean getRepeatFri() {
        return repeatFri;
    }

    public void setRepeatFri(Boolean repeatFri) {
        this.repeatFri = repeatFri;
    }

    public Boolean getRepeatSat() {
        return repeatSat;
    }

    public void setRepeatSat(Boolean repeatSat) {
        this.repeatSat = repeatSat;
    }

    public Boolean getRepeatSun() {
        return repeatSun;
    }

    public void setRepeatSun(Boolean repeatSun) {
        this.repeatSun = repeatSun;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void mondayToggle() {
        if (this.repeatMon) {
            this.repeatMon = false;
        } else {
            this.repeatMon = true;
        }
    }

    public void tuesdayToggle() {
        if (this.repeatTue) {
            this.repeatTue = false;
        } else {
            this.repeatTue = true;
        }
    }

    public void wednesdayToggle() {
        if (this.repeatWed) {
            this.repeatWed = false;
        } else {
            this.repeatWed = true;
        }
    }

    public void thursdayToggle() {
        if (this.repeatThu) {
            this.repeatThu = false;
        } else {
            this.repeatThu = true;
        }
    }

    public void fridayToggle() {
        if (this.repeatFri) {
            this.repeatFri = false;
        } else {
            this.repeatFri = true;
        }
    }

    public void saturdayToggle() {
        if (this.repeatSat) {
            this.repeatSat = false;
        } else {
            this.repeatSat = true;
        }
    }

    public void sundayToggle() {
        if (this.repeatSun) {
            this.repeatSun = false;
        } else {
            this.repeatSun = true;
        }
    }

    public static Schedule getSchedule(Cursor cursor) {
        Schedule schedule = new Schedule();

        String date = cursor.getString(cursor.getColumnIndex(ScheduleDbAdapter.KEY_DATE));
        String id = cursor.getString(cursor.getColumnIndex(ScheduleDbAdapter.KEY_ROWID));
        int monday = cursor.getInt(cursor.getColumnIndex(ScheduleDbAdapter.KEY_MONDAY));
        int tuesday = cursor.getInt(cursor.getColumnIndex(ScheduleDbAdapter.KEY_TUESDAY));
        int wednesday = cursor.getInt(cursor.getColumnIndex(ScheduleDbAdapter.KEY_WEDNESDAY));
        int thursday = cursor.getInt(cursor.getColumnIndex(ScheduleDbAdapter.KEY_THURSDAY));
        int friday = cursor.getInt(cursor.getColumnIndex(ScheduleDbAdapter.KEY_FRIDAY));
        int saturday = cursor.getInt(cursor.getColumnIndex(ScheduleDbAdapter.KEY_SATURDAY));
        int sunday = cursor.getInt(cursor.getColumnIndex(ScheduleDbAdapter.KEY_SUNDAY));
        int time = cursor.getInt(cursor.getColumnIndex(ScheduleDbAdapter.KEY_TIME));
        int audio = cursor.getInt(cursor.getColumnIndex(ScheduleDbAdapter.KEY_AUDIO));

        schedule.setDate(DateUtils.getDate(date));
        schedule.setId(id);
        schedule.setRepeatMon(monday == 1);
        schedule.setRepeatTue(tuesday == 1);
        schedule.setRepeatWed(wednesday == 1);
        schedule.setRepeatThu(thursday == 1);
        schedule.setRepeatFri(friday == 1);
        schedule.setRepeatSat(saturday == 1);
        schedule.setRepeatSun(sunday == 1);
        schedule.setTime(time);
        schedule.setAudio(audio);

        return schedule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAllRepeatFalse() {
        this.repeatMon = false;
        this.repeatTue = false;
        this.repeatWed = false;
        this.repeatThu = false;
        this.repeatFri = false;
        this.repeatSat = false;
        this.repeatSun = false;
    }

    public byte[] getBytes() {

        Date date = getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int day;
        int month;
        int year;

        if (anyRepeatSetted()) {
            day = 0;
            month = 0;
            year = 0;
        } else {
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);
        }

        int hour = cal.get(Calendar.HOUR_OF_DAY);
//        if(cal.get(Calendar.AM_PM) == Calendar.PM){
//            hour += 12;
//        }
        int minute = cal.get(Calendar.MINUTE);

        byte[] bytes = new byte[SCHEDULE_BYTE_SIZE];
        bytes[DAY_POSITION_1] = (byte) Character.forDigit(day / 10, 10);
        bytes[DAY_POSITION_2] = (byte) Character.forDigit(day % 10, 10);

        bytes[MONTH_POSITION_1] = (byte) Character.forDigit(month / 10, 10);
        bytes[MONTH_POSITION_2] = (byte) Character.forDigit(month % 10, 10);

        bytes[YAR_POSITION_1] = (byte) Character.forDigit(year / 1000, 10);
        bytes[YAR_POSITION_2] = (byte) Character.forDigit(year % 1000 / 100, 10);
        bytes[YAR_POSITION_3] = (byte) Character.forDigit(year % 1000 % 100 / 10, 10);
        bytes[YAR_POSITION_4] = (byte) Character.forDigit(year % 1000 % 100 % 10, 10);

        bytes[HOUR_POSITION_1] = (byte) Character.forDigit(hour / 10, 10);
        bytes[HOUR_POSITION_2] = (byte) Character.forDigit(hour % 10, 10);

        bytes[MINUTE_POSITION_1] = (byte) Character.forDigit(minute / 10, 10);
        bytes[MINUTE_POSITION_2] = (byte) Character.forDigit(minute % 10, 10);

        bytes[WEEK_DAY_POSITION_SUNDAY] = (byte) (repeatSun ? '1' : '0');
        bytes[WEEK_DAY_POSITION_MONDAY] = (byte) (repeatMon ? '1' : '0');
        bytes[WEEK_DAY_POSITION_TUESDAY] = (byte) (repeatTue ? '1' : '0');
        bytes[WEEK_DAY_POSITION_WEDNESDAY] = (byte) (repeatWed ? '1' : '0');
        bytes[WEEK_DAY_POSITION_THURSDAY] = (byte) (repeatThu ? '1' : '0');
        bytes[WEEK_DAY_POSITION_FRIDAY] = (byte) (repeatFri ? '1' : '0');
        bytes[WEEK_DAY_POSITION_SATURDAY] = (byte) (repeatSat ? '1' : '0');

        bytes[TIME_POSITION_1] = (byte) Character.forDigit(time / 100, 10);
        bytes[TIME_POSITION_2] = (byte) Character.forDigit(time % 100 / 10, 10);
        bytes[TIME_POSITION_3] = (byte) Character.forDigit(time % 100 % 10, 10);

        bytes[AUDIO_POSITION_1] = (byte) Character.forDigit(audio / 10, 10);
        bytes[AUDIO_POSITION_2] = (byte) Character.forDigit(audio % 10, 10);

        return bytes;
    }

    private boolean anyRepeatSetted() {
        return repeatSun || repeatMon || repeatTue || repeatWed || repeatThu || repeatFri ||
                repeatSat;
    }
}
