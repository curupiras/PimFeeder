package br.com.curubodenga.pimfeeder.schedule;

import java.util.Date;

/**
 * Created by curupiras on 18/07/16.
 */
public class Schedule {

    private Date date;
    private Boolean repeatMon;
    private Boolean repeatTue;
    private Boolean repeatWed;
    private Boolean repeatThu;
    private Boolean repeatFri;
    private Boolean repeatSat;
    private Boolean repeatSun;

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
}
