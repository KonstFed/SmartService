package com.example.smartserviceapp;

import android.content.Context;

public abstract class ServiceTask {

    Context context;

    public ServiceTask(Context context) {
        this.context = context;
    }

    public abstract void execute();

}
