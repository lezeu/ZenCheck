package com.example.phoneapp.services.devicecommunication;

import com.google.android.gms.wearable.DataEvent;

public interface DataHandler {
    void handleData(DataEvent event);
}
