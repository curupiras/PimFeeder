package br.com.curubodenga.pimfeeder.schedule;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
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
import br.com.curubodenga.pimfeeder.bluetooth.BluetoothScheduleThread;
import br.com.curubodenga.pimfeeder.bluetooth.Properties;
import br.com.curubodenga.pimfeeder.period.PeriodActivity;

public class ScheduleActivity extends PimfeederActivity {

    private ScheduleDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private Menu menu;
    private ProgressDialog progressDialog;
    private Properties properties;
    private Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        properties = Properties.getInstance();
        setContentView(R.layout.activity_schedule);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        manageBluetoothConnection();
        dbHelper = new ScheduleDbAdapter(this);
        dbHelper.open();
        displayListView();
    }

    @Override
    public void onResume(){
        super.onResume();
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
        Cursor cursor = dbHelper.fetchAllSchedules();

        String[] scheduleColumns = new String[]{
                ScheduleDbAdapter.KEY_DATE,
                ScheduleDbAdapter.KEY_MONDAY,
                ScheduleDbAdapter.KEY_TUESDAY,
                ScheduleDbAdapter.KEY_WEDNESDAY,
                ScheduleDbAdapter.KEY_THURSDAY,
                ScheduleDbAdapter.KEY_FRIDAY,
                ScheduleDbAdapter.KEY_SATURDAY,
                ScheduleDbAdapter.KEY_SUNDAY,
                ScheduleDbAdapter.KEY_ROWID
        };

        int[] scheduleListLayoutIDs = new int[]{
                R.id.timeTextView,
                R.id.mondayTextView,
                R.id.tuesdayTextView,
                R.id.wednesdayTextView,
                R.id.thursdayTextView,
                R.id.fridayTextView,
                R.id.saturdayTextView,
                R.id.sundayTextView,
                R.id.scheduleItemId
        };

        dataAdapter = new ScheduleItemAdapter(this, R.layout.scheduler_list_layout, cursor,
                scheduleColumns, scheduleListLayoutIDs, 0);

        ListView listView = (ListView) findViewById(R.id.schedulesScheduleActivityListView);
        listView.setAdapter(dataAdapter);

        if (listView.getAdapter().getCount() < 1) {
            openScheduleAdjustActivity();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String key = cursor.getString(cursor.getColumnIndex(ScheduleDbAdapter.KEY_ROWID));
                openScheduleAdjustActivity(key);
            }
        });
    }

    public void openScheduleAdjustActivity(String key) {
        if (properties.isConnectedAndDateSync()) {
            Intent intent = new Intent(this, ScheduleAdjustActivity.class);
            intent.putExtra(ScheduleDbAdapter.KEY_ROWID, key);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ScheduleAdjustActivity.class);
            intent.putExtra(ScheduleDbAdapter.KEY_ROWID, key);
            this.nextActivity=intent;
            bluetoothSync(intent);
        }
    }

    public void openScheduleAdjustActivity(View view) {
        Intent intent = new Intent(this, ScheduleAdjustActivity.class);
        if (properties.isConnectedAndDateSync()) {
            startActivity(intent);
        } else {
            this.nextActivity=intent;
            bluetoothSync(intent);
        }
    }

    public void openScheduleAdjustActivity() {
        Intent intent = new Intent(this, ScheduleAdjustActivity.class);
        startActivity(intent);
    }


    public void deleteItem(View view) {
        if (properties.isConnectedAndDateSync()) {
            View linearLayout = (View) view.getParent().getParent().getParent();
            TextView scheduleItemIdTextView = (TextView) linearLayout.findViewById(R.id
                    .scheduleItemId);
            String itemId = scheduleItemIdTextView.getText().toString();

            dbHelper.deleteItem(itemId);

            sendSchedulesByBluetooth(dbHelper);
            displayListView();
        } else {
            bluetoothSync();
        }
    }

    public void bluetoothSync(Intent intent){
        String loadingWindowName = getResources().getString(R.string.loadingWindowName);
        String msg = getResources().getString(R.string.connectingMessage);
        int i = getRequestedOrientation();
        progressDialog = ProgressDialog.show(this, loadingWindowName, msg);

        Thread bluetoothConnectThread = new BluetoothConnectThread(this, this.progressDialog, intent);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        bluetoothConnectThread.start();
    }

    public void bluetoothSync() {
        this.nextActivity=null;
        bluetoothSync(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_period:
                Intent intent = new Intent(this, PeriodActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_bluetooth:
                bluetoothSync();
                return true;

            case R.id.action_schedule:
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

    private void sendSchedulesByBluetooth(ScheduleDbAdapter adapter) {
        String loadingWindowName = getResources().getString(R.string.loadingWindowName);
        String msg = getResources().getString(R.string.sendingMessage);
        progressDialog = ProgressDialog.show(this, loadingWindowName, msg);

        BluetoothScheduleThread bluetooth = new BluetoothScheduleThread(BluetoothConnectThread
                .socket,this,progressDialog);
        bluetooth.setAdapter(adapter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        bluetooth.start();
    }

    public void updateMenuIcons(){
        if(menu != null){
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_clock_on));
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_chicken_leg_off));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent authActivityResult) {
        super.onActivityResult(requestCode, resultCode, authActivityResult);
        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            bluetoothSync(this.nextActivity);
        }
    }
}
