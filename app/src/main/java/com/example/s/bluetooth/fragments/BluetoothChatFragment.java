package com.example.s.bluetooth.fragments;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.s.bluetooth.R;
import com.example.s.bluetooth.activities.MainActivity;
import com.example.s.bluetooth.adapters.CustomAdapter;
import com.example.s.bluetooth.misc.BTCommunication;
import com.example.s.bluetooth.misc.BTSocketServer;
import com.example.s.bluetooth.receivers.BluetoothEventReceiver;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BluetoothChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothChatFragment extends Fragment implements MainActivity.ICommunicateWithFragment,
        BluetoothEventReceiver.IFoundedBTDevices, BluetoothEventReceiver.IBTDevicesState,
        BluetoothEventReceiver.IEstablishChannel, BluetoothEventReceiver.IBTDisConnected,
        BluetoothEventReceiver.IBTConnected, View.OnClickListener/*,
        BTCommunication.IBTMessage*/ {

    private static final String TAG = "BluetoothChatFragment";

    private static BluetoothChatFragment bluetoothChatFragment;
    private EditText etMessage;
    private Button btnSend;
    private RecyclerView rvDeviceList;
    private Context context;

    private static int REQUEST_EANABLE_BT = 100;
    private static int REQUEST_BT_DISCOVERABLE = 200;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothEventReceiver bluetoothEventReceiver;
    private BTSocketServer socketServer;

    private ArrayList<BluetoothDevice> bluetoothDeviceArrayList;
    private CustomAdapter customAdapter;

    public BluetoothChatFragment() {
        // Required empty public constructor
    }


    public static BluetoothChatFragment newInstance() {

        if (bluetoothChatFragment == null) {
            bluetoothChatFragment = new BluetoothChatFragment();
        }
        return bluetoothChatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothDeviceArrayList = new ArrayList<>();
        customAdapter = new CustomAdapter(context, bluetoothDeviceArrayList);


        // get bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothEventReceiver = new BluetoothEventReceiver();

        bluetoothEventReceiver.setFoundedBTDevices(this);
        bluetoothEventReceiver.setIbtDevicesState(this);
        bluetoothEventReceiver.setiEstablishChannel(this);
        bluetoothEventReceiver.setIbtDisConnected(this);
        bluetoothEventReceiver.setIbtConnected(this);


        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth not support by Device", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_EANABLE_BT);


            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter DeviceFoundintentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter StateintentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        IntentFilter DiscoveryStartintentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        IntentFilter DiscoveryFinishintentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        IntentFilter CONNECTION_STATEintentFilter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        IntentFilter ConnectedStateintentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter DisconnectedStateintentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        IntentFilter BondintentFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //  IntentFilter PairintentFilter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);

        context.registerReceiver(bluetoothEventReceiver, DeviceFoundintentFilter);
        context.registerReceiver(bluetoothEventReceiver, StateintentFilter);
        context.registerReceiver(bluetoothEventReceiver, DiscoveryStartintentFilter);
        context.registerReceiver(bluetoothEventReceiver, DiscoveryFinishintentFilter);
        context.registerReceiver(bluetoothEventReceiver, CONNECTION_STATEintentFilter);
        context.registerReceiver(bluetoothEventReceiver, ConnectedStateintentFilter);
        context.registerReceiver(bluetoothEventReceiver, DisconnectedStateintentFilter);
        context.registerReceiver(bluetoothEventReceiver, BondintentFilter);
        //  context.registerReceiver(bluetoothEventReceiver, PairintentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setListeners();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(bluetoothEventReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EANABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(context, "Bluetooth Turn On...", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_BT_DISCOVERABLE) {


            if (socketServer != null && socketServer.isAlive()) {
                socketServer.cancel();
                socketServer.interrupt();
            }


            socketServer = new BTSocketServer(bluetoothAdapter);
            socketServer.start();

           /* if (resultCode > 0) {
                Log.e(TAG, "onActivityResult: ");
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice("D0:DF:9A:8F:6F:FF");
                device.fetchUuidsWithSdp();
                Parcelable uuidList[] = device.getUuids();

                Log.e(TAG, "onActivityResult: "+uuidList );
                if (uuidList != null && uuidList.length > 0) {

                    socketServer = new BTSocketServer(bluetoothAdapter, uuidList[uuidList.length - 1].toString());
                } else {
                    socketServer = new BTSocketServer(bluetoothAdapter, UUID.randomUUID().toString());
                }

                socketServer.start();
            }*/
        }

    }


    //------------------------------------------------------------------------------------------------------

    private void initViews(View view) {
        etMessage = view.findViewById(R.id.et_message);
        btnSend = view.findViewById(R.id.button_send);
        rvDeviceList = view.findViewById(R.id.rv_device_list);

        rvDeviceList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvDeviceList.setAdapter(customAdapter);

    }


    private void setListeners() {
        btnSend.setOnClickListener(this);
    }


    @Override
    public void onCommunicateWithFragment(int val) {
        switch (val) {
            case 1:
                Toast.makeText(context, "scan", Toast.LENGTH_SHORT).show();

                if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.startDiscovery();
                }

                break;

            case 2:
                Toast.makeText(context, "off", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                Toast.makeText(context, "discover", Toast.LENGTH_SHORT).show();

                if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    Intent BTDiscoveralbe = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    BTDiscoveralbe.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                    startActivityForResult(BTDiscoveralbe, REQUEST_BT_DISCOVERABLE);
                }

                break;
        }


    }

    @Override
    public void onFoundBTDevices(BluetoothDevice bluetoothDevice) {
        bluetoothDeviceArrayList.add(bluetoothDevice);
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBTDevicesState(int state) {

        switch (state) {

            case BluetoothAdapter.STATE_ON:
                Log.e(TAG, "onBTDevicesState: on");

                break;

            case BluetoothAdapter.STATE_TURNING_OFF:
            case BluetoothAdapter.STATE_OFF:

                if (socketServer != null && socketServer.isAlive()) {
                    Log.e(TAG, "onBTDevicesState: " + socketServer.isAlive());
                    Toast.makeText(context, "Service Stop Successfully..." + socketServer.isAlive(), Toast.LENGTH_SHORT).show();
                    socketServer.cancel();
                    customAdapter.closeBluetoothSocket();
                }

                break;
        }
    }

    @Override
    public void onEstablishChannel(BluetoothDevice device) {

    }

    @Override
    public void onBTConnected(String uuid) {
        Log.e(TAG, "onBTConnected: " + uuid);

    }

    @Override
    public void onBTDisConnected() {

        if (socketServer != null && socketServer.isAlive()) {
            socketServer.cancel();
            socketServer = null;
        }
    }

    @Override
    public void onClick(View v) {
        String msg = etMessage.getText().toString();

        if (!msg.isEmpty()) {
            if (socketServer != null) {
                socketServer.write(msg.getBytes());

            } else {

                Log.e(TAG, "onClick: message send by client" );
                customAdapter.write(msg);
            }

        } else {
            Toast.makeText(context, "Empty Can't Send.....", Toast.LENGTH_SHORT).show();
        }


    }

   /* @Override
    public void onMessage(String message)
    {

    }*/
}
