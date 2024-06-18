package com.example.phoneapp.activities.measuring;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.adapters.graphs.CustomLineGraphSeries;
import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.api.bpm.BpmApi;
import com.example.phoneapp.dtos.bpm.BpmDto;
import com.example.phoneapp.utils.ZenCheckException;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.time.LocalDateTime;
import java.util.List;

public class PulseActivity extends BaseActivity {

    private GraphView graphViewAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pulse);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);
        graphViewAverage = findViewById(R.id.averagePulseGraph);

        BpmApi.INSTANCE.getDailyBpmValues(new MyCallback<>() {
            @Override
            public void onSuccess(List<BpmDto> result) {
                displayGraph(result);
            }

            @Override
            public void onFailure(ZenCheckException exception) {
                throw new ZenCheckException(exception.toString());
            }
        });
    }

    private void displayGraph(List<BpmDto> bpmDtos) {
        DataPoint[] dataPointsAverage = new DataPoint[bpmDtos.size()];

        for (int i = 0; i < bpmDtos.size(); i++) {
            long timestamp = bpmDtos.get(i).getTimestamp();
            double hoursAgo = (System.currentTimeMillis() - timestamp) / (1000.0 * 60 * 60);
            dataPointsAverage[i] = new DataPoint(24 - hoursAgo, bpmDtos.get(i).getBpmAverage());

        }

        CustomLineGraphSeries seriesAverage = new CustomLineGraphSeries(dataPointsAverage, 120);

        graphViewAverage.addSeries(seriesAverage);
        customizeGraph(seriesAverage);
    }

    @SuppressLint("SimpleDateFormat")
    private void customizeGraph(CustomLineGraphSeries series) {
        graphViewAverage.getViewport().setXAxisBoundsManual(true);
        graphViewAverage.getViewport().setMinX(20);
        graphViewAverage.getViewport().setMaxX(24);

        graphViewAverage.getViewport().setYAxisBoundsManual(true);
        graphViewAverage.getViewport().setMinY(0);
        graphViewAverage.getViewport().setMaxY(150);

        graphViewAverage.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
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

        graphViewAverage.getViewport().setScrollable(true);
        graphViewAverage.getViewport().setScalable(true);
        graphViewAverage.getViewport().setScrollableY(false);

        series.setColor(Color.BLUE);
        series.setThickness(4);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
    }
}