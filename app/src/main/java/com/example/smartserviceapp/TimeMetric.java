package com.example.smartserviceapp;

public class TimeMetric extends Metric {

    @Override
    public double getDist(InfoPrecedent a, InfoPrecedent b) {
        double dist = Math.sqrt(Math.pow(a.curLat-b.curLat,2) + Math.pow(a.lastLat - b.lastLat,2));


        return dist;

    }
}
