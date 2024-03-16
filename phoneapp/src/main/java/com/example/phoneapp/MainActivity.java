package com.example.phoneapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private GestureDetector gestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.design_navigation_view);

        gestureDetector = new GestureDetector(this, new MyGestureListener());

        ScrollView scrollView = findViewById(R.id.scroll_view);
        scrollView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
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
