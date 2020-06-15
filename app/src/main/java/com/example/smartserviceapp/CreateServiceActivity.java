package com.example.smartserviceapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class CreateServiceActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 5;

    ArrayList<SmartService> services;
    String[] tasksNames = {"Звонок","СМС"};
    int taskType;
    int service_id;
    int service_ind;
    LinearLayout inputFields;
    DBPrecedents dbPrecedents;
    ServiceTask curTask;

    EditText nameEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        dbPrecedents = new DBPrecedents(getApplicationContext());
        services = dbPrecedents.loadServices();

        Intent intent = getIntent();
        service_id = intent.getIntExtra("service_id",-1);
        for (int i = 0; i < services.size(); i++) {
            if (service_id == services.get(i).id)
            {
                service_ind = i;
                LinearLayout l = (LinearLayout) findViewById(R.id.status_button);
                Button b = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 0.5f;
                b.setText("Удалить");
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbPrecedents.changeService(service_id,null);
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });
                b.setLayoutParams(params);
                l.addView(b);
                break;
            }
        }
        inputFields = (LinearLayout) findViewById(R.id.input_fields);

        Spinner taskSpinner = (Spinner) findViewById(R.id.tasks);
        ArrayAdapter<String> taskAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tasksNames);
        taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskSpinner.setAdapter(taskAdapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                LinearLayout inp = (LinearLayout) findViewById(R.id.task_input);
                inp.removeAllViews();
                String item = (String)parent.getItemAtPosition(position);
                switch (item)
                {
                    case "Звонок":
                        taskType = 0;
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view1 = layoutInflater.inflate(R.layout.phone_task,inp,true);
                        nameEdit = (EditText) findViewById(R.id.new_service_name);
                        EditText phoneEdit = (EditText) view1.findViewById(R.id.phoneNumber);
                        phoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

                        ImageButton contact = (ImageButton) view1.findViewById(R.id.contact_button);
                        contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                // BoD con't: CONTENT_TYPE instead of CONTENT_ITEM_TYPE
                                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                                startActivityForResult(intent, RESULT_PICK_CONTACT);
                            }
                        });
                        if (service_id!=-1)
                        {

                            phoneEdit.setText(services.get(service_ind).curTask.getData());
                            nameEdit.setText(services.get(service_ind).name);
                        }
//                        inp.addView(view1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        taskSpinner.setOnItemSelectedListener(itemSelectedListener);
        if (service_id!=-1)
        {
            switch (services.get(service_ind).curTask.type)
            {
                case "phone":
                    taskSpinner.setSelection(0);
                    break;
            }
        }

        Button confirmButtin = (Button) findViewById(R.id.confirm_button);
        confirmButtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartService smartService;
                ServiceTask task;
                String name = nameEdit.getText().toString();
                switch (taskType)
                {
                    case 0:
                        EditText phoneEdit = (EditText) findViewById(R.id.phoneNumber);
                        smartService = new SmartService(getApplicationContext(),name);
                        task = new ServicePhoneTask(getApplicationContext(),phoneEdit.getText().toString());
                        break;
                    default:
                        smartService = null;
                        task = null;
                        break;
                }
                smartService.addTask(task);

                if (service_id == -1)
                {
                    dbPrecedents.addService(smartService);
                }
                else
                {
                    dbPrecedents.changeService(service_id,smartService);
                }
                dbPrecedents.close();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("status","update");
                startActivity(i);
            }
        });
        setBottomNavigation();
    }

    private void setBottomNavigation()
    {
        ImageButton home = (ImageButton) findViewById(R.id.home);
        ImageButton add_service = (ImageButton) findViewById(R.id.new_service);
        ImageButton settings = (ImageButton) findViewById(R.id.settings);

        home.setImageResource(R.drawable.ic_home_inactive_24dp);
        add_service.setImageResource(R.drawable.ic_add_box_black_24dp);
        settings.setImageResource(R.drawable.ic_settings_inactive_24dp);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode)
        {
            case RESULT_PICK_CONTACT:
                if (data != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                        Cursor c = null;
                        try {
                            c = getContentResolver().query(uri, new String[]{
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.TYPE },
                                    null, null, null);

                            if (c != null && c.moveToFirst()) {
                                String number = c.getString(0).replace("+7","8");
                                int type = c.getInt(1);
                                if (taskType == 0)
                                {
                                    EditText phoneNum = findViewById(R.id.phoneNumber);
                                    phoneNum.setText(number);
                                }
                                showSelectedNumber(type, number);
                            }
                        } finally {
                            if (c != null) {
                                c.close();
                            }
                        }
                    }
                }
                break;
        }

    }
    public void showSelectedNumber(int type, String number) {
        Toast.makeText(this, type + ": " + number, Toast.LENGTH_LONG).show();
    }
}
