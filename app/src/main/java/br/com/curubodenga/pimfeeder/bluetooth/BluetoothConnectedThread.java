package br.com.curubodenga.pimfeeder.bluetooth;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.database.Cursor;
import android.os.SystemClock;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.schedule.PimfeederActivity;
import br.com.curubodenga.pimfeeder.schedule.Schedule;
import br.com.curubodenga.pimfeeder.schedule.ScheduleAdjustActivity;

public class BluetoothConnectedThread extends Thread {
    public static BluetoothSocket socket;
    private PimfeederActivity pimfeederActivity;
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Cursor cursor;
    private ProgressDialog progressDialog;

    public BluetoothConnectedThread(BluetoothSocket socket,PimfeederActivity pimfeederActivity, ProgressDialog progressDialog) {
        this.mmSocket = socket;
        this.pimfeederActivity = pimfeederActivity;
        this.progressDialog = progressDialog;

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
        write();
        read();
    }

    private void write(){
        write(new byte[]{'i', 'm'});
        do {
            write(new byte[]{'i', 'a'});
            Schedule schedule = Schedule.getSchedule(cursor);
            write(schedule.getBytes());
            write(new byte[]{'f', 'a'});
            SystemClock.sleep(1000);
        } while (cursor.moveToNext());
        write(new byte[]{'f', 'm'});
    }

    private void read(){
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
        StringBuffer sBuffer = new StringBuffer();

        long tStart = System.currentTimeMillis();
        long tEnd;
        long tDelta;

        while (true) {
            try {
                bytes = mmInStream.read(buffer,0,mmInStream.available());
                String text = new String(buffer);
                sBuffer.append(text.substring(0, bytes));
            } catch (IOException e) {
                break;
            }
            if(sBuffer.toString().equals("imOKfm")){
                progressDialog.dismiss();
                pimfeederActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        String msg = pimfeederActivity.getResources().getString(R.string.sendMessageOK);
                        Toast.makeText(pimfeederActivity, msg, Toast.LENGTH_SHORT).show();
                    }
                });
                pimfeederActivity.goToTargetActivity();
                break;
            }

            tEnd = System.currentTimeMillis();
            tDelta = tEnd - tStart;

            if(tDelta>5000){
                progressDialog.dismiss();
                pimfeederActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        String msg = pimfeederActivity.getResources().getString(R.string.sendMessageFail);
                        Toast.makeText(pimfeederActivity, msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }

        }
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