package com.example.s.bluetooth.misc;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BTClient extends Thread implements BTCommunication.IBTMessage {
    private BluetoothSocket bluetoothSocket;
    private BTCommunication btCommunication;
    private static final String TAG = "BTClient";

    public BTClient(BluetoothDevice bluetoothDevice) {
        super();
        UUID uuid = UUID.fromString("2584b4be-cb42-4836-a813-21055a8c2e58");

        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "BTClient: " + e.getMessage());
        }

    }


    @Override
    public void run() {

        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "run: " + e.getMessage());
                cancel();
                return;
            }
        }


        Log.e(TAG, "run: " + bluetoothSocket);
        btCommunication = new BTCommunication(bluetoothSocket);
        btCommunication.setIbtMessage(this);
        btCommunication.start();

    }

    public void cancel() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "cancel: " + e.getMessage());
            }
        }
    }


    public void write(byte[] data) {
        if (btCommunication != null) {
            btCommunication.writeData(data);
        } else {
            Log.e(TAG, "write: btCommunication is null.");
        }
    }


    @Override
    public void onMessage(String message) {

    }
}
