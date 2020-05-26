package com.example.smartserviceapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

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
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.service_button);

        final SmartService service = services.get(position);

        textView.setText(service.name);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.curTask.execute();
            }
        });




        return view;
    }
}
