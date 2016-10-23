package br.com.curubodenga.pimfeeder.bluetooth;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.database.Cursor;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.curubodenga.pimfeeder.schedule.Schedule;

public class BluetoothConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Cursor cursor;
    private ProgressDialog progressDialog;

    public BluetoothConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
//        byte[] buffer = new byte[1024];  // buffer store for the stream
//        int bytes; // bytes returned from read()
//
//        // Keep listening to the InputStream until an exception occurs
//        while (true) {
//            try {
//                // Read from the InputStream
//                bytes = mmInStream.read(buffer);
//                // Send the obtained bytes to the UI activity
////                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
////                        .sendToTarget();
//            } catch (IOException e) {
//                break;
//            }
//        }
        write(new byte[]{'i', 'm'});
        do {
            write(new byte[]{'i', 'a'});
            Schedule schedule = Schedule.getSchedule(cursor);
            write(schedule.getBytes());
            write(new byte[]{'f', 'a'});
            SystemClock.sleep(1000);
        } while (cursor.moveToNext());
        write(new byte[]{'f', 'm'});
        progressDialog.dismiss();
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }

    public void setCursor(Cursor cursor){
        this.cursor = cursor;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }
}