package com.example.tracknjeep_test;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tracknjeep_test.fragments.Profile;
import com.example.tracknjeep_test.fragments.Home;
import com.example.tracknjeep_test.fragments.Maps;
import com.example.tracknjeep_test.fragments.Notifications;
import com.example.tracknjeep_test.fragments.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomePage extends AppCompatActivity {

    BottomNavigationView footer;
    Home home = new Home();
    Profile add = new Profile();
    Maps maps = new Maps();
    Notifications notifications = new Notifications();
    Settings settings = new Settings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        footer = findViewById(R.id.footer);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();

        footer.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                        return true;
                    case R.id.add:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, add).commit();
                        return true;
                    case R.id.maps:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, maps).commit();
                        return true;
                    case R.id.notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, notifications).commit();
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settings).commit();
                        return true;
                }
                return false;
            }
        });
    }
}