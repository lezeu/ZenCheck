package com.example.phoneapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.challenges.ChallengeTimerActivity;
import com.example.phoneapp.dtos.challenges.Progress;

import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {
    private List<Progress> challengeList;
    private Context context;

    public ChallengeAdapter(List<Progress> challengeList, Context context) {
        this.challengeList = challengeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_challenge, parent, false);
        return new ChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Progress progress = challengeList.get(position);
        holder.bind(progress);

        holder.itemView.findViewById(R.id.start_challenge).setOnClickListener(v -> {
            Intent intent = new Intent(context, ChallengeTimerActivity.class);
            intent.putExtra("title", progress.title());
            intent.putExtra("points", progress.points());
            intent.putExtra("duration", progress.duration());
            intent.putExtra("streak", progress.streak());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    public static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        private TextView challengeName;
        private TextView challengePoints;
        private ImageView challengeImage;

        public ChallengeViewHolder(View itemView) {
            super(itemView);
            challengeName = itemView.findViewById(R.id.challenge_title);
            challengePoints = itemView.findViewById(R.id.progress_points);
            challengeImage = itemView.findViewById(R.id.challenge_image);
        }

        public void bind(Progress progress) {
            challengeName.setText(progress.title());
            challengePoints.setText(String.valueOf(progress.points()));
            challengeImage.setImageResource(progress.image());
        }
    }
}
