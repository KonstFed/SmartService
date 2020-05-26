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
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AddInfoService extends Service {
    private Timer timer;
    private long UPDATE_INTERVAL;
    int currentTime;
    LocationManager mlocmag;
    LocationListener mlocList;
    VectorSVM vectorSVM;
//    private double lat, longn;

    @Override
    public void onCreate() {
        super.onCreate();
        mlocmag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocList = new MyLocationList();

//        currentTime = Calendar.getInstance().get()

        vectorSVM = new VectorSVM();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        Location loc = mlocmag.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null) {
            loc = mlocmag.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        timer  = new Timer();       // location.
        UpdateWithNewLocation(loc); // This method is used to get updated
        mlocmag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,mlocList);

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        mlocmag.removeUpdates(mlocList);
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
//                    final double latitude = loc.getLatitude(); // Updated lat
//                    final double longitude = loc.getLongitude(); // Updated long
                    Calendar rightNow = Calendar.getInstance();
                    currentTime = rightNow.get(Calendar.MINUTE) + rightNow.get(Calendar.HOUR_OF_DAY)*60;
                    vectorSVM.addNewCoord(loc);
                    Log.e("gav",loc.getLatitude() + " : " + loc.getLongitude()+ " time: " + vectorSVM.time);
                    vectorSVM.time = currentTime;
//                    if (vectorSVM.isReady)
//                    {
//                        Intent intent = new Intent();
//                        intent.putExtra("data",vectorSVM.getVector());
//
//                    }
                }
                else {
                }

            }
        }, 0, UPDATE_INTERVAL);
    }


    public class MyLocationList implements LocationListener {

        public void onLocationChanged(Location arg0) {
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
