package com.example.carrace.Activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.carrace.Activity_Scores;
import com.example.carrace.R;
import com.google.android.material.button.MaterialButton;


public class Activity_Menu extends AppCompatActivity {


    private MaterialButton menu_BTN_acc;
    private MaterialButton menu_BTN_btns;
    private MaterialButton menu_BTN_scores;
    public static final String BUNDLE = "BUNDLE";
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findView();
        setOnClick();
    }

    private void findView() {
        menu_BTN_acc = findViewById(R.id.menu_BTN_acc);
        menu_BTN_btns = findViewById(R.id.menu_BTN_btns);
        menu_BTN_scores = findViewById(R.id.menu_BTN_scores);
    }

    private void setOnClick() {
        menu_BTN_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity(true);
            }
        });

        menu_BTN_btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity(false);
            }
        });
        menu_BTN_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent;
                myIntent = new Intent(Activity_Menu.this, Activity_Scores.class);
                startActivity(myIntent);
            }
        });
    }

    private void GameActivity(boolean snsFlag) {
        Intent myIntent;
        bundle = new Bundle();
        bundle.putBoolean(Activity_game.SENSOR_TYPE,snsFlag);
        myIntent = new Intent(this, Activity_game.class);
        myIntent.putExtra(BUNDLE, bundle);
        startActivity(myIntent);
    }


}