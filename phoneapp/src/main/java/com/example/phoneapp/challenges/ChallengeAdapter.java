package com.example.phoneapp.challenges;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {

    private List<Challenge> challengeList;

    public ChallengeAdapter(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_challenge, parent, false);
        return new ChallengeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChallengeViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);

        holder.title.setText(challenge.getTitle());
        holder.difficulty.setText(challenge.getDifficulty());
        holder.progress.setProgress(challenge.getProgress());
        holder.image.setImageResource(challenge.getImage());
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    public static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView difficulty;
        public ProgressBar progress;
        public ImageView image;

        public ChallengeViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.challenge_title);
            difficulty = view.findViewById(R.id.challenge_difficulty);
            progress = view.findViewById(R.id.challenge_progress);
            image = view.findViewById(R.id.challenge_image);
        }
    }
}
