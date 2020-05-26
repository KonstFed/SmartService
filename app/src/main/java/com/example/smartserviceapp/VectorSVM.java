package com.example.smartserviceapp;

import android.location.Location;

import java.util.ArrayList;

public class VectorSVM {
    double time;
    double lastLat;
    double lastLong;
    double curLat;
    double curLong;
    public boolean isReady = false;
    private int cnt;
    public VectorSVM(double time, double lastLat, double lastLong, double curLat, double curLong) {
        this.time = time;
        this.lastLat = lastLat;
        this.lastLong = lastLong;
        this.curLat = curLat;
        this.curLong = curLong;
        cnt=0;
    }
    public VectorSVM() {
        this.lastLat = lastLat;
        this.lastLong = lastLong;
        this.curLat = curLat;
        this.curLong = curLong;
        cnt = 0;
    }
    public ArrayList<Double> getVector()
    {
        ArrayList<Double> vec =  new ArrayList<Double>();
        vec.add(time);
        vec.add(lastLat);
        vec.add(lastLong);
        vec.add(curLat);
        vec.add(curLong);
        return  vec;
    }
    public void addNewCoord(Location loc)
    {
        cnt+=1;
        lastLat = curLat;
        lastLong = curLong;

        curLat = loc.getLatitude();
        curLong = loc.getLongitude();
        if (cnt>2)
        {
            isReady = true;
        }
    }
}
