package com.example.s.bluetooth.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.s.bluetooth.R;
import com.example.s.bluetooth.fragments.BluetoothChatFragment;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView nv_menu;
    private RelativeLayout rl_fragment_container;


    public interface ICommunicateWithFragment {
        // todo change to bitwise way.
        void onCommunicateWithFragment(int val);
    }

    private ICommunicateWithFragment iCommunicateWithFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();


        BluetoothChatFragment bluetoothChatFragment = BluetoothChatFragment.newInstance();
        iCommunicateWithFragment = bluetoothChatFragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_fragment_container, bluetoothChatFragment)
                .commit();


    }


    private void initViews() {
        rl_fragment_container = findViewById(R.id.rl_fragment_container);
        nv_menu = findViewById(R.id.nv_menu);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

    }

    private void setListeners() {
        nv_menu.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.scan:
                iCommunicateWithFragment.onCommunicateWithFragment(1);
                break;

            case R.id.bluetooth_off:
                iCommunicateWithFragment.onCommunicateWithFragment(2);
                break;

            case R.id.discoverable:
                iCommunicateWithFragment.onCommunicateWithFragment(3);
                break;
        }
        ((DrawerLayout)findViewById(R.id.drawer)).closeDrawers();

        return false;
    }
}
