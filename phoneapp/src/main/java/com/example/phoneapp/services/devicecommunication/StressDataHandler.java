package com.example.phoneapp.services.devicecommunication;

import android.util.Log;

import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.api.stress.StressApi;
import com.example.phoneapp.dtos.pulse.StressDto;
import com.example.phoneapp.exceptions.ZenCheckException;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMapItem;

public class StressDataHandler {
    private static final String TAG = "ManualDebug";

    private StressDataHandler() {
        throw new ZenCheckException("Utility class");
    }

    public static class ScheduledDataHandler implements DataHandler {
        @Override
        public void handleData(DataEvent event) {
            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            String stressLevel = dataMapItem.getDataMap().getString("stressLevel");
            float hr = dataMapItem.getDataMap().getFloat("HR");
            float sdnn = dataMapItem.getDataMap().getFloat("SDNN");
            float rmssd = dataMapItem.getDataMap().getFloat("RMSSD");
            long timestamp = dataMapItem.getDataMap().getLong("timestamp");

            StressDto stressDto = StressDto.builder()
                    .stressLevel(stressLevel)
                    .hr(hr)
                    .sdnn(sdnn)
                    .rmssd(rmssd)
                    .timestamp(timestamp).build();

            StressApi.INSTANCE.sendStress(stressDto, new MyCallback<>() {
                @Override
                public void onSuccess(StressDto result) {
                    Log.d(TAG, String.valueOf(result));
                }

                @Override
                public void onFailure(ZenCheckException exception) {
                    throw new ZenCheckException(exception.getMessage());
                }
            });
        }
    }
}
