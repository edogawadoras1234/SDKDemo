package com.example.smartbandsdkdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.interfaces.BluetoothScanCallBack;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    Button btn_scan, btn_stop;
    private final String TAG = "Main Activity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    RecyclerView recyclerView;
    DeviceListAdapter deviceListAdapter;
    List<Device> deviceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationPermiss();
        btn_scan = findViewById(R.id.button_scan);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration deviderItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(deviderItemDecoration);

        btn_scan.setOnClickListener(view -> {
            if (BluetoothSDK.startScan(new BluetoothScanCallBack() {
                @Override
                public void onLeScan(BluetoothDevice bluetoothDevice, int rssi) {
                    Log.i(TAG, "deviceName : " + bluetoothDevice.getName() + " MAC : " + bluetoothDevice.getAddress() + " rssi :" + rssi);
                    Toast.makeText(MainActivity.this, "deviceName : " + bluetoothDevice.getName() + " MAC : " + bluetoothDevice.getAddress() + " rssi :" + rssi, Toast.LENGTH_SHORT).show();
                    for (Device device : deviceList) {
                        if (device.getName().equals(bluetoothDevice.getName())) {
                            return;
                        }
                    }
                    String name = bluetoothDevice.getName();
                    String mac = bluetoothDevice.getAddress();
                    Device device = new Device(name, mac, rssi);
                    deviceList.add(device);
                    deviceListAdapter= new DeviceListAdapter(MainActivity.this, deviceList);
                    recyclerView.setAdapter(deviceListAdapter);
                    deviceListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onStopScan(boolean b) {

                }
            }, "")) {
                Toast.makeText(MainActivity.this, "start scan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint("ObsoleteSdkInt")
    private void locationPermiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(dialog -> requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION));
                builder.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "coarse location permission granted");
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Functionality limited");
                builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(dialog -> {
                });
                builder.show();
            }
        }
    }
}