package com.zinc.flavordemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvContent = findViewById(R.id.tv_content);
        tvContent.setText("APPLICATION_ID:" + BuildConfig.APPLICATION_ID + "\n" +
                "VERSION_NAME:" + BuildConfig.VERSION_NAME + "\n" +
                "VERSION_CODE:" + BuildConfig.VERSION_CODE + "\n" +
                "LogicUtils:" + LogicUtils.calculate(1, 2));


    }
}
