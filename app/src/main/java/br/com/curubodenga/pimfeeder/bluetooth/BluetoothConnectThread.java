package br.com.curubodenga.pimfeeder.bluetooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import br.com.curubodenga.pimfeeder.R;

public class BluetoothConnectThread extends Thread {
    private final static int REQUEST_ENABLE_BT = 1;
    ProgressDialog progressDialog;
    private Properties properties;
    private BluetoothAdapter mBluetoothAdapter;
    public static BluetoothSocket socket;
    private Activity activity;

    public BluetoothConnectThread(Activity activity, ProgressDialog progressDialog) {
        this.activity = activity;
        this.properties = Properties.getInstance();
        this.progressDialog = progressDialog;
    }

    public void run() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    String msg = activity.getResources().getString(R.string
                            .adaptador_bluetooth_nao_encontrado);
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                String text = device.getName();
                if (text.equals(activity.getResources().getString(R.string.bluetooth_device_name)
                )) {
                    bluetoothConnect(device);
                    break;
                }
            }
        }
    }

    public void bluetoothConnect(BluetoothDevice device) {
        if (properties.isConnected()) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    String msg = activity.getResources().getString(R.string.already_connected);
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            try {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                String uuid = activity.getResources().getString(R.string.bluetooth_device_uuid);
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
                mBluetoothAdapter.cancelDiscovery();
                String msg;
                Toast toast;
                socket.connect();
            } catch (IOException connectException) {
                try {
                    socket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        String msg = activity.getResources().getString(R.string
                                .problema_conectar_bluetooth);
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
//             TODO: terminar essa função
//            manageConnectedSocket(mmSocket);
        }
        progressDialog.dismiss();
    }

    /**
     * Will cancel an in-progress connection, and close the socket
     */
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
