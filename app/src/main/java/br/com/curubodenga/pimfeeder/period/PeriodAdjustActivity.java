package br.com.curubodenga.pimfeeder.period;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.bluetooth.BluetoothConnectThread;
import br.com.curubodenga.pimfeeder.bluetooth.Properties;
import br.com.curubodenga.pimfeeder.schedule.PimfeederActivity;
import br.com.curubodenga.pimfeeder.schedule.ScheduleActivity;
import br.com.curubodenga.pimfeeder.schedule.ScheduleDbAdapter;

public class PeriodAdjustActivity extends PimfeederActivity {

    Period period;
    private Properties properties;
    private ProgressDialog progressDialog;
    private int currentImage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        properties = Properties.getInstance();
        setContentView(R.layout.activity_period_adjust);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String key = extras.getString(PeriodDbAdapter.KEY_ROWID);
            this.period = getPeriod(key);
        } else {
            this.period = new Period();
        }

        updateScreen();
    }

    private Period getPeriod(String key) {
        PeriodDbAdapter periodDbAdapter = new PeriodDbAdapter(this);
        periodDbAdapter.open();
        Cursor cursor = periodDbAdapter.fetchPeriod(key);
        return Period.getPeriod(cursor);
    }

    private void updateScreen() {
        updateNumberPicker();
        updateEditText();
        updateImageSwitcher();
    }

    private void updateNumberPicker() {
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.periodAdjustNumberPicker);

        int seconds = period.getSeconds();
        numberPicker.setValue(seconds);
    }

    private void updateEditText() {
        EditText editText = (EditText) findViewById(R.id.aliasEditText);

        String alias = period.getAlias();
        editText.setText(alias);
    }

    private void updateImageSwitcher() {
        ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.periodImageSwitcher);

        String icon = period.getIcon();
//        imageSwitcher.setImageResource(new Integer(icon));

    }

    public void save(View view) {
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.periodAdjustNumberPicker);
        EditText editText = (EditText) findViewById(R.id.aliasEditText);
        ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.periodImageSwitcher);

        this.period.setSeconds(numberPicker.getValue());
        this.period.setAlias(editText.getText().toString());
        this.period.setIcon(""+currentImage);

        PeriodDbAdapter periodDbAdapter = new PeriodDbAdapter(this);
        periodDbAdapter.open();
        periodDbAdapter.createPeriod(this.period);

        Intent intent = new Intent(this, PeriodActivity.class);
        startActivity(intent);
    }

    public void bluetoothSync() {
        String loadingWindowName = getResources().getString(R.string.loadingWindowName);
        String msg = getResources().getString(R.string.connectingMessage);
        progressDialog = ProgressDialog.show(this, loadingWindowName, msg);
        Thread bluetoothConnectThread = new BluetoothConnectThread(this, progressDialog);
        bluetoothConnectThread.start();
    }

}
