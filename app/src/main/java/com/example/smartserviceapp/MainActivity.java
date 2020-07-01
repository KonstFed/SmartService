package com.example.smartserviceapp;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Observer {
    public static boolean isVisible = true;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_DEBUG = "debug"; // имя кота
    public static final String APP_PREFERENCES_TRACKER = "tracker"; // имя кота
    private ArrayList<SmartService> services;
    private boolean debug;
    SharedPreferences mySharedPreferences;
    ListView servicesList;
    SwitchCompat sw;
    DBPrecedents dbPrecedents;
    TextView debugView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isVisible = true;
        servicesList = (ListView) findViewById(R.id.services_list);
        dbPrecedents = new DBPrecedents(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] mre = new String[4];
            mre[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            mre[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            mre[2] = Manifest.permission.CALL_PHONE;
            mre[3] = Manifest.permission.FOREGROUND_SERVICE;
            requestPermissions(mre, 4);

        }
        mySharedPreferences =  getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        debug = mySharedPreferences.getBoolean(APP_PREFERENCES_DEBUG,false);
        services = dbPrecedents.loadServices();

        if (debug) {
            debugView = (TextView) findViewById(R.id.debug_text);

        }
        sw = (SwitchCompat) findViewById(R.id.service_switch);

        sw.setChecked(isMyServiceRunning(AddInfoService.class));
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(getApplicationContext(), AddInfoService.class);
                for (int i = 0; i < services.size(); i++) {
                    services.get(i).isTrackerOn = isChecked;
                }
                SharedPreferences.Editor e =  mySharedPreferences.edit();
                e.putBoolean(APP_PREFERENCES_TRACKER,isChecked);
                e.apply();
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

        if (mySharedPreferences.contains(APP_PREFERENCES_TRACKER))
        {
            sw.setChecked(mySharedPreferences.getBoolean(APP_PREFERENCES_TRACKER,false));
        }
        updateAdapter();
        Log.d("meow","db precedents" + dbPrecedents.howMuch());


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

        ObservableObject.getInstance().addObserver(this);


        setBottomNavigation();


    }
    private void updateAdapter()
    {
        SmartServiceAdapter smartServiceAdapter = new SmartServiceAdapter(this,R.layout.list_item,services);
        servicesList.setAdapter(smartServiceAdapter);
        for (int i = 0; i < services.size(); i++) {
            services.get(i).isTrackerOn = sw.isChecked();
        }
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

        add_service.setImageResource(R.drawable.ic_add_box_anactive_24dp);
        settings.setImageResource(R.drawable.ic_settings_inactive_24dp);

        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CreateServiceActivity.class);
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;

    }

    @Override
    public void update(Observable o, Object arg) {
        Intent i = (Intent) arg;
        if (i.hasExtra("status")) {
            String com = i.getStringExtra("status");

            Log.d("meow", "got update");
            switch (com) {
                case "update":
                    updateAdapter();
                    Intent is = new Intent(this, AddInfoService.class);
                    is.putExtra("status", "UPDATE_DB");
                    this.startService(is);
                    break;


            }
        }
        else if (i.hasExtra("debug"))
        {

            if (debugView != null) {
                Log.d("meow","MainActivity" + i.getStringExtra("debug"));
                debugView.setText(i.getStringExtra("debug"));
            }

        }
    }
}
