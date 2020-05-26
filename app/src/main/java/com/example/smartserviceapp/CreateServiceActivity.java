package com.example.smartserviceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class CreateServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        setBottomNavigation();


    }

    private void setBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        intent = new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_new:
                        break;
//                        intent = new Intent(getBaseContext(),CreateServiceActivity.class);
//                        startActivity(intent);
//                        break;
                    case R.id.nav_settings:
                        intent = new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
}
