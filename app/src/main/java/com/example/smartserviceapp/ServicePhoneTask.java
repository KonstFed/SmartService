package com.example.smartserviceapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;


public class ServicePhoneTask extends ServiceTask {
    Context context;
    String phone;
//    ContentValues cv;
    
    public ServicePhoneTask(Context context, String phone) {
        super(context);
        fillCV = new ContentValues();
        this.context = context;
        this.phone = phone;
        super.type = "phone";
        fillCV.put("type", "PHONE_CALL");
        fillCV.put("phone", phone);
        fillCV.put("message", "");

    }

    @Override
    public void execute() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        Intent myIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);

    }

    @Override
    public String getData() {

        return phone;
    }
}
