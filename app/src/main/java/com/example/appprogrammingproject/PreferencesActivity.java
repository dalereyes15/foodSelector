package com.example.appprogrammingproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity {

    TextView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        setting = (TextView) findViewById(R.id.setting);

    }
}
