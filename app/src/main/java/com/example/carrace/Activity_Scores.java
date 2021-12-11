package com.example.carrace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//public class Activity_Scores extends AppCompatActivity implements OnMapReadyCallback {
public class Activity_Scores extends AppCompatActivity {


    private Fragment_List fragmentList;
    private Fragment_Map fragmentMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        fragmentList = new Fragment_List();
        fragmentList.setCallBackList(callBackList);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_list, fragmentList).commit();

        fragmentMap = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_map, fragmentMap).commit();

    }

    CallBack_List callBackList = new CallBack_List() {
        @Override
        public void rowSelected(String name, int score,double latitude, double longitude) {
            fragmentMap.zoom(name,score,latitude,longitude);
        }
    };


}