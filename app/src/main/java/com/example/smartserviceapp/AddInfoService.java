package com.example.smartserviceapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AddInfoService extends Service {
    private Timer timer;
    private long UPDATE_INTERVAL;
    int currentTime;
    final double THRESHOLD=10;
    ArrayList<SmartService> services;
    LocationManager mlocmag;
    LocationListener mlocList;
    InfoPrecedent vectorSVM;
    Location lastLocation;
    DBPrecedents dbPrecedents;

    @Override
    public void onCreate() {
        super.onCreate();
        mlocmag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocList = new MyLocationList();

        vectorSVM = new InfoPrecedent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        dbPrecedents = new DBPrecedents(getApplicationContext());

        updateFromDB();

        Location lastLocation = mlocmag.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation == null) {
            lastLocation = mlocmag.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        timer  = new Timer();       // location.
        UpdateWithNewLocation(lastLocation); // This method is used to get updated
        mlocmag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,mlocList);

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int curID;
        switch (intent.getStringExtra("status"))
        {
            case "UPDATE_DB":
                updateFromDB();
                break;
            case "YES_REQUEST":
                newPrecedent();
                vectorSVM.label = "ok";
                Log.d("meows","time: "+ vectorSVM.time + " loc: " + vectorSVM.curLat + " " + vectorSVM.curLong);


                curID = intent.getIntExtra("id",-1);
                if (curID==-1)
                {
                    break;
                }
                dbPrecedents.addPrecedent(vectorSVM,curID);
                updateFromDB();
                break;
            case "NO_REQUEST":
                newPrecedent();
                vectorSVM.label = "no";
                curID = intent.getIntExtra("id",-1);
                if (curID==-1)
                {
                    break;
                }
                dbPrecedents.addPrecedent(vectorSVM,curID);
                updateFromDB();
                break;
        }
//        switch (intent.getStringExtra("status"))
//        {
//            case "YES_REQUEST":
//                newPrecedent();
//                vectorSVM.label = "ok";
//                Log.d("meows","time: "+ vectorSVM.time);
//                curID = intent.getIntExtra("id",-1);
//                if (curID == -1)
//                {
//                    break;
//                }
//                dbPrecedents.addPrecedent(vectorSVM,curID);
//                serviceClusters.get(curID).addPrecedent(vectorSVM);
//                serviceClusters.get(curID).run(new TimeMetric(),THRESHOLD);
//                break;
//            case "NO_REQUEST":
//                newPrecedent();
//                vectorSVM.label = "no";
//                curID = intent.getIntExtra("id",-1);
//                if (curID == -1)
//                {
//                    break;
//                }
//                dbPrecedents.addPrecedent(vectorSVM,curID);
//                serviceClusters.get(curID).addPrecedent(vectorSVM);
//                serviceClusters.get(curID).run(new TimeMetric(),THRESHOLD);
//                break;
//            case "UPDATE_FROM_DATABASE":
//                break;
//        }
        return START_STICKY;
    }
    public boolean testVector(int id)
    {
        if (!services.get(id).clustering.trained)
        {
            return false;
        }
        ArrayList<InfoPrecedent> preced =  services.get(id).clustering.precedents;
        double thresh = services.get(id).clustering.thresh;
        TimeMetric timeMetric = new TimeMetric();
        InfoPrecedent minD = null;
        for (int i = 0; i < preced.size(); i++) {
            if (minD == null)
            {
                if (timeMetric.getDist(vectorSVM,preced.get(i))<thresh) minD = preced.get(i);
            }
            else if (timeMetric.getDist(vectorSVM,preced.get(i))<timeMetric.getDist(vectorSVM,minD))
            {
                minD = preced.get(i);
            }
        }
        if (minD==null)
        {
            return false;
        }
        else if (minD.label.equals("ok"))
        {
            return true;
        }
        else if (minD.label.equals( "no"))
        {
            return false;
        }
        return false;
    }

    public void updateFromDB()
    {
        services = dbPrecedents.loadServices();
        for (int i = 0; i < services.size(); i++) {
            SmartServiceClustering clustering = new SmartServiceClustering(dbPrecedents.loadPrecedents(i));
            TimeMetric timeMetric = new TimeMetric();
            clustering.run(timeMetric);
            services.get(i).clustering = clustering;

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        mlocmag.removeUpdates(mlocList);
        dbPrecedents.close();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    private void UpdateWithNewLocation(final Location loc) {

        UPDATE_INTERVAL = 3000;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (loc != null) {

                    lastLocation = loc;
                    newPrecedent();
                    if (vectorSVM.lastLong != 1000.0) {
                        Log.d("meow","I am working");
                        for (int i = 0; i < services.size(); i++) {
                            if (testVector(i)) {
                                Log.d("meow", "I pass testVector() with id: " + i);
                                services.get(i).curTask.execute();
                            }
                        }
                    }
                }

            }
        }, 0, UPDATE_INTERVAL);
    }

    public void newPrecedent()
    {

        Calendar rightNow = Calendar.getInstance();
        currentTime = rightNow.get(Calendar.MINUTE) + rightNow.get(Calendar.HOUR_OF_DAY)*60;
        vectorSVM.addNewCoord(lastLocation);
        vectorSVM.time = currentTime;
//        Toast.makeText(getApplicationContext(),vectorSVM.curLat + " : " + vectorSVM.curLong,Toast.LENGTH_SHORT).show();
//        Log.d("meow-coord",vectorSVM.curLat + " : " + vectorSVM.curLong);
    }
    public class MyLocationList implements LocationListener {

        public void onLocationChanged(Location arg0) {
            if (arg0 != null)
            {
                lastLocation = arg0;
            }
            UpdateWithNewLocation(arg0);
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS Disable ",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS enabled",
                    Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
}
