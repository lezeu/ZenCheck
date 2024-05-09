package com.example.phoneapp;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer(R.id.ConstraintLayout, R.id.design_navigation_view);
    }
}
