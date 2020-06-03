package com.example.smartserviceapp;

public class TimeMetric extends Metric {
    private static final int earthRad = 6371000;
    @Override
    public double getDist(InfoPrecedent a, InfoPrecedent b) {
        double d1 = getCoordDist(a.curLat,a.curLong,b.curLat,b.curLong);
//        double dist = Math.sqrt(Math.pow(a.curLat-b.curLat,2) + Math.pow(a.lastLat - b.lastLat,2));

        return d1;

    }
    public static double getCoordDist(Double lati1,Double long1, Double lait2, Double long2)
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
