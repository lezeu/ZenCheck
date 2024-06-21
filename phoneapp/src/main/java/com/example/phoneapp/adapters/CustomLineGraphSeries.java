package com.example.phoneapp.adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Iterator;

public class CustomLineGraphSeries extends LineGraphSeries<DataPoint> {
    private double max;
    private double min;

    public CustomLineGraphSeries(DataPoint[] data, double max, double min) {
        super(data);
        this.max = max;
        this.min = min;
    }

    @Override
    public void draw(GraphView graphView, Canvas canvas, boolean isSecondScale) {
        super.draw(graphView, canvas, isSecondScale);

        // Get the viewport bounds
        double maxX = graphView.getViewport().getMaxX(false);
        double minX = graphView.getViewport().getMinX(false);
        double maxY = graphView.getViewport().getMaxY(false);
        double minY = graphView.getViewport().getMinY(false);

        // Iterate through data points
        Iterator<DataPoint> values = getValues(minX, maxX);
        while (values.hasNext()) {
            DataPointInterface point = values.next();

            // Calculate the pixel coordinates for the point
            double x = graphView.getGraphContentLeft() + (point.getX() - minX) / (maxX - minX) * graphView.getGraphContentWidth();
            double y = graphView.getGraphContentTop() + (maxY - point.getY()) / (maxY - minY) * graphView.getGraphContentHeight();

            Paint paint = new Paint();
            paint.setColor(point.getY() > max ? Color.RED : point.getY() < min ? Color.RED : Color.GREEN);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle((float) x, (float) y, 10, paint);
        }
    }
}
