package br.com.curubodenga.pimfeeder.period;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.bluetooth.BluetoothConnectThread;
import br.com.curubodenga.pimfeeder.bluetooth.Properties;
import br.com.curubodenga.pimfeeder.schedule.PimfeederActivity;
import br.com.curubodenga.pimfeeder.schedule.ScheduleActivity;
import br.com.curubodenga.pimfeeder.schedule.ScheduleDbAdapter;

public class PeriodActivity extends PimfeederActivity {

    private PeriodDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private Menu menu;
    private ProgressDialog progressDialog;
    private Properties properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        properties = Properties.getInstance();
        setContentView(R.layout.activity_period);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        manageBluetoothConnection();
        dbHelper = new PeriodDbAdapter(this);
        dbHelper.open();

        displayListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        updateBluetoothIcon();
        updateMenuIcons();

        return true;
    }

    private void displayListView() {
        Cursor cursor = dbHelper.fetchAllPeriods();

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

        dataAdapter = new PeriodItemAdapter(this, R.layout.period_list_layout, cursor,
                periodColumns, periodListLayoutIDs, 0);

        ListView listView = (ListView) findViewById(R.id.periodsPeriodActivityListView);
        listView.setAdapter(dataAdapter);

        if (listView.getAdapter().getCount() < 1) {
            openPeriodAdjustActivity();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String key = cursor.getString(cursor.getColumnIndex(PeriodDbAdapter.KEY_ROWID));
                openPeriodAdjustActivity(key);
            }
        });
    }

    public void openPeriodAdjustActivity(String key) {
        Intent intent = new Intent(this, PeriodAdjustActivity.class);
        intent.putExtra(PeriodDbAdapter.KEY_ROWID, key);
        startActivity(intent);
    }

    public void openPeriodAdjustActivity(View view) {
        Intent intent = new Intent(this, PeriodAdjustActivity.class);
        startActivity(intent);
    }

    public void openPeriodAdjustActivity() {
        Intent intent = new Intent(this, PeriodAdjustActivity.class);
        startActivity(intent);
    }


    public void deleteItem(View view) {
        PeriodDbAdapter periodDbAdapter = new PeriodDbAdapter(this);
        periodDbAdapter.open();

        View linearLayout = (View) view.getParent().getParent().getParent();
        TextView periodItemIdTextView = (TextView) linearLayout.findViewById(R.id.periodItemId);
        String itemId = periodItemIdTextView.getText().toString();

        periodDbAdapter.deleteItem(itemId);

        displayListView();
    }

    public void bluetoothSync() {
        String loadingWindowName = getResources().getString(R.string.loadingWindowName);
        String msg = getResources().getString(R.string.connectingMessage);
        progressDialog = ProgressDialog.show(this, loadingWindowName, msg);
        Thread bluetoothConnectThread = new BluetoothConnectThread(this, this.progressDialog);
        bluetoothConnectThread.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_period:
                return true;

            case R.id.action_bluetooth:
                bluetoothSync();
                return true;

            case R.id.action_schedule:
                Intent intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Device found
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                properties.setConnected(true);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Done searching
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Device is about to disconnect
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                properties.setConnected(false);
                updateBluetoothIcon();
            }
        }
    };

    private void manageBluetoothConnection() {
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter1);
        this.registerReceiver(mReceiver, filter2);
        this.registerReceiver(mReceiver, filter3);
    }

    public void updateBluetoothIcon() {
        if (properties.isConnectedAndDateSync()) {
            menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_ligado));
        } else {
            menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_desligado));
        }
    }

    public void updateMenuIcons(){
        if(menu!=null){
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_clock_off));
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_chicken_leg_on));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent authActivityResult) {
        super.onActivityResult(requestCode, resultCode, authActivityResult);
        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            bluetoothSync();
        }
    }
}
