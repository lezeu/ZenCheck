package com.example.phoneapp.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phoneapp.BaseActivity;
import com.example.phoneapp.R;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);
    }
}
