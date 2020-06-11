package com.example.smartserviceapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SmartServiceAdapter extends ArrayAdapter<SmartService> {
    private LayoutInflater inflater;
    private int layout;
    private List<SmartService> services;

    public SmartServiceAdapter(Context context, int resource, List<SmartService> objects) {
        super(context, resource, objects);
        services = objects;
        layout = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.service_name);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.service_button_yes);
        ImageButton b = (ImageButton) view.findViewById(R.id.service_button_no);
        ImageButton redactB = (ImageButton) view.findViewById(R.id.rewrite);
        ImageButton stats = (ImageButton) view.findViewById(R.id.statistics);
        final SmartService service = services.get(position);

        View.OnTouchListener buttonEffect = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageButton)v.findViewById(v.getId())).setPressed(true);
                    return true;
                }
                return false;
            }
        };

        textView.setText(service.name);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.noPrecedent();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.execute();
                service.yesPrecedent();
            }
        });

        redactB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"red",Toast.LENGTH_SHORT).show();
            }
        });
        redactB.setOnTouchListener(buttonEffect);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"stats",Toast.LENGTH_SHORT).show();

            }
        });
        stats.setOnTouchListener(buttonEffect);


        return view;
    }
}
