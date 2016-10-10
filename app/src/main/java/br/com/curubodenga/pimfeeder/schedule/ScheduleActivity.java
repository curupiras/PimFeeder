package br.com.curubodenga.pimfeeder.schedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.bluetooth.BluetoothConnectThread;
import br.com.curubodenga.pimfeeder.bluetooth.Properties;

public class ScheduleActivity extends AppCompatActivity {

    private ScheduleDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private Menu menu;
    private ProgressDialog progressDialog;
    private Properties properties;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        updateBluetoothIcon();

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

        dataAdapter = new ItemAdapter(this, R.layout.scheduler_list_layout, cursor,
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
        if (properties.isConnected()) {
            Intent intent = new Intent(this, ScheduleAdjustActivity.class);
            intent.putExtra(ScheduleDbAdapter.KEY_ROWID, key);
            startActivity(intent);
        } else {
            bluetoothSync();
        }
    }

    public void openScheduleAdjustActivity(View view) {
        if (properties.isConnected()) {
            Intent intent = new Intent(this, ScheduleAdjustActivity.class);
            startActivity(intent);
        } else {
            bluetoothSync();
        }
    }

    public void openScheduleAdjustActivity() {
        if (properties.isConnected()) {
            Intent intent = new Intent(this, ScheduleAdjustActivity.class);
            startActivity(intent);
        } else {
            bluetoothSync();
        }
    }


    public void deleteItem(View view) {
        if (properties.isConnected()) {
            ScheduleDbAdapter scheduleDbAdapter = new ScheduleDbAdapter(this);
            scheduleDbAdapter.open();

            View linearLayout = (View) view.getParent().getParent().getParent();
            TextView scheduleItemIdTextView = (TextView) linearLayout.findViewById(R.id
                    .scheduleItemId);
            String itemId = scheduleItemIdTextView.getText().toString();

            scheduleDbAdapter.deleteItem(itemId);
            displayListView();
        } else {
            bluetoothSync();
        }
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
//            case R.id.action_settings:
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
//                return true;

            case R.id.action_bluetooth:
                bluetoothSync();
                return true;

            case R.id.action_schedule:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

//    private class BluetoothConnectThread extends Thread {
//        private BluetoothAdapter mBluetoothAdapter;
//        private BluetoothSocket socket;
//        private Activity activity;
//
//        public BluetoothConnectThread(Activity activity) {
//            this.activity = activity;
//        }
//
//        public void run() {
//            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//            if (mBluetoothAdapter == null) {
//                activity.runOnUiThread(new Runnable() {
//                    public void run() {
//                        String msg = getResources().getString(R.string
//                                .adaptador_bluetooth_nao_encontrado);
//                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            if (!mBluetoothAdapter.isEnabled()) {
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            }
//
//            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//            // If there are paired devices
//            if (pairedDevices.size() > 0) {
//                // Loop through paired devices
//                for (BluetoothDevice device : pairedDevices) {
//                    // Add the name and address to an array adapter to show in a ListView
//                    String text = device.getName();
//                    if (text.equals(getResources().getString(R.string.bluetooth_device_name))) {
//                        bluetoothConnect(device);
//                        break;
//                    }
//                }
//            }
//        }
//
//        public void bluetoothConnect(BluetoothDevice device) {
//            if (properties.isConnected()) {
//                activity.runOnUiThread(new Runnable() {
//                    public void run() {
//                        String msg = getResources().getString(R.string.already_connected);
//                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                try {
//                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                    String uuid = getResources().getString(R.string.bluetooth_device_uuid);
//                    socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
//                    mBluetoothAdapter.cancelDiscovery();
//                    String msg;
//                    Toast toast;
//                    socket.connect();
//                } catch (IOException connectException) {
//                    try {
//                        socket.close();
//                    } catch (IOException closeException) {
//                        closeException.printStackTrace();
//                    }
//                    activity.runOnUiThread(new Runnable() {
//                        public void run() {
//                            String msg = getResources().getString(R.string
//                                    .problema_conectar_bluetooth);
//                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                activity.runOnUiThread(new Runnable() {
//                    public void run() {
//                        String msg = getResources().getString(R.string
// .sucesso_conectar_bluetooth);
//                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
////             TODO: terminar essa função
////            manageConnectedSocket(mmSocket);
//            }
//            progressDialog.dismiss();
//        }
//
//        /**
//         * Will cancel an in-progress connection, and close the socket
//         */
//        public void cancel() {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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
                String msg = getResources().getString(R.string.sucesso_conectar_bluetooth);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                updateBluetoothIcon();
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

    private void updateBluetoothIcon() {
        if (properties.isConnected()) {
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_ligado));
        } else {
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_desligado));
        }
    }
}
