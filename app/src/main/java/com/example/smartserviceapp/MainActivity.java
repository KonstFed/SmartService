package com.example.smartserviceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView servicesList;
    private ArrayList<SmartService> services;
    private final int REQUEST_CODE_NEW_SERVICE = 1;
    private final int OK_RESULT = 1;
    DBPrecedents dbPrecedents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        servicesList = (ListView) findViewById(R.id.services_list);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] mre = new String[3];
            mre[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            mre[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            mre[2] = Manifest.permission.CALL_PHONE;
            requestPermissions(mre,4);

        }



        dbPrecedents = new DBPrecedents(this);
//        dbPrecedents.clearPrecedents();
        services = dbPrecedents.loadServices();
        SmartServiceAdapter smartServiceAdapter = new SmartServiceAdapter(this,R.layout.list_item,services);
        servicesList.setAdapter(smartServiceAdapter);
        Log.d("meow","dist:" + TimeMetric.getCoordDist(51.0,50.0,50.0,50.0));
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
        dbPrecedents.clearPrecedents();
        if (dbPrecedents.howMuch()==0) {
            dbPrecedents.addPrecedent(yi1, 0);
            dbPrecedents.addPrecedent(yi2, 0);
            dbPrecedents.addPrecedent(yi3, 0);
            dbPrecedents.addPrecedent(yi4, 0);
            dbPrecedents.addPrecedent(yi5, 0);
            dbPrecedents.addPrecedent(yi6, 0);
            dbPrecedents.addPrecedent(ni1, 0);
            dbPrecedents.addPrecedent(ni2, 0);
            dbPrecedents.addPrecedent(ni3, 0);
        }

        Intent intent = new Intent(this,AddInfoService.class);
        intent.putExtra("status","UPDATE_DB");
        startService(intent);
//        Log.d("meow","size database: " + d.size() );

//        setBottomNavigation();

        Button hm = (Button) findViewById(R.id.howmuch);
        Button del = (Button) findViewById(R.id.del);
        hm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbPrecedents.clearPrecedents();
            }
        });

    }
    public void show()
    {
        Toast.makeText(getApplicationContext(),Integer.toString(dbPrecedents.howMuch()),Toast.LENGTH_SHORT).show();

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
