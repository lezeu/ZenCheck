package com.example.phoneapp.activities.challenges;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.adapters.ChallengeAdapter;
import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.api.challenge.ChallengeApi;
import com.example.phoneapp.dtos.challenges.ChallengeDto;
import com.example.phoneapp.dtos.challenges.Progress;
import com.example.phoneapp.utils.ZenCheckException;

import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends BaseActivity {
    List<Progress> challengeList;
    ChallengeAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        setupDrawer(R.id.drawer_layout, R.id.navigation_view);

        recyclerView = findViewById(R.id.recycler_view_challenges);
        initializeView();
    }

    private void initializeView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        challengeList = new ArrayList<>();
        ChallengeApi.INSTANCE.getChallenges(new MyCallback<>() {
            @Override
            public void onSuccess(List<ChallengeDto> result) {
                for (ChallengeDto challengeDto : result) {
                    challengeList.add(new Progress(
                            challengeDto.getTitle(),
                            challengeDto.getDescription(),
                            challengeDto.getPoints(),
                            challengeDto.isStreak(),
                            getIconResource(challengeDto.getTitle()),
                            challengeDto.isStatus(),
                            challengeDto.getDuration()));

                    adapter = new ChallengeAdapter(challengeList, ChallengeActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(ZenCheckException exception) {
                throw new ZenCheckException(exception.getMessage());
            }
        });
    }

   private int getIconResource(String title) {
        switch (title.toUpperCase()) {
            case "RUN":
                return R.drawable.run;
            case "SHOWER":
                return R.drawable.shower;
            case "MEDITATE":
                return R.drawable.meditate;
            default:
                return R.drawable.baseline_question_mark_24;
        }
   }
}