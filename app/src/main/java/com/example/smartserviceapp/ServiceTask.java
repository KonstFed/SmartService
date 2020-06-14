package com.example.smartserviceapp;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;

public abstract class ServiceTask {
    Context context;
    public ContentValues fillCV;
    SmartService hostSmartService;
    String type;
    public ServiceTask(Context context) {
        this.context = context;
    }
//    public abstract void
    public abstract void execute();
    public abstract String getData();

}
