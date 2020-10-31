package com.example.smartbandsdkdemo;

import android.app.Application;
import android.content.Intent;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.ota.OtaService;
import cn.appscomm.ota.util.OtaAppContext;

/**
 * Created by duzhe on 17-1-25-0025.
 */

public class GlobalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothSDK.initSDK(this);
        OtaAppContext.INSTANCE.init(this);
    }
}