package com.example.smartserviceapp;

import android.content.ContentValues;
import android.content.Context;

public abstract class ServiceTask {
    Context context;
    public ContentValues fillCV;
    SmartService hostSmartService;

    public ServiceTask(Context context,SmartService hostSmartService) {
        this.context = context;
        this.hostSmartService = hostSmartService;
    }

    public abstract void execute();

}
