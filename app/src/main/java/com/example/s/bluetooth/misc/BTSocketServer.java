package com.example.s.bluetooth.misc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BTSocketServer extends Thread {
    private static final String TAG = "BTSocketServer";
    private BluetoothServerSocket serverSocket;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private static final String NAME = "ABCD";
    private UUID uuid = UUID.randomUUID();

    public BTSocketServer(BluetoothAdapter bluetoothAdapter) {
        super(TAG);
        this.bluetoothAdapter = bluetoothAdapter;
    }


    @Override
    public void run() {
        super.run();
        while (serverSocket == null) {
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, uuid);
            } catch (IOException e) {
                Log.e(TAG, "run: " + e.getMessage());
                e.printStackTrace();
            }
        }


        if (serverSocket != null) {
            try {

              socket = serverSocket.accept();
                Log.e(TAG, "run: "+socket );

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "run: " + e.getMessage());
            }
        }

    }



}
