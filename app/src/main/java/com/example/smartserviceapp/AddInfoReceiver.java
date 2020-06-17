package com.example.smartserviceapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AddInfoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.hasExtra("status")) {
//            Toast.makeText(context, "Обнаружено сообщение: " +
//                            intent.getStringExtra("status"),
//                    Toast.LENGTH_LONG).show();
            Log.d("meow", intent.getStringExtra("status"));
        }
        ObservableObject.getInstance().updateValue(intent);


    }
}
