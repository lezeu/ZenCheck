package com.example.phoneapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.challenges.ChallengeActivity;
import com.example.phoneapp.activities.profile.ProfileActivity;
import com.example.phoneapp.listeners.DrawerListener;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {


    private GestureDetector gestureDetector;

    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    protected void setupDrawer(int drawerLayoutId, int navigationViewId) {
        DrawerLayout drawerLayout;
        drawerLayout = findViewById(drawerLayoutId);
        NavigationView navigationView = findViewById(navigationViewId);
        ScrollView scrollView = findViewById(R.id.scrollView);

        gestureDetector = new GestureDetector(this, new DrawerListener(drawerLayout));

        scrollView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                startActivity(new Intent(BaseActivity.this, MainActivity.class));
            } else if (id == R.id.nav_stats) {
// TODO
            } else if (id == R.id.nav_challenges) {
                startActivity(new Intent(BaseActivity.this, ChallengeActivity.class));
            } else if (id == R.id.nav_friends) {
// TODO
            } else if (id == R.id.nav_notifications) {
// TODO
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
            } else if (id == R.id.nav_about) {
// TODO
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
}
