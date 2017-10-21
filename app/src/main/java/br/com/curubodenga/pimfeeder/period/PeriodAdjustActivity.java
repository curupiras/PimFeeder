package br.com.curubodenga.pimfeeder.period;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.bluetooth.BluetoothConnectThread;
import br.com.curubodenga.pimfeeder.bluetooth.Properties;
import br.com.curubodenga.pimfeeder.schedule.PimfeederActivity;
import br.com.curubodenga.pimfeeder.schedule.Schedule;

public class PeriodAdjustActivity extends PimfeederActivity {

    Period period;
    private Properties properties;
    private ProgressDialog progressDialog;
    private int currentImage = 0;
    private CircularList circularList;

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

        initializeScreen();
        updateScreen();
    }

    private Period getPeriod(String key) {
        PeriodDbAdapter periodDbAdapter = new PeriodDbAdapter(this);
        periodDbAdapter.open();
        Cursor cursor = periodDbAdapter.fetchPeriod(key);
        return Period.getPeriod(cursor);
    }

    private void initializeScreen() {
        initializeNumberPicker();
        initializeButtons();
        initializeImageSwitcher();
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

    private void initializeNumberPicker() {
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.periodAdjustNumberPicker);
        numberPicker.setFormatter(new SecondsFormatter());

        numberPicker.setMaxValue(300);
        numberPicker.setMinValue(1);
        numberPicker.setValue(Schedule.DEFAULT_TIME);
        numberPicker.refreshDrawableState();
    }

    private void initializeButtons() {
        Button previewsButton = (Button) findViewById(R.id.previewsButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);

        previewsButton.setBackgroundColor(Color.TRANSPARENT);
        nextButton.setBackgroundColor(Color.TRANSPARENT);

        previewsButton.setTextColor(Color.rgb(0x87,0x87,0x87));
        nextButton.setTextColor(Color.rgb(0x87,0x87,0x87));

        previewsButton.setText("<");
        nextButton.setText(">");

        previewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSwitcher sw = (ImageSwitcher) findViewById(R.id.periodImageSwitcher);
                currentImage = circularList.getPrev();
                sw.setImageResource(currentImage);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSwitcher sw = (ImageSwitcher) findViewById(R.id.periodImageSwitcher);
                currentImage = circularList.getNext();
                sw.setImageResource(currentImage);
            }
        });
    }

    private void initializeImageSwitcher() {

        ImageSwitcher sw = (ImageSwitcher) findViewById(R.id.periodImageSwitcher);
        sw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new
                        ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                return myView;
            }
        });

        circularList = new CircularList();

        circularList.add(R.drawable.steak);
        circularList.add(R.drawable.sausage);
        circularList.add(R.drawable.pernil);
        circularList.add(R.drawable.kebab);
        circularList.add(R.drawable.hamburger);
        circularList.add(R.drawable.fish);
        circularList.add(R.drawable.coxa);
        circularList.add(R.drawable.chicken);

        currentImage = circularList.get(0);
        sw.setImageResource(currentImage);
    }

    private void updateEditText() {
        EditText editText = (EditText) findViewById(R.id.aliasEditText);

        String alias = period.getAlias();
        editText.setText(alias);
    }

    private void updateImageSwitcher() {
        ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.periodImageSwitcher);

        int icon = period.getIcon();
//        imageSwitcher.setImageResource(new Integer(icon));

    }

    public void save(View view) {
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.periodAdjustNumberPicker);
        EditText editText = (EditText) findViewById(R.id.aliasEditText);
        ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.periodImageSwitcher);

        this.period.setSeconds(numberPicker.getValue());
        this.period.setAlias(editText.getText().toString());
        this.period.setIcon(currentImage);

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
