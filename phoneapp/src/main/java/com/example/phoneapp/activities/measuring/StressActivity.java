package com.example.phoneapp.activities.measuring;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.adapters.CustomLineGraphSeries;
import com.example.phoneapp.adapters.StressItemAdapter;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class StressActivity extends BaseActivity {
    private GraphView stressGraphView;
    private TextView timeRangeDisplay;
    private TextView stressRangeDisplay;
    private LinearLayout timeAndStressLayout;
    private String selectedTimeRange;
    private String selectedStressRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stress);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);
        stressGraphView = findViewById(R.id.stressGraph);
        timeRangeDisplay = findViewById(R.id.time_range_display);
        stressRangeDisplay = findViewById(R.id.stress_range_display);
        timeAndStressLayout = findViewById(R.id.time_and_stress_ranges);

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

        Button showTimeButton = findViewById(R.id.show_time_button);
        showTimeButton.setOnClickListener(v -> showTimeRangePickerDialog());
        Button showStressButton = findViewById(R.id.show_stress_button);
        showStressButton.setOnClickListener(v -> showStressRangePickerDialog());
        Button addEventButton = findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(v -> {
            timeAndStressLayout.setVisibility(View.VISIBLE);
            addEventButton.setVisibility(View.INVISIBLE);
        });
        Button saveChangesButton = findViewById(R.id.save_changes);
        saveChangesButton.setOnClickListener(v -> {
            // calculate new Thresholds
            // save them to profile
            timeAndStressLayout.setVisibility(View.INVISIBLE);
            addEventButton.setVisibility(View.VISIBLE);
            timeRangeDisplay.setText(R.string.select_time_range);
            stressRangeDisplay.setText(R.string.actual_stress_level);
        });
        Button discardChangesButton = findViewById(R.id.discard_changes);
        discardChangesButton.setOnClickListener(v -> {
            timeAndStressLayout.setVisibility(View.INVISIBLE);
            addEventButton.setVisibility(View.VISIBLE);
            timeRangeDisplay.setText(R.string.select_time_range);
            stressRangeDisplay.setText(R.string.actual_stress_level);
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


    private void showTimeRangePickerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_time_range_picker);

        RecyclerView timeIntervalsRecyclerView =
                dialog.findViewById(R.id.time_intervals_recycler_view);
        timeIntervalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> timeRanges = generateTimeRanges();
        TimeRangeAdapter adapter = new TimeRangeAdapter(
                timeRanges, timeRange -> {
                    selectedTimeRange = timeRange;
                    timeRangeDisplay.setText(selectedTimeRange);
                    dialog.dismiss();
                });

        timeIntervalsRecyclerView.setAdapter(adapter);
        dialog.show();
    }


    private void showStressRangePickerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_stress_range_picker);

        RecyclerView stringItemsRecyclerView = dialog.findViewById(R.id.string_items_recycler_view);
        stringItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> stringItems = Arrays.asList(
                "Very High Stress", "High Stress", "Low Stress", "No Stress");
        StressItemAdapter adapter = new StressItemAdapter(
                stringItems, stringItem -> {
                    selectedStressRange = stringItem;
                    stressRangeDisplay.setText(selectedStressRange);
                    dialog.dismiss();
        });

        stringItemsRecyclerView.setAdapter(adapter);
        dialog.show();
    }

    private List<String> generateTimeRanges() {
        List<String> timeRanges = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        for (int i = 0; i < 24; i++) {
            int startHour = (currentHour - i + 24) % 24;
            int endHour = (startHour + 1) % 24;
            @SuppressLint("DefaultLocale") String timeRange =
                    String.format("%02d:00 - %02d:00", startHour, endHour);
            timeRanges.add(timeRange);
        }

        return timeRanges;
    }
}