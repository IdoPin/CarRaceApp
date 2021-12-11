package com.example.carrace.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrace.MSPV3;
import com.example.carrace.R;
import com.example.carrace.Record;
import com.example.carrace.RecordsDB;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Activity_Over extends AppCompatActivity {

    private TextView tv_score;
    private TextInputLayout ti_name;
    private MaterialButton btn_save;
    private int score;
    private RecordsDB database;
    private LocationManager locationManager;
    private Location gps_loc;
    private Location network_loc;
    private Location final_loc;
    private double longitude;
    private double latitude;
    private String userCountry, userAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);
        findViews();
        initButton();
        score = getIntent().getExtras().getInt(Activity_game.SCORE);
        tv_score.setText(""+score);
        getLocation();
    }

    private String getName() {
        return ti_name.getEditText().getText().toString();
    }

    private void getLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        try {

            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else {
            latitude = 0.0;
            longitude = 0.0;
        }


        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                userCountry = addresses.get(0).getCountryName();
                userAddress = addresses.get(0).getAddressLine(0);
            }
            else {
                userCountry = "Unknown";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initButton() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getName();
                getLocation();
                insertRecord(name,score,latitude,longitude);
                Toast.makeText(Activity_Over.this, "Record Saved", Toast.LENGTH_SHORT).show();
                Intent Intent = new Intent(Activity_Over.this, Activity_Menu.class);
                startActivity(Intent);
                Activity_Over.this.finish();
            }
        });
    }

    private void insertRecord(String name, int score, double latitude, double longitude) {
        String js = MSPV3.getMe().getString("DB", "");
        database = new Gson().fromJson(js, RecordsDB.class);
        database.addRecord(new Record()
                .setName(name)
                .setScore(score)
                .setLatitude(latitude)
                .setLongitude(longitude));
        String json = new Gson().toJson(database);
        MSPV3.getMe().putString("DB", json);
    }

    private void findViews() {
        tv_score = findViewById(R.id.tv_score);
        ti_name = findViewById(R.id.ti_name);
        btn_save = findViewById(R.id.btn_save);
    }
}