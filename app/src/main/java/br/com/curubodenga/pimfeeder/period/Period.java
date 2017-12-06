package br.com.curubodenga.pimfeeder.period;

import android.database.Cursor;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.schedule.Schedule;

/**
 * Created by curupiras on 18/07/16.
 */
public class Period {

    public static final String PERIOD = "PERIOD";
    public static final int DEFAULT_ICON = R.drawable.coxa;
    public static final String DEFAULT_ALIAS = "";

    private String id;
    private int icon;
    private String alias;
    private int seconds;

    public Period() {
        this.id = null;
        this.icon = DEFAULT_ICON;
        this.alias = DEFAULT_ALIAS;
        this.seconds = Schedule.DEFAULT_TIME;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Period getPeriod(Cursor cursor) {

        String id = cursor.getString(cursor.getColumnIndex(PeriodDbAdapter.KEY_ROWID));

        if(id.equals("0")){
            return null;
        }

        Period period = new Period();

        int icon = cursor.getInt(cursor.getColumnIndex(PeriodDbAdapter.KEY_ICON));
        String alias = cursor.getString(cursor.getColumnIndex(PeriodDbAdapter.KEY_ALIAS));
        int seconds = cursor.getInt(cursor.getColumnIndex(PeriodDbAdapter.KEY_SECONDS));

        period.setId(id);
        period.setIcon(icon);
        period.setAlias(alias);
        period.setSeconds(seconds);

        return period;
    }

}
