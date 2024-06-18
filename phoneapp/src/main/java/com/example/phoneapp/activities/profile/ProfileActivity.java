package com.example.phoneapp.activities.profile;

import android.os.Bundle;

import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.R;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);
    }
}
