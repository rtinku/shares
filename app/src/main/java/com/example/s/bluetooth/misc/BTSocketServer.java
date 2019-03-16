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
    private BTCommunication btCommunication;
    private static final String NAME = "XYZ";
    private UUID uuid;

    public BTSocketServer(BluetoothAdapter bluetoothAdapter) {
        super(TAG);
        this.bluetoothAdapter = bluetoothAdapter;


        try {
            Log.e(TAG, "BTSocketServer: " + uuid);
            this.uuid = UUID.fromString("2584b4be-cb42-4836-a813-21055a8c2e58");

            Log.e(TAG, "BTSocketServer: randomUUID :-" + this.uuid);
            serverSocket = this.bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, this.uuid);

            Log.e(TAG, "BTSocketServer: " + serverSocket);

        } catch (IOException e) {
            Log.e(TAG, "BTSocketServer: " + e.getMessage());
            e.printStackTrace();
            cancel();
        }


    }


    @Override
    public void run() {

        if (serverSocket != null) {
            while (socket == null) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: " + e.getMessage());
                    cancel();
                    break;
                }

                if (socket != null) {
                    Log.e(TAG, "run: get socket :-" + socket);
                    btCommunication = new BTCommunication(socket);
                    btCommunication.start();
                    cancel();

                }


            }
        }


    }


    public void cancel() {

        Log.e(TAG, "cancel: " + socket + " " + serverSocket);

        /*if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();

                Log.e(TAG, "cancel: " + e.getMessage());
            }
        }*/


        if (serverSocket != null) {
            try {
                serverSocket.close();
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


}
