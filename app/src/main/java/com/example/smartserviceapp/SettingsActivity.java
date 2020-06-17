package com.example.smartserviceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences mySharedPreferences;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_DEBUG = "debug"; // имя кота
    public static final String APP_PREFERENCES_TRACKER = "tracker"; // имя кота
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mySharedPreferences =  getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SwitchCompat debugSwitch = (SwitchCompat) findViewById(R.id.debug_switch);
        debugSwitch.setChecked(mySharedPreferences.getBoolean(APP_PREFERENCES_DEBUG,false));
        debugSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putBoolean(APP_PREFERENCES_DEBUG,b);
                editor.apply();
            }
        });
        setBottomNavigation();
    }
    private void setBottomNavigation()
    {
        ImageButton home = (ImageButton) findViewById(R.id.home);
        ImageButton add_service = (ImageButton) findViewById(R.id.new_service);
        ImageButton settings = (ImageButton) findViewById(R.id.settings);

        add_service.setImageResource(R.drawable.ic_add_box_anactive_24dp);
        home.setImageResource(R.drawable.ic_home_inactive_24dp);

        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CreateServiceActivity.class);

                startActivity(i);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

    }
}