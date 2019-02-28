package com.example.s.bluetooth.receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class BluetoothEventReceiver extends BroadcastReceiver {

    private static final String TAG = "BluetoothEventReceiver";
    private ArrayList<BluetoothDevice> bluetoothDeviceArrayList = new ArrayList<>();
    private Bundle bundle;
    private IFoundedBTDevices foundedBTDevices;
    private IBTDevicesState ibtDevicesState;

    public void setFoundedBTDevices(IFoundedBTDevices foundedBTDevices) {
        this.foundedBTDevices = foundedBTDevices;
    }


    public void setIbtDevicesState(IBTDevicesState ibtDevicesState) {
        this.ibtDevicesState = ibtDevicesState;
    }

    public interface IFoundedBTDevices {
        void onFoundBTDevices(ArrayList<BluetoothDevice> bluetoothDeviceArrayList);
    }

    public interface IBTDevicesState {
        void onBTDevicesState(int state);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    bluetoothDeviceArrayList.clear();
                    Toast.makeText(context, "Scanning Start...", Toast.LENGTH_SHORT).show();
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Toast.makeText(context, "Scanning Finished...", Toast.LENGTH_SHORT).show();

                    if (foundedBTDevices != null) {
                        foundedBTDevices.onFoundBTDevices(bluetoothDeviceArrayList);
                    }

                    break;

                case BluetoothDevice.ACTION_FOUND:
                    Toast.makeText(context, "FOUND...", Toast.LENGTH_SHORT).show();

                    bundle = intent.getExtras();
                    if (bundle != null) {
                        BluetoothDevice bluetoothDevice = bundle.getParcelable(BluetoothDevice.EXTRA_DEVICE);
                        bluetoothDeviceArrayList.add(bluetoothDevice);
                    }

                    break;


                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Toast.makeText(context, "Connected....", Toast.LENGTH_SHORT).show();
                    break;

                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Toast.makeText(context, "Disconnected....", Toast.LENGTH_SHORT).show();
                    break;

                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    bundle = intent.getExtras();
                    if (bundle != null) {
                        int state = bundle.getInt(BluetoothAdapter.EXTRA_CONNECTION_STATE);

                        String val;
                        switch (state) {
                            case BluetoothAdapter.STATE_CONNECTING:
                                val = "STATE_CONNECTING";
                                break;
                            case BluetoothAdapter.STATE_CONNECTED:
                                val = "STATE_CONNECTED";
                                break;
                            case BluetoothAdapter.STATE_DISCONNECTING:

                                val = "STATE_DISCONNECTING";
                                break;
                            case BluetoothAdapter.STATE_DISCONNECTED:
                                val = "STATE_DISCONNECTED";
                                break;
                            default:
                                val = "?!?!? (" + state + ")";
                                break;
                        }

                        Toast.makeText(context, val, Toast.LENGTH_SHORT).show();

                    }

                    break;


                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    bundle = intent.getExtras();
                    if (bundle != null) {
                        int state = bundle.getInt(BluetoothAdapter.EXTRA_STATE);
                        String val;
                        switch (state) {
                            case BluetoothAdapter.STATE_OFF:
                                val = "OFF";
                                break;
                            case BluetoothAdapter.STATE_TURNING_ON:
                                val = "TURNING_ON";
                                break;
                            case BluetoothAdapter.STATE_ON:

                                val = "ON";
                                break;
                            case BluetoothAdapter.STATE_TURNING_OFF:
                                val = "TURNING_OFF";
                                break;
                            default:
                                val = "?!?!? (" + state + ")";
                                break;
                        }

                        if (ibtDevicesState != null) {
                            ibtDevicesState.onBTDevicesState(state);
                        }
                    }
                    break;
            }

        }
    }
}
