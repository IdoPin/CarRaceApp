package com.example.carrace;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecordsDB {

    private ArrayList<Record> records;
    private int maxSize;

    public RecordsDB() {
        records = new ArrayList<Record>();
        this.maxSize = 10;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public RecordsDB setRecords(ArrayList<Record> records) {
        this.records = records;
        return this;
    }

    public void addRecord(Record rec){
        this.records.add(rec);
        Collections.sort(this.records, new Sort());
        if(this.records.size()>this.maxSize){
            this.records.remove(this.maxSize);
        }
    }
}


class Sort implements Comparator<Record> {

    @Override
    public int compare(Record rec1, Record rec2) {

        return rec2.getScore() - rec1.getScore();
    }
}