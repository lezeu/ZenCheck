package com.example.phoneapp.activities.measuring;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.adapters.CustomLineGraphSeries;
import com.example.phoneapp.adapters.TimeRangeAdapter;
import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.api.stress.StressApi;
import com.example.phoneapp.dtos.pulse.StressDto;
import com.example.phoneapp.utils.ZenCheckException;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StressActivity extends BaseActivity {
    private GraphView stressGraphView;
    private TextView timeRangeDisplay;
    private String selectedTimeRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stress);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);
        stressGraphView = findViewById(R.id.stressGraph);
        timeRangeDisplay = findViewById(R.id.time_range_display);

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

        Button showDialogButton = findViewById(R.id.show_dialog_button);
        showDialogButton.setOnClickListener(v -> showTimeRangePickerDialog());
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


    private void showTimeRangePickerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_time_range_picker);

        RecyclerView timeIntervalsRecyclerView = dialog.findViewById(R.id.time_intervals_recycler_view);
        timeIntervalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> timeRanges = generateTimeRanges();
        TimeRangeAdapter adapter = new TimeRangeAdapter(timeRanges, new TimeRangeAdapter.OnTimeRangeSelectedListener() {
            @Override
            public void onTimeRangeSelected(String timeRange) {
                selectedTimeRange = timeRange;
            }
        });

        timeIntervalsRecyclerView.setAdapter(adapter);

        Button confirmButton = dialog.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            if (selectedTimeRange != null) {
                timeRangeDisplay.setText(selectedTimeRange);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private List<String> generateTimeRanges() {
        List<String> timeRanges = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        for (int i = 0; i < 24; i++) {
            int startHour = (currentHour - i + 24) % 24;
            int endHour = (startHour + 1) % 24;
            String timeRange = String.format("%02d:00 - %02d:00", startHour, endHour);
            timeRanges.add(timeRange);
        }

        return timeRanges;
    }
}