package com.example.phoneapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.phoneapp.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    private static final int NAV_DASHBOARD = 2131231003;
    private static final int NAV_STATS = 2131231003;
    private static final int NAV_FRIENDS = 2131231002;
    private static final int NAV_NOTIFICATIONS = 2131231005;
    private static final int NAV_PROFILE = 2131231006;
    private static final int NAV_ABOUT = 2131231002;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private GestureDetector gestureDetector;
    private ScrollView scrollView;

    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    protected void setupDrawer(int drawerLayoutId, int navigationViewId) {
        drawerLayout = findViewById(drawerLayoutId);
        navigationView = findViewById(navigationViewId);
        scrollView = findViewById(R.id.scroll_view);

        gestureDetector = new GestureDetector(this, new MyGestureListener());

        scrollView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case NAV_DASHBOARD ->
                    startActivity(new Intent(BaseActivity.this, MainActivity.class));
//                case NAV_STATS ->
//                    startActivity(new Intent(BaseActivity.this, StatsActivity.class));
//                case NAV_FRIENDS ->
//                    startActivity(new Intent(BaseActivity.this, FriendsActivity.class));
//                case NAV_NOTIFICATIONS ->
//                    startActivity(new Intent(BaseActivity.this, NotificationsActivity.class));
                case NAV_PROFILE ->
                    startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
//                case NAV_ABOUT ->
//                    startActivity(new Intent(BaseActivity.this, AboutActivity.class));
                default -> System.out.println(item.getItemId());
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
