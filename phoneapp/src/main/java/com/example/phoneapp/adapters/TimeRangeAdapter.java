package com.example.phoneapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;

import java.util.List;

public class TimeRangeAdapter extends RecyclerView.Adapter<TimeRangeAdapter.TimeRangeViewHolder> {

    private List<String> timeRanges;
    private OnTimeRangeSelectedListener listener;
    private int selectedPosition = -1; // to keep track of selected position

    public interface OnTimeRangeSelectedListener {
        void onTimeRangeSelected(String timeRange);
    }

    public TimeRangeAdapter(List<String> timeRanges, OnTimeRangeSelectedListener listener) {
        this.timeRanges = timeRanges;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeRangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_range, parent, false);
        return new TimeRangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeRangeViewHolder holder, int position) {
        String timeRange = timeRanges.get(position);
        holder.timeRangeTextView.setText(timeRange);
        holder.itemView.setSelected(selectedPosition == position); // highlight selected item
        holder.itemView.setOnClickListener(v -> {
            listener.onTimeRangeSelected(timeRange);
            notifyItemChanged(selectedPosition); // update previously selected item
            selectedPosition = holder.getAdapterPosition(); // set new position
            notifyItemChanged(selectedPosition); // update new selected item
        });
    }

    @Override
    public int getItemCount() {
        return timeRanges.size();
    }

    static class TimeRangeViewHolder extends RecyclerView.ViewHolder {
        TextView timeRangeTextView;

        TimeRangeViewHolder(View itemView) {
            super(itemView);
            timeRangeTextView = itemView.findViewById(R.id.time_range_text);
        }
    }
}

