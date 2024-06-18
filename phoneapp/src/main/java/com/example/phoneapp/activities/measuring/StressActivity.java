package com.example.phoneapp.activities.measuring;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.adapters.graphs.CustomLineGraphSeries;
import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.api.stress.StressApi;
import com.example.phoneapp.dtos.pulse.StressDto;
import com.example.phoneapp.utils.ZenCheckException;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.time.LocalDateTime;
import java.util.List;

public class StressActivity extends BaseActivity {
    private GraphView stressGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stress);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);
        stressGraphView = findViewById(R.id.stressGraph);

        StressApi.INSTANCE.getDailyStress(new MyCallback<>() {
            @Override
            public void onSuccess(List<StressDto> result) {
                displayGraph(result);
            }

            @Override
            public void onFailure(ZenCheckException exception) {
                throw new ZenCheckException(exception.getMessage());
            }
        });
    }

    private void displayGraph(List<StressDto> stressDtos) {
        DataPoint[] points = new DataPoint[stressDtos.size()];

        for (int i = 0; i < stressDtos.size(); i++) {
            long timestamp = stressDtos.get(i).getTimestamp();
            double hoursAgo = (System.currentTimeMillis() - timestamp) / (1000.0 * 60 * 60);
            points[i] = new DataPoint(24 - hoursAgo, stressDtos.get(i).getSdnn());
        }

        double colorThreshold = 50;
        CustomLineGraphSeries series = new CustomLineGraphSeries(points, colorThreshold);
        stressGraphView.addSeries(series);
        customizeGraph(series);
    }

    @SuppressLint("SimpleDateFormat")
    private void customizeGraph(CustomLineGraphSeries series) {
        stressGraphView.getViewport().setXAxisBoundsManual(true);
        stressGraphView.getViewport().setMinX(20);
        stressGraphView.getViewport().setMaxX(24);

        stressGraphView.getViewport().setYAxisBoundsManual(true);
        stressGraphView.getViewport().setMinY(0);
        stressGraphView.getViewport().setMaxY(150);

        stressGraphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    int currentHour = LocalDateTime.now().getHour();
                    int hour = (int) ((value + currentHour) % 24);
                    if (hour < 0) {
                        hour += 24;
                    }
                    return String.format("%02d:00", hour);
                }
                return super.formatLabel(value, false);
            }
        });

        stressGraphView.getViewport().setScrollable(true);
        stressGraphView.getViewport().setScalable(true);
        stressGraphView.getViewport().setScrollableY(false);

        series.setColor(Color.BLUE);
        series.setThickness(4);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
    }
}