package com.example.smartserviceapp;

import android.content.ContentValues;
import android.content.Context;

public abstract class ServiceTask {
    Context context;
    public ContentValues fillCV;
    SmartService hostSmartService;

    public ServiceTask(Context context) {
        this.context = context;
    }
//    public abstract void
    public abstract void execute();

}
