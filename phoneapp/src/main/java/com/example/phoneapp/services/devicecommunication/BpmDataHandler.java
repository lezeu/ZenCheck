package com.example.phoneapp.services.devicecommunication;

import android.util.Log;

import com.example.phoneapp.api.measurements.BpmApi;
import com.example.phoneapp.api.measurements.MyCallback;
import com.example.phoneapp.dtos.bpm.BpmDto;
import com.example.phoneapp.exceptions.ZenCheckException;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMapItem;

public class BpmDataHandler {
    private static final String TAG = "ManualDebug";

    private BpmDataHandler() {
        throw new ZenCheckException("Utility class");
    }

    public static class ScheduledDataHandler implements DataHandler {
        @Override
        public void handleData(DataEvent event) {
            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            float bpmAverage = dataMapItem.getDataMap().getFloat("bpmAverage");
            float bpmHigh = dataMapItem.getDataMap().getFloat("bpmHigh");
            float bpmLow = dataMapItem.getDataMap().getFloat("bpmLow");
            long timestamp = dataMapItem.getDataMap().getLong("timestamp");

            BpmDto bpmDto = BpmDto.builder()
                    .bpmAverage(bpmAverage)
                    .bpmHigh(bpmHigh)
                    .bpmLow(bpmLow)
                    .timestamp(timestamp).build();

            BpmApi.INSTANCE.sendBpm(bpmDto, new MyCallback<>() {
                @Override
                public void onSuccess(BpmDto result) {
                    Log.d(TAG, String.format("BPM Average: %f%nBPM High: %f%nBPM Low: %f%n at: %d",
                            bpmAverage, bpmHigh, bpmLow, timestamp));
                }

                @Override
                public void onFailure(ZenCheckException exception) {
                    Log.d(TAG, exception.toString());
                }
            });
        }
    }

    public static class DemandDataHandler implements DataHandler {
        @Override
        public void handleData(DataEvent event) {
            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            float bpm = dataMapItem.getDataMap().getFloat("bpm");
            long timestamp = dataMapItem.getDataMap().getLong("timestamp");

            Log.d(TAG, String.format("BPM Average: %f%ntimestamp: %d", bpm, timestamp));
        }
    }
}
