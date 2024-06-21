package com.example.phoneapp.utils;

public class Constants {

    private Constants() {
        throw new ZenCheckException("Utility class");
    }
    public static final String TAG = "ManualDebug";
    public static final String CHANNEL_ID = "ZenCheck";
    public static final String VERY_HIGH_STRESS = "Highly Stressed";
    public static final String HIGH_STRESS = "Stressed";
    public static final String LOW_STRESS = "Slightly Stressed";
    public static final String VERY_LOW_STRESS = "Not Stressed";
}
