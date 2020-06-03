package com.example.smartserviceapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;


public class ServicePhoneTask extends ServiceTask {
    Context context;
    String phone;
    ContentValues cv = new ContentValues();
    public ServicePhoneTask(Context context,SmartService hostSmartService, String phone) {
        super(context,hostSmartService);

        this.context = context;
        this.phone = phone;

        cv.put("name",hostSmartService.name);
        cv.put("type", "PHONE_CALL");
        cv.put("phone", phone);
        cv.put("message", "");

        super.fillCV = cv;
    }

    @Override
    public void execute() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        Intent tmp = new Intent(context,AddInfoService.class);
        tmp.putExtra("status","YES_REQUEST");
        tmp.putExtra("id",hostSmartService.id);
        context.startService(tmp);

        Intent myIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
        context.startActivity(myIntent);

    }
}
