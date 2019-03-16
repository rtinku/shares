package com.example.s.bluetooth.misc;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BTCommunication extends Thread {

    private static final String TAG = "BTCommunication";

    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private IBTMessage ibtMessage;

    public interface IBTMessage {
        void onMessage(String message);
    }


    public BTCommunication(BluetoothSocket bluetoothSocket) {
        super();
        try {
            this.bluetoothSocket = bluetoothSocket;
            this.inputStream = bluetoothSocket.getInputStream();
            this.outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "BTCommunication: " + e.getMessage());
        }
    }


    public void setIbtMessage(IBTMessage ibtMessage) {
        this.ibtMessage = ibtMessage;
    }

    @Override
    public void run() {
        byte[] data = new byte[1024];
        int bytes;

        while (true) {
            try {
                bytes = inputStream.read(data);
                Log.e(TAG, "bytes read =  " + bytes);

                String res = new String(data).trim();
                Log.e(TAG, "run: msg  " + res);


               /* if (ibtMessage != null) {
                    ibtMessage.onMessage(res);
                }*/


            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "run: unable to read data from socket :-" + e.getMessage());
                break;
            }
        }


    }


    public void writeData(byte[] buffer) {
        try {
            outputStream.write(buffer);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, " unable to write data on socket:-" + e.getMessage());
        }
    }


    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }


}
