package br.com.curubodenga.pimfeeder.schedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.bluetooth.BluetoothConnectThread;
import br.com.curubodenga.pimfeeder.bluetooth.BluetoothScheduleThread;
import br.com.curubodenga.pimfeeder.bluetooth.Properties;
import br.com.curubodenga.pimfeeder.period.Period;
import br.com.curubodenga.pimfeeder.period.PeriodAdjustActivity;
import br.com.curubodenga.pimfeeder.period.PeriodDbAdapter;
import br.com.curubodenga.pimfeeder.period.PeriodItemAdapter;
import br.com.curubodenga.pimfeeder.utils.DateUtils;

public class ScheduleAdjustActivity extends PimfeederActivity {

    Schedule schedule;
    private Spinner spinner;
    private Properties properties;
    private ProgressDialog progressDialog;
    private SimpleCursorAdapter dataAdapter;
    private PeriodDbAdapter periodDbAdapter;
    private ScheduleDbAdapter scheduleDbAdapter;
    private boolean newPeriod = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        properties = Properties.getInstance();
        setContentView(R.layout.activity_schedule_adjust);

        periodDbAdapter = new PeriodDbAdapter(this);
        periodDbAdapter.open();

        scheduleDbAdapter = new ScheduleDbAdapter(this);
        scheduleDbAdapter.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String key = extras.getString(ScheduleDbAdapter.KEY_ROWID);
            this.schedule = getSchedule(key);
        } else {
            this.schedule = new Schedule();
        }

        openTimePicker();
        addListenerOnSpinnerItemSelection();
        updateScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateScreen();
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.scheduleAdjustSpinner);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    private Schedule getSchedule(String key) {
        Cursor cursorSchedule = scheduleDbAdapter.fetchSchedule(key);
        return Schedule.getSchedule(cursorSchedule, periodDbAdapter);
    }

    public void updateScreen() {
        updateTimePicker();
        updateDatePicker();
        updateCompleteDay();
        updateWeekDayStatus();
        updateSpinner();
    }

    private void updateSpinner() {
        spinner = (Spinner) findViewById(R.id.scheduleAdjustSpinner);

        Cursor cursor = periodDbAdapter.fetchAllPeriodsPlusCreate();

        String[] periodColumns = new String[]{
                PeriodDbAdapter.KEY_ICON,
                PeriodDbAdapter.KEY_ALIAS,
                PeriodDbAdapter.KEY_SECONDS,
                ScheduleDbAdapter.KEY_ROWID
        };

        int[] periodListLayoutIDs = new int[]{
                R.id.iconImageView,
                R.id.aliasTextView,
                R.id.secondsTextView,
                R.id.periodItemId
        };

        dataAdapter = new PeriodItemAdapter(this, R.layout.period_list_compact_layout, cursor,
                periodColumns, periodListLayoutIDs, 0);

        spinner.setAdapter(dataAdapter);

        Period period = schedule.getPeriod();
        if (period != null) {
            long periodId = Long.parseLong(period.getId());

            for (int i = 0; i < spinner.getCount(); i++) {
                long itemId = spinner.getItemIdAtPosition(i);
                if (itemId == periodId) {
                    spinner.setSelection(i);
                }
            }
        }

        if (newPeriod == true) {
            spinner.setSelection(spinner.getCount() - 2);
            newPeriod = false;
        }

    }

    private int getIndex(Spinner spinner, String id) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemIdAtPosition(i) == Long.getLong(id)) {
                index = i;
            }
        }
        return index;
    }

    private void updateTimePicker() {
        TimePicker timePicker = (TimePicker) findViewById(R.id.scheduleAdjustTimePicker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(schedule.getDate());

        int hour = calendar.get(Calendar.HOUR);
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            hour = hour + 12;
        }

        timePicker.setCurrentHour(hour);
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

        if (isAllRepeatUnset()) {
            completeDay.setText(DateUtils.getCompleteDay(this.schedule.getDate()));
        } else {
            completeDay.setText(getWeekDaysString());
        }
    }

    private String getWeekDaysString() {
        String weekDayString = "";

        if (schedule.getRepeatMon()) {
            weekDayString = weekDayString + getResources().getString(R.string.mon);
            weekDayString = weekDayString + ", ";
        }
        if (schedule.getRepeatTue()) {
            weekDayString = weekDayString + getResources().getString(R.string.tue);
            weekDayString = weekDayString + ", ";
        }
        if (schedule.getRepeatWed()) {
            weekDayString = weekDayString + getResources().getString(R.string.wed);
            weekDayString = weekDayString + ", ";
        }
        if (schedule.getRepeatThu()) {
            weekDayString = weekDayString + getResources().getString(R.string.thu);
            weekDayString = weekDayString + ", ";
        }
        if (schedule.getRepeatFri()) {
            weekDayString = weekDayString + getResources().getString(R.string.fri);
            weekDayString = weekDayString + ", ";
        }
        if (schedule.getRepeatSat()) {
            weekDayString = weekDayString + getResources().getString(R.string.sat);
            weekDayString = weekDayString + ", ";
        }
        if (schedule.getRepeatSun()) {
            weekDayString = weekDayString + getResources().getString(R.string.sun);
            weekDayString = weekDayString + ", ";
        }

        if (isAllRepeatSet()) {
            return getResources().getString(R.string.allDays);
        } else {
            return weekDayString.substring(0, weekDayString.length() - 2);
        }
    }

    private boolean isAllRepeatUnset() {
        boolean isAllUnsetted = true;
        isAllUnsetted = isAllUnsetted && !schedule.getRepeatMon();
        isAllUnsetted = isAllUnsetted && !schedule.getRepeatTue();
        isAllUnsetted = isAllUnsetted && !schedule.getRepeatWed();
        isAllUnsetted = isAllUnsetted && !schedule.getRepeatThu();
        isAllUnsetted = isAllUnsetted && !schedule.getRepeatFri();
        isAllUnsetted = isAllUnsetted && !schedule.getRepeatSat();
        isAllUnsetted = isAllUnsetted && !schedule.getRepeatSun();

        return isAllUnsetted;
    }

    private boolean isAllRepeatSet() {
        boolean isAllSet = true;
        isAllSet = isAllSet && schedule.getRepeatMon();
        isAllSet = isAllSet && schedule.getRepeatTue();
        isAllSet = isAllSet && schedule.getRepeatWed();
        isAllSet = isAllSet && schedule.getRepeatThu();
        isAllSet = isAllSet && schedule.getRepeatFri();
        isAllSet = isAllSet && schedule.getRepeatSat();
        isAllSet = isAllSet && schedule.getRepeatSun();

        return isAllSet;
    }

    private void openTimePicker() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.scheduleAdjustDatePicker);
        datePicker.setVisibility(View.GONE);

        TextView okButton = (TextView) findViewById(R.id.dateScheduleAdjustOkButton);
        okButton.setVisibility(View.GONE);

        TimePicker timePicker = (TimePicker) findViewById(R.id.scheduleAdjustTimePicker);
        timePicker.setVisibility(View.VISIBLE);
        timePicker.setIs24HourView(true);

        LinearLayout weekDayLayout = (LinearLayout) findViewById(R.id.weekDayLayout);
        weekDayLayout.setVisibility(View.VISIBLE);

        TextView saveButton = (TextView) findViewById(R.id.dateScheduleAdjustSaveButton);
        saveButton.setVisibility(View.VISIBLE);

        LinearLayout topLayout = (LinearLayout) findViewById(R.id.topScheduleAdjustLayout);
        topLayout.setVisibility(View.VISIBLE);
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

        LinearLayout topLayout = (LinearLayout) findViewById(R.id.topScheduleAdjustLayout);
        topLayout.setVisibility(View.GONE);
    }

    public void dateSelected(View view) {
        openTimePicker();

        TextView completeDay = (TextView) findViewById(R.id.completeDayScheduleAdjustTextView);
        DatePicker datePicker = (DatePicker) findViewById(R.id.scheduleAdjustDatePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        this.schedule.setDate(calendar.getTime());
        this.schedule.setAllRepeatFalse();

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
            monday.setTextColor(Color.BLUE);
        } else {
            monday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatTue()) {
            tuesday.setTextColor(Color.BLUE);
        } else {
            tuesday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatWed()) {
            wednesday.setTextColor(Color.BLUE);
        } else {
            wednesday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatThu()) {
            thursday.setTextColor(Color.BLUE);
        } else {
            thursday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatFri()) {
            friday.setTextColor(Color.BLUE);
        } else {
            friday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatSat()) {
            saturday.setTextColor(Color.BLUE);
        } else {
            saturday.setTextColor(Color.GRAY);
        }

        if (schedule.getRepeatSun()) {
            sunday.setTextColor(Color.BLUE);
        } else {
            sunday.setTextColor(Color.GRAY);
        }
    }

    public void saturdayToggle(View view) {
        this.schedule.saturdayToggle();
        updateWeekDayStatus();
        updateCompleteDay();
    }

    public void mondayToggle(View view) {
        this.schedule.mondayToggle();
        updateWeekDayStatus();
        updateCompleteDay();
    }

    public void tuesdayToggle(View view) {
        this.schedule.tuesdayToggle();
        updateWeekDayStatus();
        updateCompleteDay();
    }

    public void wednesdayToggle(View view) {
        this.schedule.wednesdayToggle();
        updateWeekDayStatus();
        updateCompleteDay();
    }

    public void thursdayToggle(View view) {
        this.schedule.thursdayToggle();
        updateWeekDayStatus();
        updateCompleteDay();
    }

    public void fridayToggle(View view) {
        this.schedule.fridayToggle();
        updateWeekDayStatus();
        updateCompleteDay();
    }

    public void sundayToggle(View view) {
        this.schedule.sundayToggle();
        updateWeekDayStatus();
        updateCompleteDay();
    }

    public void save(View view) {
        if (properties.isConnectedAndDateSync()) {
            Cursor cursor = (Cursor) spinner.getSelectedItem();
            Period period = Period.getPeriod(cursor);

            if (period == null) {
                Toast toast = Toast.makeText(this, getResources().getString(R.string.createMeal),
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            TimePicker timePicker = (TimePicker) findViewById(R.id.scheduleAdjustTimePicker);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.schedule.getDate());
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

            this.schedule.setDate(calendar.getTime());
            this.schedule.setPeriod(period);

            scheduleDbAdapter.createSchedule(this.schedule);
            sendSchedulesByBluetooth(scheduleDbAdapter);

            //TODO: Essa parte tem que ser chamada ao receber a resposta do PimFeeder e fechar a
            // janela progressDialog

        } else {
            bluetoothSync();
        }
    }

    public void goToTargetActivity() {
//        Intent intent = new Intent(this, ScheduleActivity.class);
//        startActivity(intent);
        finish();
    }

    private void sendSchedulesByBluetooth(ScheduleDbAdapter adapter) {
        String loadingWindowName = getResources().getString(R.string.loadingWindowName);
        String msg = getResources().getString(R.string.sendingMessage);
        progressDialog = ProgressDialog.show(this, loadingWindowName, msg);

        BluetoothScheduleThread bluetooth = new BluetoothScheduleThread(BluetoothConnectThread
                .socket, this, progressDialog);
        bluetooth.setAdapter(adapter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        bluetooth.start();
    }

    public void bluetoothSync() {
        String loadingWindowName = getResources().getString(R.string.loadingWindowName);
        String msg = getResources().getString(R.string.connectingMessage);
        progressDialog = ProgressDialog.show(this, loadingWindowName, msg);
        Thread bluetoothConnectThread = new BluetoothConnectThread(this, progressDialog);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        bluetoothConnectThread.start();
    }

    public void openPeriodAdjustActivity() {
        Intent intent = new Intent(this, PeriodAdjustActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                updateScreen();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public boolean isNewPeriod() {
        return newPeriod;
    }

    public void setNewPeriod(boolean newPeriod) {
        this.newPeriod = newPeriod;
    }
}
