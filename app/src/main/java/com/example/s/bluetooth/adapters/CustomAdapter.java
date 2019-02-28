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

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    private ArrayList<BluetoothDevice> bluetoothDeviceArrayList;
    private Context context;

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

    public class Holder extends RecyclerView.ViewHolder {
        private TextView textView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
