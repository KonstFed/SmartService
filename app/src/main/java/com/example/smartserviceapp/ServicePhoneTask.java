package com.example.smartserviceapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;


public class ServicePhoneTask extends ServiceTask {
    Context context;
    String phone;

    public ServicePhoneTask(Context context, String phone) {
        super(context);
        this.context = context;
        this.phone = phone;
    }

    @Override
    public void execute() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        Intent myIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
        context.startActivity(myIntent);


    }
}
