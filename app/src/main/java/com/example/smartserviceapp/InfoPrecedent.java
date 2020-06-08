package com.example.smartserviceapp;

import android.location.Location;

import java.util.ArrayList;

public class InfoPrecedent {
    Cluster hostCluster;
    String label;
    double time;
    double lastLat;
    double lastLong;
    double curLat;
    double curLong;
    double accuracyRadius;
    public InfoPrecedent(double time, double lastLat, double lastLong, double curLat, double curLong, String label) {
        this.time = time;
        this.lastLat = lastLat;
        this.lastLong = lastLong;
        this.curLat = curLat;
        this.curLong = curLong;
        this.label = label;
        hostCluster = null;
    }
    public InfoPrecedent() {
        this.lastLat = 1000.0;
        this.lastLong = 1000.0;
        this.curLat = 1000.0;
        this.curLong = 1000.0;
        accuracyRadius = -1.0;
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
