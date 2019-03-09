package com.example.s.bluetooth.misc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
    private BluetoothDevice device;
    private UUID uuid;

    public BTSocketServer(BluetoothAdapter bluetoothAdapter, String uuid) {
        super(TAG);
        this.bluetoothAdapter = bluetoothAdapter;
        this.uuid = UUID.fromString(uuid);


        if (serverSocket == null) {
            try {
                Log.e(TAG, "BTSocketServer: " + uuid);
                serverSocket = this.bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, this.uuid);

                Log.e(TAG, "BTSocketServer: "+serverSocket );

            } catch (IOException e) {
                Log.e(TAG, "run: " + e.getMessage());
                e.printStackTrace();
                cancel();
            }
        }

    }


    @Override
    public void run() {

        if (serverSocket != null) {
           while (socket==null)
           {
               try {


                   socket=serverSocket.accept();
                   break;
               } catch (IOException e)
               {
                   e.printStackTrace();
                   Log.e(TAG, "run: "+e.getMessage() );
                   cancel(); break;
               }

           }
        }


        if (socket != null) {
            Log.e(TAG, "run: " + socket);
            cancel();
        }

    }


    public void cancel() {

        Log.e(TAG, "cancel: "+socket  +" " +serverSocket);

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();

                Log.e(TAG, "cancel: " + e.getMessage());
            }
        }


        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();

                Log.e(TAG, "cancel: " + e.getMessage());

            }
        }


    }


}
