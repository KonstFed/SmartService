package com.example.smartserviceapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SmartServiceAdapter extends ArrayAdapter<SmartService> {
    SmartServiceAdapter sm;
    private LayoutInflater inflater;
    private int layout;
    private List<SmartService> services;
    DBPrecedents dbPrecedents;
    Context context;
    Timer timer;
    public SmartServiceAdapter(Context context, int resource, List<SmartService> objects) {
        super(context, resource, objects);
        services = objects;
        layout = resource;
        inflater = LayoutInflater.from(context);
        this.context = context;
        sm = this;
        timer = new Timer();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        dbPrecedents = new DBPrecedents(context);

        View view=inflater.inflate(this.layout, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.service_name);
        ImageButton yesPr = (ImageButton) view.findViewById(R.id.service_button_yes);
        final ImageButton noPr = (ImageButton) view.findViewById(R.id.service_button_no);
        final ImageButton redactB = (ImageButton) view.findViewById(R.id.rewrite);
        ImageButton stats = (ImageButton) view.findViewById(R.id.statistics);
        final SmartService service = services.get(position);

        LinearLayout lin = (LinearLayout) view.findViewById(R.id.infification_id);
        ArrayList<InfoPrecedent> list  =dbPrecedents.loadPrecedents(service.id);
        boolean isNoPr = false;
        boolean isYesPr = false;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).label.equals("ok")) isYesPr = true;
            else if (list.get(i).label.equals("no")) isNoPr = true;
            if (isYesPr && isNoPr)
            {
                lin.setBackgroundResource(R.drawable.ready_top_bar);
                break;
            }
        }



        textView.setText(service.name);
        noPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.noPrecedent();
                sm.notifyDataSetChanged();
            }
        });
        noPr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        noPr.setBackgroundResource(R.drawable.ic_block_black_pressed_24dp);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {



                        noPr.setBackgroundResource(R.drawable.ic_block_black_24dp);

                        break;
                    }
                }
                return false;
            }
        });
        yesPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.execute();
                service.yesPrecedent();
                sm.notifyDataSetChanged();
            }
        });

        redactB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),CreateServiceActivity.class);
                i.putExtra("service_id",service.id);
                getContext().startActivity(i);

            }
        });
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"stats",Toast.LENGTH_SHORT).show();

            }
        });


        dbPrecedents.close();
        return view;
    }

}
