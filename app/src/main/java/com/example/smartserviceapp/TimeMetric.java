package com.example.smartserviceapp;

import android.location.Location;

public class TimeMetric extends Metric {
    private static final int earthRad = 6371000;
    @Override
    public double getDist(InfoPrecedent a, InfoPrecedent b) {
//        double d1 = getCoordDist(a.curLat,a.curLong,b.curLat,b.curLong);
        float[] s = new float[1];
        Location.distanceBetween(a.curLat,a.curLong,b.curLat,b.curLong,s);
//        double dist = Math.sqrt(Math.pow(a.curLat-b.curLat,2) + Math.pow(a.lastLat - b.lastLat,2));
        double d = s[0] + (Math.max(0,a.accuracyRadius) + Math.max(0.0,b.accuracyRadius))/2;
        return d;

    }
    private static double getCoordDist(Double lati1,Double long1, Double lait2, Double long2)
    {
        double lat1 = Math.toRadians(lati1);
        double lon1 = Math.toRadians(long1);
        double lat2 = Math.toRadians(lait2);
        double lon2 = Math.toRadians(long2);

        double t1 = Math.pow(Math.sin((lon1 - lon2)/2),2) +  Math.cos(lon1)*Math.cos(lon2)*Math.pow(Math.sin((lat1 - lat2)/2),2);
        double t2 = 2* Math.asin(Math.sqrt(t1));
        return earthRad*t2;

    }
}
