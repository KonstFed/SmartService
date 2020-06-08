package com.example.smartserviceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class CreateServiceActivity extends AppCompatActivity {
    ArrayList<SmartService> services;
    ServiceTask curTask;
    EditText phoneView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        final DBPrecedents dbPrecedents = new DBPrecedents(getApplicationContext());
        services = dbPrecedents.loadServices();
        phoneView = (EditText) findViewById(R.id.new_service_UI_4);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.new_service_UI_3);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        break;
                    case R.id.phone_task:
                        break;
                    case R.id.sms_task:
                        break;
                }
            }
        });
//        setBottomNavigation();
        Button confirmButtin = (Button) findViewById(R.id.confirm_button);
        confirmButtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameEditView = (EditText) findViewById(R.id.new_service_name);
                String name = nameEditView.getText().toString();
                curTask = new ServicePhoneTask(getApplicationContext(),phoneView.getText().toString());
                SmartService smartService = new SmartService(getApplicationContext(),name,services.size());
                smartService.addTask(curTask);
                dbPrecedents.addService(smartService);
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        setBottomNavigation();
    }

    private void setBottomNavigation()
    {
        ImageButton home = (ImageButton) findViewById(R.id.home);
        ImageButton add_service = (ImageButton) findViewById(R.id.new_service);
        ImageButton settings = (ImageButton) findViewById(R.id.settings);

        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddInfoService.class);
                startActivity(i);
            }
        });

    }
}
