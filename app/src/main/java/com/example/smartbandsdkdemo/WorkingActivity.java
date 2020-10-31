package com.example.smartbandsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;
import cn.appscomm.bluetoothsdk.model.HeartRateData;
import cn.appscomm.bluetoothsdk.model.SleepData;
import cn.appscomm.bluetoothsdk.model.SportData;

import static cn.appscomm.ota.util.OtaUtil.byteArrayToHexString;

public class WorkingActivity extends AppCompatActivity {
    TextView txt_name, txt_mac, txt_info;
    Button btn_connect, btn_step, btn_sleep, btn_heart, btn_disconnect;
    String TAG = "Working Activity";
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);
        findViewByIds();
        progressBar.setVisibility(View.GONE);

    }

    public void findViewByIds() {
        txt_name = findViewById(R.id.text_device_name_working);
        txt_mac = findViewById(R.id.text_device_mac_working);
        txt_info = findViewById(R.id.text_information);
        btn_connect = findViewById(R.id.button_connect);
        btn_step = findViewById(R.id.button_step);
        btn_sleep = findViewById(R.id.button_sleep);
        btn_heart = findViewById(R.id.button_heart);
        btn_disconnect = findViewById(R.id.button_disconnect);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String name = intent.getStringExtra("devicename");
        String mac = intent.getStringExtra("devicemac");
        txt_name.setText(name);
        txt_mac.setText(mac);

        btn_connect.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(WorkingActivity.this, "Connecting....", Toast.LENGTH_SHORT).show();
            BluetoothSDK.connectByMAC(resultCallBack, txt_mac.getText().toString());
        });
        btn_disconnect.setOnClickListener(view -> {

            Toast.makeText(WorkingActivity.this, "Đang tính toán....", Toast.LENGTH_SHORT).show();
            BluetoothSDK.disConnect(resultCallBack);
        });
        btn_step.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            txt_info.setText("");
            Toast.makeText(WorkingActivity.this, "Đang tính toán....", Toast.LENGTH_SHORT).show();
            BluetoothSDK.getSportData(resultCallBack);
        });
        btn_sleep.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            txt_info.setText("");
            Toast.makeText(WorkingActivity.this, "Đang tính toán....", Toast.LENGTH_SHORT).show();
            BluetoothSDK.getSleepData(resultCallBack);
        });
        btn_heart.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            txt_info.setText("");
            Toast.makeText(WorkingActivity.this, "Đang tính toán....", Toast.LENGTH_SHORT).show();
            BluetoothSDK.getHeartRateData(resultCallBack);
        });
    }

    ResultCallBack resultCallBack = new ResultCallBack() {
        @Override
        public void onSuccess(int resultType, Object[] objects) {
            switch (resultType) {
                case ResultCallBack.TYPE_CONNECT:
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(WorkingActivity.this, "connected", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "connected");
                    BluetoothSDK.setTransparentPassageCallBack(new ResultCallBack() {
                        @Override
                        public void onSuccess(int result, Object[] objects) {
                            if (result == ResultCallBack.TYPE_TRANSPARENT_PASSAGE_DATA) {
                                byte[] bytes = (byte[]) objects[0];
                                Log.i(TAG, "---" + bytes.length);
                                String str = byteArrayToHexString(bytes);
                                Log.i(TAG, "The result obtained is:" + str);
                            }
                        }

                        @Override
                        public void onFail(int i) {

                        }
                    });
                    break;
                case ResultCallBack.TYPE_DISCONNECT:
                    Toast.makeText(WorkingActivity.this, "disconnected", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "disconnected");
                    break;
                case ResultCallBack.TYPE_GET_SLEEP_DATA:
                    progressBar.setVisibility(View.GONE);
                    if (objects == null || objects.length == 0) {
                        Toast.makeText(WorkingActivity.this, "No sleep data", Toast.LENGTH_SHORT).show();
                    } else {
                        List<SleepData> sleepDataList = (List<SleepData>) objects[0];
                        txt_info.setText(sleepDataList.toString());
                        Log.i(TAG, "---" + sleepDataList.toString());
                    }
                    break;

                case ResultCallBack.TYPE_GET_SPORT_DATA:
                    progressBar.setVisibility(View.GONE);
                    int total_step = 0;
                    int total_distance = 0;
                    int total_calorires = 0;
                    if (objects == null || objects.length == 0) {
                        Toast.makeText(WorkingActivity.this, "No exercise data", Toast.LENGTH_SHORT).show();
                    } else {
                        List<SportData> sportDataList = (List<SportData>) objects[0];
                        for (int i = 0; i < sportDataList.size(); i++) {
                            total_step = total_step + sportDataList.get(i).step;
                            total_distance = total_distance + sportDataList.get(i).distance;
                            total_calorires = total_calorires + sportDataList.get(i).calories;
                            txt_info.setText(sportDataList.toString());
                            Log.i(TAG, "Vào lúc:" + Utils.DateFormat(sportDataList.get(i).time) + " Bạn đi được: " + sportDataList.get(i).step);
                        }
                        Toast.makeText(WorkingActivity.this, "Tổng bước đi bạn đi được:" + total_step, Toast.LENGTH_LONG).show();
                    }
                    break;
                case ResultCallBack.TYPE_GET_HEART_RATE_DATA:
                    progressBar.setVisibility(View.GONE);
                    int total_heart_rate = 0;
                    if (objects == null || objects.length == 0) {
                        Toast.makeText(WorkingActivity.this, "No heart rate data", Toast.LENGTH_SHORT).show();
                    } else {
                        List<HeartRateData> heartRateDataList = (List<HeartRateData>) objects[0];
                        for(int i = 0; i < heartRateDataList.size(); i++){
                            total_heart_rate = total_heart_rate + heartRateDataList.get(i).avg;
                        }
                        txt_info.setText(heartRateDataList.toString());
                        int rate_avg = total_heart_rate/heartRateDataList.size();
                        Toast.makeText(WorkingActivity.this, "Nhịp tim trung bình: " + rate_avg, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Tim:" + rate_avg);
                    }
                    break;
            }
        }

        @Override
        public void onFail(int i) {

        }
    };
}
