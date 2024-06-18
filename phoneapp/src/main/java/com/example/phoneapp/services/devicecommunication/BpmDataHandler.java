package com.example.phoneapp.services.devicecommunication;

import android.util.Log;

import com.example.phoneapp.api.bpm.BpmApi;
import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.dtos.bpm.BpmDto;
import com.example.phoneapp.utils.Constants;
import com.example.phoneapp.utils.ZenCheckException;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMapItem;

public class BpmDataHandler {

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
                    Log.d(Constants.TAG, String.valueOf(result));
                }

                @Override
                public void onFailure(ZenCheckException exception) {
                    throw new ZenCheckException(exception.getMessage());
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

            Log.d(Constants.TAG, String.format("BPM Average: %f%ntimestamp: %d", bpm, timestamp));
        }
    }
}
