package com.example.smartbandsdkdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;

public class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Device> deviceList;

    public DeviceListAdapter(Context context, List<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DeviceViewHolder){
            final DeviceViewHolder deviceViewHolder = (DeviceViewHolder) holder;
            ((DeviceViewHolder) holder).txt_name.setText(deviceList.get(position).getName());
            ((DeviceViewHolder) holder).txt_rssi.setText(String.valueOf(deviceList.get(position).getRssi()));
            ((DeviceViewHolder) holder).txt_mac.setText(deviceList.get(position).getMac());
            deviceViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setPositiveButton("Yes", (dialog, id) -> {
                        final Intent intent = new Intent(context, WorkingActivity.class);
                        intent.putExtra("devicename",  deviceList.get(position).getName());
                        intent.putExtra("devicemac",  deviceList.get(position).getMac());
                        context.startActivity(intent);
                        BluetoothSDK.stopScan();
                    });
                    builder.setNeutralButton("No", (dialog, id) -> {
                    });
                    builder.setMessage("DeviceName : " +  deviceList.get(position).getName()+ "\r\n\r\nMAC : " +  deviceList.get(position).getMac());
                    builder.setTitle("Connect the device?");
                    builder.create().show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    private static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_rssi, txt_mac;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.text_device_name);
            txt_rssi = itemView.findViewById(R.id.text_device_rssi);
            txt_mac = itemView.findViewById(R.id.text_device_mac);
        }
    }

}
