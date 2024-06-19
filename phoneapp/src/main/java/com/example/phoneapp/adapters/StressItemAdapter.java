package com.example.phoneapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;

import java.util.List;

public class StressItemAdapter extends RecyclerView.Adapter<StressItemAdapter.StringItemViewHolder> {

    private List<String> stringItems;
    private OnStringItemSelectedListener listener;
    private int selectedPosition = -1;

    public interface OnStringItemSelectedListener {
        void onStringItemSelected(String stringItem);
    }

    public StressItemAdapter(List<String> stringItems, OnStringItemSelectedListener listener) {
        this.stringItems = stringItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StringItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stress_range, parent, false);
        return new StringItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StringItemViewHolder holder, int position) {
        String stringItem = stringItems.get(position);
        holder.stringItemTextView.setText(stringItem);
        holder.itemView.setSelected(selectedPosition == position); // highlight selected item
        holder.itemView.setOnClickListener(v -> {
            listener.onStringItemSelected(stringItem);
            notifyItemChanged(selectedPosition);
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return stringItems.size();
    }

    static class StringItemViewHolder extends RecyclerView.ViewHolder {
        TextView stringItemTextView;

        StringItemViewHolder(View itemView) {
            super(itemView);
            stringItemTextView = itemView.findViewById(R.id.string_item_text);
        }
    }
}

