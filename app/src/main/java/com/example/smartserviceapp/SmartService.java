package com.example.smartserviceapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SmartService {
    private static final int delay = 300000;
//    private static final int delay = 10000;
    Boolean isTrackerOn;
    String name;
    ServiceTask curTask;
    SmartServiceClustering clustering;
    Context context;
    boolean isReload;
    int id;
    public SmartService(Context context, String name) {
        this.name = name;
        this.context = context;
        isReload = true;
        isTrackerOn = false;
    }
    public void addTask(ServiceTask serviceTask)
    {
        curTask = serviceTask;
        serviceTask.hostSmartService = this;
        curTask.fillCV.put("name",this.name);
    }
    public void execute()
    {
        curTask.execute();
        Log.d("meow","i've executed");
        setDelay();
    }
    public void setDelay()
    {
        this.isReload = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isReload = true;
            }
        },delay);
    }
    public void updateService(SmartService service)
    {
        this.name = service.name;
        this.curTask = service.curTask;
    }
    public void yesPrecedent() {
        Log.d("meow","yes preced: " + isTrackerOn);
        if (isTrackerOn)
        {
            Toast.makeText(context,"YES PRECED ",Toast.LENGTH_SHORT).show();

            Log.d("meow", name + " YesPreced");
            Intent tmp = new Intent(context,AddInfoService.class);
            tmp.putExtra("status","YES_REQUEST");
            tmp.putExtra("id",this.id);
            context.startService(tmp);
        }
    }

    public void noPrecedent()
    {
        Log.d("meow","no preced:" + isTrackerOn);

        if (isTrackerOn)
        {
            Toast.makeText(context,"NO PRECED " + isTrackerOn,Toast.LENGTH_SHORT).show();

            Log.d("meow", name + " NoPreced");
            Intent tmp = new Intent(context, AddInfoService.class);
            tmp.putExtra("status", "NO_REQUEST");
            tmp.putExtra("id", this.id);
            context.startService(tmp);
        }
    }
}
