package com.example.smartserviceapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.smartserviceapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


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
        final ImageButton yesPr = (ImageButton) view.findViewById(R.id.service_button_yes);
        final ImageButton noPr = (ImageButton) view.findViewById(R.id.service_button_no);
        final ImageButton redactB = (ImageButton) view.findViewById(R.id.rewrite);
        final ImageButton stats = (ImageButton) view.findViewById(R.id.statistics);
        final SmartService service = services.get(position);

        LinearLayout lin = (LinearLayout) view.findViewById(R.id.infification_id);
        final LinearLayout additionalField = (LinearLayout) view.findViewById(R.id.additional_info_panel);
        ArrayList<InfoPrecedent> list  =dbPrecedents.loadPrecedents(service.id);
        boolean isNoPr = false;
        boolean isYesPr = false;
        int amPrY = 0;
        int amPrN = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).label.equals("ok"))
            {
                amPrY++;
                isYesPr = true;
            }
            else if (list.get(i).label.equals("no"))
            {
                amPrN++;
                isNoPr = true;
            }
            if (isYesPr && isNoPr)
            {
                lin.setBackgroundResource(R.drawable.ready_top_bar);
            }
        }
        final int amountNoPr = amPrN;
        final int amountYesPr = amPrY;


        textView.setText(service.name);

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
        noPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.noPrecedent();
                sm.notifyDataSetChanged();
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
            Boolean flag =true;
            @Override
            public void onClick(View v) {
                if (flag)
                {
                    View vie = inflater.inflate(R.layout.additional_item_phone,null);
                    LinearLayout typeLayout = (LinearLayout) vie.findViewById(R.id.task_field_task);
                    TextView type =  (TextView) typeLayout.findViewById(R.id.type_task);
                    type.setText(service.curTask.type);

//                    LinearLayout yesLayout = (LinearLayout) vie.findViewById(R.id.yes_precedent_field_task);
                    TextView yesPrText = (TextView) vie.findViewById(R.id.yes_precedent_number_task);
                    yesPrText.setText(Integer.toString(amountYesPr));

//                    LinearLayout noLayout = (LinearLayout) vie.findViewById(R.id.no_precedent_field_task);
                    TextView noPrText = (TextView) vie.findViewById(R.id.no_precedent_number_task);
                    noPrText.setText(Integer.toString(amountNoPr));

                    additionalField .addView(vie);
                    stats.setImageResource(R.drawable.ic_drop_up);

                    flag = false;
                }
                else
                {
                    additionalField.removeAllViews();
                    stats.setImageResource(R.drawable.ic_drop_down);

                    flag = true;
                }
            }
        });


        dbPrecedents.close();
        return view;
    }

}
