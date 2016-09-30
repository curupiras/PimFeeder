package br.com.curubodenga.pimfeeder.schedule;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
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

public class ScheduleActivity extends AppCompatActivity {

    private ScheduleDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private boolean isConnected;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        isConnected = false;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        dbHelper = new ScheduleDbAdapter(this);
        dbHelper.open();

        displayListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

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
        Intent intent = new Intent(this, ScheduleAdjustActivity.class);
        intent.putExtra(ScheduleDbAdapter.KEY_ROWID, key);
        startActivity(intent);
    }

    public void openScheduleAdjustActivity(View view) {
        Intent intent = new Intent(this, ScheduleAdjustActivity.class);
        startActivity(intent);
    }

    public void openScheduleAdjustActivity() {
        Intent intent = new Intent(this, ScheduleAdjustActivity.class);
        startActivity(intent);
    }


    public void deleteItem(View view) {
        ScheduleDbAdapter scheduleDbAdapter = new ScheduleDbAdapter(this);
        scheduleDbAdapter.open();

        View linearLayout = (View) view.getParent().getParent().getParent();
        TextView scheduleItemIdTextView = (TextView) linearLayout.findViewById(R.id.scheduleItemId);
        String itemId = scheduleItemIdTextView.getText().toString();

        scheduleDbAdapter.deleteItem(itemId);
        displayListView();
    }

    public void bluetoothSync() {
        Context context = getApplicationContext();
        CharSequence text;
        Toast toast;

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            text = getResources().getString(R.string.adaptador_bluetooth_nao_encontrado);
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                text = device.getName();
                if (text.equals(getResources().getString(R.string.bluetooth_device_name))) {
                    bluetoothConnect(device);
                    break;
                }
            }
        }
    }

    public void bluetoothConnect(BluetoothDevice device) {
        Context context = getApplicationContext();
        String msg;
        Toast toast;

        BluetoothSocket socket = null;

        try {
            String uuid = getResources().getString(R.string.bluetooth_device_uuid);
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isConnected) {
                msg = getResources().getString(R.string.already_connected);
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.show();
        } else {
            try {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothAdapter.cancelDiscovery();
                socket.connect();
                isConnected = true;
            } catch (IOException e) {
                isConnected = false;
                try {
                    socket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
                msg = getResources().getString(R.string.problema_conectar_bluetooth);
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.show();
            }
            if (isConnected) {
                msg = getResources().getString(R.string.sucesso_conectar_bluetooth);
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


//        BluetoothConnectThread thread = new BluetoothConnectThread(device);
//        thread.run();


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

    private class BluetoothConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public BluetoothConnectThread(BluetoothDevice device) {

            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                String uuid = getResources().getString(R.string.bluetooth_device_uuid);
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.cancelDiscovery();
            Context context = getApplicationContext();
            String msg;
            Toast toast;

            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
                msg = getResources().getString(R.string.problema_conectar_bluetooth);
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.show();
            }
            msg = getResources().getString(R.string.sucesso_conectar_bluetooth);
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.show();

//             TODO: terminar essa função
//            manageConnectedSocket(mmSocket);
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
