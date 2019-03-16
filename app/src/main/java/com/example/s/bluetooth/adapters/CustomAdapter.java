package com.example.s.bluetooth.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.s.bluetooth.R;
import com.example.s.bluetooth.misc.BTClient;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    private ArrayList<BluetoothDevice> bluetoothDeviceArrayList;
    private Context context;
    private BTClient btClient;

    public CustomAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDeviceArrayList) {
        this.bluetoothDeviceArrayList = bluetoothDeviceArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        BluetoothDevice device = bluetoothDeviceArrayList.get(i);
        if (device.getName() != null) {
            holder.textView.setText(device.getName());
        } else {
            holder.textView.setText(device.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return bluetoothDeviceArrayList.size();
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (bluetoothDeviceArrayList.size() > 0) {

                if (btClient != null && btClient.isAlive()) {
                    btClient.interrupt();
                    btClient.cancel();

                }

                btClient = new BTClient(bluetoothDeviceArrayList.get(getAdapterPosition()));
                btClient.start();
            }
        }
    }


    public void write(String msg) {
        if (btClient != null)
            btClient.write(msg.getBytes());
    }

    public void closeBluetoothSocket() {
        btClient.cancel();
    }
}
