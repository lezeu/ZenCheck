package com.example.phoneapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.dtos.challenges.Progress;

import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {

    private final List<Progress> challengeList;

    public ChallengeAdapter(List<Progress> challengeList) {
        this.challengeList = challengeList;
    }

    @NonNull
    @Override
    public ChallengeAdapter.ChallengeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_challenge, parent, false);
        return new ChallengeAdapter.ChallengeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChallengeAdapter.ChallengeViewHolder holder, int position) {
        Progress challenge = challengeList.get(position);

        holder.title.setText(challenge.title());
        holder.points.setText(challenge.points());
        holder.image.setImageResource(challenge.image());
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    public static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView points;
        private final ImageView image;

        public ChallengeViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.challenge_title);
            points = view.findViewById(R.id.progress_points);
            image = view.findViewById(R.id.challenge_image);
        }
    }
}
