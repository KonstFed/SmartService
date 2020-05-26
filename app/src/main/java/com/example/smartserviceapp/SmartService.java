package com.example.smartserviceapp;

public class SmartService {
    String name;
    String description;
    ServiceTask curTask;
    public SmartService(String name, String description, ServiceTask curTask) {
        this.name = name;
        this.description = description;
        this.curTask = curTask;

    }
}
