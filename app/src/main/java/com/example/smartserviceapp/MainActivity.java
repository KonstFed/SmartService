package com.example.smartserviceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView servicesList;
    private ArrayList<SmartService> services;
    private final int REQUEST_CODE_NEW_SERVICE = 1;
    private final int OK_RESULT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        services = new ArrayList<>();
        servicesList = (ListView) findViewById(R.id.services_list);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] mre = new String[3];
            mre[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            mre[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            mre[2] = Manifest.permission.CALL_PHONE;
            requestPermissions(mre,4);

        }
        startService(new Intent(this, AddInfoService.class));
        createExamples();


//        setBottomNavigation();

    }
    private void setBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        intent = new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_new:
                        intent = new Intent(getBaseContext(),CreateServiceActivity.class);
                        startActivityForResult(intent,REQUEST_CODE_NEW_SERVICE);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
    private void createExamples()
    {
        SmartServiceAdapter smartServiceAdapter = new SmartServiceAdapter(this,R.layout.list_item,services);
        servicesList.setAdapter(smartServiceAdapter);

        for (int i = 0; i < 25; i++) {
            ServicePhoneTask servicePhoneTask = new ServicePhoneTask(getApplicationContext(),"+79500890841");
            SmartService s = new SmartService("kavo"+i,"ura",servicePhoneTask);
            services.add(s);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode)
        {
            case REQUEST_CODE_NEW_SERVICE:
                if (resultCode == OK_RESULT)
                {
                    Toast.makeText(this,"Я кот",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,"Я пёс",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
