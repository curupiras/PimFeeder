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

    public Schedule() {
        this.date = new Date();
        this.repeatMon = false;
        this.repeatTue = false;
        this.repeatWed = false;
        this.repeatThu = false;
        this.repeatFri = false;
        this.repeatSat = false;
        this.repeatSun = false;
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

    public void mondayToggle(){
        if(this.repeatMon){
            this.repeatMon = false;
        }else{
            this.repeatMon = true;
        }
    }
    public void tuesdayToggle(){
        if(this.repeatTue){
            this.repeatTue = false;
        }else{
            this.repeatTue = true;
        }
    }
    public void wednesdayToggle(){
        if(this.repeatWed){
            this.repeatWed = false;
        }else{
            this.repeatWed = true;
        }
    }
    public void thursdayToggle(){
        if(this.repeatThu){
            this.repeatThu = false;
        }else{
            this.repeatThu = true;
        }
    }
    public void fridayToggle(){
        if(this.repeatFri){
            this.repeatFri = false;
        }else{
            this.repeatFri = true;
        }
    }
    public void saturdayToggle(){
        if(this.repeatSat){
            this.repeatSat = false;
        }else{
            this.repeatSat = true;
        }
    }
    public void sundayToggle(){
        if(this.repeatSun){
            this.repeatSun = false;
        }else{
            this.repeatSun = true;
        }
    }
}
