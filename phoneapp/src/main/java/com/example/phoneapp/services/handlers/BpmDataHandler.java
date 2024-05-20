package com.example.phoneapp.services.handlers;

import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMapItem;

public class BpmDataHandler implements DataHandler{
    private static final String TAG = "ManualDebug";

    @Override
    public void handleData(DataEvent event) {
        DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
        float bpm = dataMapItem.getDataMap().getFloat("bpm");
        long timestamp = dataMapItem.getDataMap().getLong("timestamp");

        Log.d(TAG, "Received BPM: " + bpm + " at " + timestamp);
    }
}
