package com.example.carrace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Fragment_List extends Fragment {


    private CallBack_List callBackList;
    private MaterialButton[] btns_records;

    public void setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        String js = MSPV3.getMe().getString("DB", "");
        RecordsDB database = new Gson().fromJson(js, RecordsDB.class);
        ArrayList<Record> records = database.getRecords();
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            btns_records[i].setText(i + 1 + ": " + record.getName() + " --> " + record.getScore());
            btns_records[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBackList.rowSelected(record.getName(),record.getScore(),record.getLatitude(),record.getLongitude());
                }
            });

        }
    }

    private void findViews(View view) {
        btns_records = new MaterialButton[]{
                view.findViewById(R.id.record_1),
                view.findViewById(R.id.record_2),
                view.findViewById(R.id.record_3),
                view.findViewById(R.id.record_4),
                view.findViewById(R.id.record_5),
                view.findViewById(R.id.record_6),
                view.findViewById(R.id.record_7),
                view.findViewById(R.id.record_8),
                view.findViewById(R.id.record_9),
                view.findViewById(R.id.record_10),

        };
    }
}
