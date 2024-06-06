package com.example.phoneapp.activities.measuring;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.adapters.graphs.CustomLineGraphSeries;
import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.api.stress.StressApi;
import com.example.phoneapp.dtos.pulse.StressDto;
import com.example.phoneapp.exceptions.ZenCheckException;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Comparator;
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

        stressDtos.sort(Comparator.comparingLong(StressDto::getTimestamp));

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

    private void customizeGraph(CustomLineGraphSeries series) {
        stressGraphView.getViewport().setXAxisBoundsManual(true);
        stressGraphView.getViewport().setMinX(0);
        stressGraphView.getViewport().setMaxX(4);

        stressGraphView.getViewport().setYAxisBoundsManual(true);
        stressGraphView.getViewport().setMinY(0);
        stressGraphView.getViewport().setMaxY(200);

        stressGraphView.getViewport().setScrollable(true);
        stressGraphView.getViewport().setScrollableY(false);

        series.setColor(Color.GRAY);
        series.setThickness(4);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
    }
}