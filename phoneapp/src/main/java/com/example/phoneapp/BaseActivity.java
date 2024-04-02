package com.example.phoneapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.phoneapp.challenges.ChallengeActivity;
import com.example.phoneapp.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private GestureDetector gestureDetector;
    private ScrollView scrollView;

    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    protected void setupDrawer(int drawerLayoutId, int navigationViewId) {
        drawerLayout = findViewById(drawerLayoutId);
        navigationView = findViewById(navigationViewId);
        scrollView = findViewById(R.id.scrollView);

        gestureDetector = new GestureDetector(this, new MyGestureListener());

        scrollView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                startActivity(new Intent(BaseActivity.this, MainActivity.class));
            } else if (id == R.id.nav_stats) {

            } else if (id == R.id.nav_challenges) {
                startActivity(new Intent(BaseActivity.this, ChallengeActivity.class));
            } else if (id == R.id.nav_friends) {

            } else if (id == R.id.nav_notifications) {

            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
            } else if (id == R.id.nav_about) {

            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
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
