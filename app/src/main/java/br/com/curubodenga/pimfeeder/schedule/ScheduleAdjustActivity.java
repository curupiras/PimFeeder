package br.com.curubodenga.pimfeeder.schedule;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.utils.DateUtils;

public class ScheduleAdjustActivity extends AppCompatActivity {

    Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_adjust);

        this.schedule = new Schedule();


        openTimePicker();
        updateScreen();
    }

    private void updateScreen() {
        updateTimePicker();
        updateDatePicker();
        updateCompleteDay();
        updateWeekDayStatus();
    }

    private void updateTimePicker() {
        TimePicker timePicker = (TimePicker) findViewById(R.id.scheduleAdjustTimePicker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(schedule.getDate());

        timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    private void updateDatePicker() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.scheduleAdjustDatePicker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(schedule.getDate());

        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH));
    }

    private void updateCompleteDay() {
        TextView completeDay = (TextView) findViewById(R.id.completeDayScheduleAdjustTextView);
        completeDay.setText(DateUtils.getCompleteDay(this.schedule.getDate()));
    }

    private void openTimePicker() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.scheduleAdjustDatePicker);
        datePicker.setVisibility(View.GONE);

        TextView okButton = (TextView) findViewById(R.id.dateScheduleAdjustOkButton);
        okButton.setVisibility(View.GONE);

        TimePicker timePicker = (TimePicker) findViewById(R.id.scheduleAdjustTimePicker);
        timePicker.setVisibility(View.VISIBLE);

        LinearLayout weekDayLayout = (LinearLayout) findViewById(R.id.weekDayLayout);
        weekDayLayout.setVisibility(View.VISIBLE);

        TextView saveButton = (TextView) findViewById(R.id.dateScheduleAdjustSaveButton);
        saveButton.setVisibility(View.VISIBLE);
    }

    public void openDatePicker(View view) {
        DatePicker datePicker = (DatePicker) findViewById(R.id.scheduleAdjustDatePicker);
        datePicker.setVisibility(View.VISIBLE);

        TextView okButton = (TextView) findViewById(R.id.dateScheduleAdjustOkButton);
        okButton.setVisibility(View.VISIBLE);

        TimePicker timePicker = (TimePicker) findViewById(R.id.scheduleAdjustTimePicker);
        timePicker.setVisibility(View.GONE);

        LinearLayout weekDayLayout = (LinearLayout) findViewById(R.id.weekDayLayout);
        weekDayLayout.setVisibility(View.GONE);

        TextView saveButton = (TextView) findViewById(R.id.dateScheduleAdjustSaveButton);
        saveButton.setVisibility(View.GONE);
    }

    public void dateSelected(View view) {
        openTimePicker();

        TextView completeDay = (TextView) findViewById(R.id.completeDayScheduleAdjustTextView);
        DatePicker datePicker = (DatePicker) findViewById(R.id.scheduleAdjustDatePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        this.schedule.setDate(calendar.getTime());

        updateScreen();
    }

    private void updateWeekDayStatus() {
        TextView monday = (TextView) findViewById(R.id.mondayButton);
        TextView tuesday = (TextView) findViewById(R.id.tuesdayButton);
        TextView wednesday = (TextView) findViewById(R.id.wednesdayButton);
        TextView thursday = (TextView) findViewById(R.id.thursdayButton);
        TextView friday = (TextView) findViewById(R.id.fridayButton);
        TextView saturday = (TextView) findViewById(R.id.saturdayButton);
        TextView sunday = (TextView) findViewById(R.id.sundayButton);

        if (schedule.getRepeatMon()) {
            monday.setTextColor(Color.BLACK);
        } else {
            monday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatTue()) {
            tuesday.setTextColor(Color.BLACK);
        } else {
            tuesday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatWed()) {
            wednesday.setTextColor(Color.BLACK);
        } else {
            wednesday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatThu()) {
            thursday.setTextColor(Color.BLACK);
        } else {
            thursday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatFri()) {
            friday.setTextColor(Color.BLACK);
        } else {
            friday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatSat()) {
            saturday.setTextColor(Color.BLACK);
        } else {
            saturday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatSun()) {
            sunday.setTextColor(Color.BLACK);
        } else {
            sunday.setTextColor(Color.GRAY);
        }
    }

    public void saturdayToggle(View view) {
        this.schedule.saturdayToggle();
        updateWeekDayStatus();
    }

    public void mondayToggle(View view) {
        this.schedule.mondayToggle();
        updateWeekDayStatus();
    }

    public void tuesdayToggle(View view) {
        this.schedule.tuesdayToggle();
        updateWeekDayStatus();
    }

    public void wednesdayToggle(View view) {
        this.schedule.wednesdayToggle();
        updateWeekDayStatus();
    }

    public void thursdayToggle(View view) {
        this.schedule.thursdayToggle();
        updateWeekDayStatus();
    }

    public void fridayToggle(View view) {
        this.schedule.fridayToggle();
        updateWeekDayStatus();
    }

    public void sundayToggle(View view) {
        this.schedule.sundayToggle();
        updateWeekDayStatus();
    }

    public void save(View view) {
        TimePicker timePicker = (TimePicker) findViewById(R.id.scheduleAdjustTimePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.schedule.getDate());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        this.schedule.setDate(calendar.getTime());

        ScheduleDbAdapter scheduleDbAdapter = new ScheduleDbAdapter(this);
        scheduleDbAdapter.open();
        scheduleDbAdapter.createSchedule(this.schedule);
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }
}
