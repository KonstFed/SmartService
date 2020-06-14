package com.example.smartserviceapp;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private final int REQUEST_CODE_NEW_SERVICE = 1;
    private final int OK_RESULT = 1;
    ListView servicesList;
    private ArrayList<SmartService> services;

    DBPrecedents dbPrecedents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        servicesList = (ListView) findViewById(R.id.services_list);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] mre = new String[4];
            mre[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            mre[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            mre[2] = Manifest.permission.CALL_PHONE;
            mre[3] = Manifest.permission.FOREGROUND_SERVICE;
            requestPermissions(mre, 4);

        }
        SwitchCompat sw = (SwitchCompat) findViewById(R.id.service_switch);

        sw.setChecked(isMyServiceRunning(AddInfoService.class));

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(getApplicationContext(), AddInfoService.class);


                for (int i = 0; i < services.size(); i++) {
                    services.get(i).isTrackerOn = isChecked;
                }
                if (isChecked)
                {

//                    intent.putExtra("status","UPDATE_DB");
////                    ContextCompat.startForegroundService(getApplicationContext(), intent);
//                    startService(intent);
                    startForegroundService();
                }
                else
                {
                    intent.putExtra("status","KILL");

                    startService(intent);
                }
            }
        });
        Intent intent = getIntent();
        if (intent != null)
        {
            if (intent.hasExtra("status"))
            {
                if (intent.getStringExtra("status").equals("update") && sw.isChecked())
                {
                    startForegroundService();
                }
            }
        }
        dbPrecedents = new DBPrecedents(this);
        Log.d("meow","db precedents" + dbPrecedents.howMuch());
        services = dbPrecedents.loadServices();
        SmartServiceAdapter smartServiceAdapter = new SmartServiceAdapter(this,R.layout.list_item,services);
        servicesList.setAdapter(smartServiceAdapter);

        Intent ift = getIntent();
        String s = "";
        if (ift.hasExtra("debug"))
        {
            s = ift.getStringExtra("debug");

        }
        InfoPrecedent yi1 = new InfoPrecedent();
        yi1.curLat = 52.211958;
        yi1.curLong = 104.242408;
        yi1.label = "ok";

        InfoPrecedent yi2 = new InfoPrecedent();
        yi2.curLat = 52.211751;
        yi2.curLong = 104.242310;
        yi2.label = "ok";

        InfoPrecedent yi3 = new InfoPrecedent();
        yi3.curLat = 52.211400;
        yi3.curLong = 104.241915;
        yi3.label = "ok";

        InfoPrecedent yi4 = new InfoPrecedent();
        yi4.curLat = 52.210110;
        yi4.curLong = 104.242506;
        yi4.label = "ok";

        InfoPrecedent yi5 = new InfoPrecedent();
        yi5.curLat = 52.210357;
        yi5.curLong = 104.242450;
        yi5.label = "ok";

        InfoPrecedent yi6 = new InfoPrecedent();
        yi6.curLat = 52.210176;
        yi6.curLong = 104.242489;
        yi6.label = "ok";

        InfoPrecedent ni1 = new InfoPrecedent();
        ni1.curLat = 52.211089;
        ni1.curLong = 104.242313;
        ni1.label  = "no";

        InfoPrecedent ni2 = new InfoPrecedent();
        ni2.curLat = 52.210927;
        ni2.curLong = 104.242352;
        ni2.label  = "no";

        InfoPrecedent ni3 = new InfoPrecedent();
        ni3.curLat = 52.210793;
        ni3.curLong = 104.242377;
        ni3.label  = "no";
//        dbPrecedents.clearPrecedents();
//        if (dbPrecedents.howMuch()==0) {
//            dbPrecedents.addPrecedent(yi1, 0);
//            dbPrecedents.addPrecedent(yi2, 0);
//            dbPrecedents.addPrecedent(yi3, 0);
//            dbPrecedents.addPrecedent(yi4, 0);
//            dbPrecedents.addPrecedent(yi5, 0);
//            dbPrecedents.addPrecedent(yi6, 0);
//            dbPrecedents.addPrecedent(ni1, 0);
//            dbPrecedents.addPrecedent(ni2, 0);
//            dbPrecedents.addPrecedent(ni3, 0);
//        }



        setBottomNavigation();


    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void startForegroundService()
    {
        Intent serviceIntent = new Intent(this, AddInfoService.class);
        serviceIntent.putExtra("status", "UPDATE-DB");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void show()
    {
        Toast.makeText(getApplicationContext(),Integer.toString(dbPrecedents.howMuch()),Toast.LENGTH_SHORT).show();

    }
    private void setBottomNavigation()
    {
        ImageButton home = (ImageButton) findViewById(R.id.home);
        ImageButton add_service = (ImageButton) findViewById(R.id.new_service);
        ImageButton settings = (ImageButton) findViewById(R.id.settings);

        home.setImageResource(R.drawable.ic_home_black_24dp);
        add_service.setImageResource(R.drawable.ic_add_box_anactive_24dp);
        settings.setImageResource(R.drawable.ic_settings_inactive_24dp);

        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CreateServiceActivity.class);

                startActivity(i);
            }
        });

    }

//    @Override
//    protected void onDestroy() {
//        Log.d("meow","MainActivity: onDestroy");
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(this, Restarter.class);
//        this.sendBroadcast(broadcastIntent);
//        Intent i = new Intent(this,AddInfoService.class);
//        stopService(i);
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onStop() {
//        Log.d("meow","MainActivity: onStop");
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(getApplicationContext(), Restarter.class);
//        this.sendBroadcast(broadcastIntent);
//        Intent i = new Intent(this,AddInfoService.class);
//        stopService(i);
//        super.onStop();
//    }
}
