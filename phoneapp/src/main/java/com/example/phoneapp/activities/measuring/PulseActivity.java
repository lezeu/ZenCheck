package com.example.phoneapp.activities.measuring;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.api.measurements.BpmApi;
import com.example.phoneapp.api.measurements.MyCallback;
import com.example.phoneapp.dtos.bpm.BpmDto;
import com.example.phoneapp.exceptions.ZenCheckException;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
            dataPointsAverage[i] = new DataPoint(i, bpmDtos.get(i) == null ? 0 : bpmDtos.get(i).getBpmAverage());
        }

        LineGraphSeries<DataPoint> seriesAverage = new LineGraphSeries<>(dataPointsAverage);

        graphViewAverage.addSeries(seriesAverage);
        customizeGraph(graphViewAverage, seriesAverage);
    }

    private void customizeGraph(GraphView graphView, LineGraphSeries<DataPoint> series) {
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(4);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(150);

        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScrollableY(false);

        series.setColor(Color.RED);
        series.setThickness(8);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
    }
}