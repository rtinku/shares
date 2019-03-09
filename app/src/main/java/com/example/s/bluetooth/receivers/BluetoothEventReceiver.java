package com.example.s.bluetooth.receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

public class BluetoothEventReceiver extends BroadcastReceiver {

    private static final String TAG = "BluetoothEventReceiver";
    private ArrayList<BluetoothDevice> bluetoothDeviceArrayList = new ArrayList<>();
    private Bundle bundle;
    private IFoundedBTDevices foundedBTDevices;
    private IBTDevicesState ibtDevicesState;
    private IEstablishChannel iEstablishChannel;
    private IBTConnected ibtConnected;
    private IBTDisConnected ibtDisConnected;


    public void setFoundedBTDevices(IFoundedBTDevices foundedBTDevices) {
        this.foundedBTDevices = foundedBTDevices;
    }


    public void setIbtDevicesState(IBTDevicesState ibtDevicesState) {
        this.ibtDevicesState = ibtDevicesState;
    }

    public void setiEstablishChannel(IEstablishChannel iEstablishChannel) {
        this.iEstablishChannel = iEstablishChannel;
    }

    public void setIbtConnected(IBTConnected ibtConnected) {
        this.ibtConnected = ibtConnected;
    }

    public void setIbtDisConnected(IBTDisConnected ibtDisConnected) {
        this.ibtDisConnected = ibtDisConnected;
    }


    public interface IFoundedBTDevices {
        void onFoundBTDevices(ArrayList<BluetoothDevice> bluetoothDeviceArrayList);
    }

    public interface IBTDevicesState {
        void onBTDevicesState(int state);
    }


    public interface IEstablishChannel {
        void onEstablishChannel(BluetoothDevice device);
    }


    public interface IBTConnected {
        void onBTConnected(String uuid);
    }

    public interface IBTDisConnected {
        void onBTDisConnected();
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

                case BluetoothDevice.ACTION_PAIRING_REQUEST:
                    /*bundle = intent.getExtras();
                    if (bundle != null) {
                        BluetoothDevice bluetoothDevice = bundle.getParcelable(BluetoothDevice.EXTRA_DEVICE);


                        if (bluetoothDevice != null && ibtConnected != null) {

//                            bluetoothDevice=BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bluetoothDevice.getAddress());

                            Log.e(TAG, "onReceive::: "+bluetoothDevice.getName() );
                            Log.e(TAG, "onReceive::: "+bluetoothDevice.getAddress() );

                            bluetoothDevice.fetchUuidsWithSdp();
                            Parcelable uuidList[] = bluetoothDevice.getUuids();

                            if (uuidList != null && uuidList.length > 0) {
                                UUID uuid = bluetoothDevice.getUuids()[bluetoothDevice.getUuids().length-1].getUuid();
                                Log.e(TAG, "onReceive: ACTION_PAIRING_REQUEST uuid: " + uuid);
                                ibtConnected.onBTConnected(uuid.toString());
                            }

                        }
                    }*/
                    break;




                case BluetoothDevice.ACTION_UUID:

                    bundle = intent.getExtras();
                    if (bundle != null) {
                        UUID uuid = bundle.getParcelable(BluetoothDevice.EXTRA_UUID);

                        if (uuid != null) {
                            Log.e(TAG, "onReceive: " + uuid.toString());
                        }

                    }

                    break;


                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    bundle = intent.getExtras();
                    if (bundle != null) {
                        int state = bundle.getInt(BluetoothDevice.EXTRA_BOND_STATE);
                        String val = "";
                        switch (state) {
                            case BluetoothDevice.BOND_NONE:
                                val = "BOND_NONE";
                                break;
                            case BluetoothDevice.BOND_BONDING:
                                val = "BOND_BONDING";
                                break;
                            case BluetoothDevice.BOND_BONDED:

                                val = "BOND_BONDED";

                                //  BluetoothDevice device = bundle.getParcelable(BluetoothDevice.EXTRA_DEVICE);

                                /*Log.e(TAG, "onReceive: " + device);
                                if (device != null && iEstablishChannel != null) {
                                    device.fetchUuidsWithSdp();
                                    UUID uuid = device.getUuids()[0].getUuid();
                                    Log.e(TAG, "onReceive: " + uuid);
                                    iEstablishChannel.onEstablishChannel(device);
                                }*/


                                /*bundle = intent.getExtras();
                                if (bundle != null) {
                                    //Parcelable [] device = bundle.getParcelableArray(BluetoothDevice.EXTRA_UUID);
                                    BluetoothDevice device = bundle.getParcelable(BluetoothDevice.EXTRA_DEVICE);
                                    Log.e(TAG, "onReceive:BOND_BONDED " + device + "  " + ibtConnected);


                                    if (device != null && ibtConnected != null) {
                                        device.fetchUuidsWithSdp();
                                        UUID uuid = device.getUuids()[device.getUuids().length-1].getUuid();
                                        Log.e(TAG, "onReceive: BOND_BONDED uuid: " + uuid);
                                        ibtConnected.onBTConnected(uuid.toString());
                                    }
                                }*/


                                break;

                        }

                        Toast.makeText(context, val, Toast.LENGTH_SHORT).show();
                    }

                    break;

                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Toast.makeText(context, "Connected....", Toast.LENGTH_SHORT).show();

                    //bundle = intent.getExtras();
                   /* if (bundle != null) {
                        Parcelable[] device = bundle.getParcelableArray(BluetoothDevice.EXTRA_UUID);

                        Log.e(TAG, "onReceive:ACTION_ACL_CONNECTED " + device + "  " + ibtConnected);

                        if (device != null && ibtConnected != null) {


                            for (Parcelable p : device)
                            {
                                Log.e(TAG, "onReceive: " + p.toString());
                            }

                            ibtConnected.onBTConnected(device[0].toString());
                        }
                    }*/


                    bundle = intent.getExtras();
                    if (bundle != null) {
                        //Parcelable [] device = bundle.getParcelableArray(BluetoothDevice.EXTRA_UUID);
                        BluetoothDevice device = bundle.getParcelable(BluetoothDevice.EXTRA_DEVICE);
                        Log.e(TAG, "onReceive:ACTION_ACL_CONNECTED " + device + "  " + ibtConnected);


                        if (device != null && ibtConnected != null) {
                            device.fetchUuidsWithSdp();
                            UUID uuid = device.getUuids()[device.getUuids().length-1].getUuid();
                            Log.e(TAG, "onReceive: ACTION_ACL_CONNECTED uuid: " + uuid);
                            ibtConnected.onBTConnected(uuid.toString());
                        }
                    }



                    break;

                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Toast.makeText(context, "Disconnected....", Toast.LENGTH_SHORT).show();

                    if (ibtDisConnected != null) {
                        ibtDisConnected.onBTDisConnected();
                    }

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
