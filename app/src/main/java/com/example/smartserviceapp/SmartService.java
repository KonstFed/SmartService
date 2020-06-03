package com.example.smartserviceapp;

public class SmartService {
    String name;
    ServiceTask curTask;
    SmartServiceClustering clustering;
    int id;
    public SmartService(String name, ServiceTask curTask, int id) {
        this.name = name;
        this.curTask = curTask;
        this.id = id;
    }
}
