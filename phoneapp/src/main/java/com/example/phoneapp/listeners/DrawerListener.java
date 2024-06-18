package com.example.phoneapp.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class DrawerListener extends GestureDetector.SimpleOnGestureListener {

    private final DrawerLayout drawerLayout;

    public DrawerListener(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null) throw new IllegalArgumentException("drawer action failed");

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
