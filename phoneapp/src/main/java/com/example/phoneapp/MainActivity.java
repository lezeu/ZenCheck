package com.example.phoneapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.phoneapp.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private GestureDetector gestureDetector;

    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.design_navigation_view);
        gestureDetector = new GestureDetector(this, new MyGestureListener());

        ScrollView scrollView = findViewById(R.id.scroll_view);
        scrollView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.profile_name_picture_layout -> startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                case R.id.nav_dashboard -> startActivity(new Intent(MainActivity.this, DashboardActivity.class));
//                case R.id.nav_stats -> startActivity(new Intent(MainActivity.this, StatsActivity.class));
//                case R.id.nav_friends -> startActivity(new Intent(MainActivity.this, FriendsActivity.class));
//                case R.id.nav_notifications -> startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                case R.id.nav_profile -> startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//                case R.id.nav_about -> startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
            return false;
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            assert e1 != null;
            final float distanceX = e2.getX() - e1.getX();
            final float distanceY = e2.getY() - e1.getY();

            if (Math.abs(distanceX) > Math.abs(distanceY) &&
                    Math.abs(distanceX) > 100 && Math.abs(velocityX) > 100) {
                if (distanceX > 0) {
                    // Right swipe detected, open the drawer
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            }
            return false;
        }
    }
}
