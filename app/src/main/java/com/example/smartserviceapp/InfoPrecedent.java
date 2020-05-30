package com.example.smartserviceapp;

import android.location.Location;

import java.util.ArrayList;

public class InfoPrecedent {
    Cluster hostCluster;

    double time;
    double lastLat;
    double lastLong;
    double curLat;
    double curLong;
    public InfoPrecedent(double time, double lastLat, double lastLong, double curLat, double curLong) {
        this.time = time;
        this.lastLat = lastLat;
        this.lastLong = lastLong;
        this.curLat = curLat;
        this.curLong = curLong;
        hostCluster = null;
    }
    public InfoPrecedent() {
        this.lastLat = lastLat;
        this.lastLong = lastLong;
        this.curLat = curLat;
        this.curLong = curLong;
        hostCluster = null;
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
        lastLat = curLat;
        lastLong = curLong;
        curLat = loc.getLatitude();
        curLong = loc.getLongitude();

    }
}
