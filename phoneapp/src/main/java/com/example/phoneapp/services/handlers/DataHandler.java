package com.example.phoneapp.services.handlers;

import com.google.android.gms.wearable.DataEvent;

public interface DataHandler {
    void handleData(DataEvent event);
}
