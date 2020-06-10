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
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class AddInfoService extends Service {
    private static final int servicedelay = 300000;

    private Timer timer;
    private long UPDATE_INTERVAL;
    int currentTime;
    private Location lastLocation;

    ArrayList<SmartService> services;
    LocationManager mlocmag;
    LocationListener mlocList;
    InfoPrecedent vectorSVM;
    DBPrecedents dbPrecedents;

    @Override
    public void onCreate() {
        super.onCreate();


        vectorSVM = new InfoPrecedent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        UPDATE_INTERVAL = 3000;
        dbPrecedents = new DBPrecedents(getApplicationContext());
        services = new ArrayList<>();
        updateFromDB();

        mlocList = new MyLocationList();
        ((MyLocationList) mlocList).setup();
        timer  = new Timer();       // location.
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
//                Log.d("meows","time: "+ vectorSVM.time + " loc: " + vectorSVM.curLat + " " + vectorSVM.curLong);


                curID = intent.getIntExtra("id",-1);
                if (curID==-1)
                {
                    break;
                }
                dbPrecedents.addPrecedent(vectorSVM,curID);
                services.get(curID).setDelay();
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
                services.get(curID).setDelay();

                updateFromDB();
                break;
        }

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

                if (timeMetric.getDist(vectorSVM,preced.get(i))<thresh)
                {
                    minD = preced.get(i);
                }
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
    public double debug_computeDistnace(int id)
    {
        if (!services.get(id).clustering.trained)
        {
            return -1.0;
        }
        ArrayList<InfoPrecedent> preced =  services.get(id).clustering.precedents;
        TimeMetric timeMetric = new TimeMetric();
        double dmin = 500000000;
        InfoPrecedent dminInf = null;
        for (int i = 0; i < preced.size(); i++) {


            if (dmin > timeMetric.getDist(vectorSVM,preced.get(i)))
            {
                dmin = timeMetric.getDist(vectorSVM,preced.get(i));
            }
        }
        return dmin;
    }
    public void updateFromDB()
    {
        ArrayList<SmartService> se = dbPrecedents.loadServices();

        for (int i = 0; i < se.size(); i++) {
            if (i == services.size())
            {
                services.add(se.get(i));
            }
            else
            {
                services.get(i).updateService(se.get(i));

            }
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
        dbPrecedents.close();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }


    public void newPrecedent()
    {

        Calendar rightNow = Calendar.getInstance();
        currentTime = rightNow.get(Calendar.MINUTE) + rightNow.get(Calendar.HOUR_OF_DAY)*60;
        vectorSVM.accuracyRadius = lastLocation.getAccuracy();
        vectorSVM.addNewCoord(lastLocation);
        vectorSVM.time = currentTime;
//        Toast.makeText(getApplicationContext(),vectorSVM.curLat + " : " + vectorSVM.curLong,Toast.LENGTH_SHORT).show();
//        Log.d("meow-coord",vectorSVM.curLat + " : " + vectorSVM.curLong);
    }
    private void checkPrecedent()
    {
        if (lastLocation != null)
        {
            if (lastLocation.getAccuracy()>45)
            {
                return;
            }
            newPrecedent();
//                    SmartService smartService = services.get(0);
            Log.d("meow","i work");
            Toast.makeText(getApplicationContext(),"I WORK",Toast.LENGTH_SHORT).show();
            if (vectorSVM.lastLong != 1000.0) {
//                        Log.d("meow","I am working");
                for (int i = 0; i < services.size(); i++) {
                    testVector(i);
                    if (services.get(i).isReload && testVector(i)) {
                        Calendar rightNow = Calendar.getInstance();

                        Log.d("meow-calls", "service: " + i + ", time: " + rightNow.getTime().toString() + ", thresh: " + services.get(i).clustering.thresh + ", dist: "  +  debug_computeDistnace(i));
                        services.get(i).execute();
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"LOC:WORK",Toast.LENGTH_SHORT).show();

            }
        }
    }
    public class MyLocationList implements LocationListener {


        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 3; // 4 sec

        // Declaring a Location Manager
        protected LocationManager locationManager;
        public void onLocationChanged(Location arg0) {
            lastLocation = arg0;
            checkPrecedent();
//            UpdateWithNewLocation(arg0);
        }
        public void setup()
        {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                locationManager = (LocationManager) getApplicationContext()
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("meow", "Network-location");
                        if (locationManager != null) {
                            lastLocation = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (lastLocation == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("meow", "GPS Enabled Location");
                            if (locationManager != null) {
                                lastLocation = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS Disable ",
                    Toast.LENGTH_LONG).show();
            setup();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS enabled",
                    Toast.LENGTH_LONG).show();
            setup();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
}
