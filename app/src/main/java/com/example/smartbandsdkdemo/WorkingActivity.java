package com.example.smartbandsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WorkingActivity extends AppCompatActivity {
    TextView txt_name, txt_mac;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);
        txt_name = findViewById(R.id.text_device_name_working);
        txt_mac = findViewById(R.id.text_device_mac_working);

        Intent intent = getIntent();
        String name = intent.getStringExtra("devicename");
        String mac = intent.getStringExtra("devicemac");
        txt_name.setText(name);
        txt_mac.setText(mac);
    }

}
