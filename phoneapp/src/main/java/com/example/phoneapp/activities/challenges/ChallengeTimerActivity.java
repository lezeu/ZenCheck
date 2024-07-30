package com.example.phoneapp.activities.challenges;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phoneapp.R;

import java.time.Duration;

public class ChallengeTimerActivity extends AppCompatActivity {
    private TextView timerTextView;
    private Duration challengeDuration;
    private String challengeTitle;
    private long challengePoints;
    private boolean streak;
    private Button cancelButton;
    private Button viewProgressButton;
    private Button startChallengeButton;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_timer);

        timerTextView = findViewById(R.id.timerTextView);
        cancelButton = findViewById(R.id.cancel_timer_button);
        viewProgressButton = findViewById(R.id.view_progress_button);
        progressBar = findViewById(R.id.challenge_progress_bar);
        startChallengeButton = findViewById(R.id.start_challenge_button);

        Intent intent = getIntent();
        challengeDuration = Duration.ofSeconds(intent.getLongExtra("duration", 30));
        challengeTitle = intent.getStringExtra("title");
        challengePoints = intent.getLongExtra("points", 1);
        streak = intent.getBooleanExtra("streak", false);
        challengePoints = streak ? (long) (challengePoints * 1.5) : challengePoints;

        startChallengeButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
        viewProgressButton.setVisibility(View.INVISIBLE);

        cancelButton.setOnClickListener(v -> {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
            finish();
        });

        startChallengeButton.setOnClickListener(v -> {
            startChallengeButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            timerTextView.setVisibility(View.VISIBLE);
            // make watch to start measuring
            startTimer();
        });

        viewProgressButton.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ChallengeTimerActivity.this, ProgressActivity.class));
        });
    }

    private void startTimer() {
        progressBar.setMax(100); // Set max progress to 100
        countDownTimer = new CountDownTimer(challengeDuration.toMillis(), 1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("remaining: " + millisUntilFinished / 1000 + "s");
                int progress = (int) ((challengeDuration.toMillis() - millisUntilFinished) * 100 / challengeDuration.toMillis());
                progressBar.setProgress(progress);
            }

            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                progressBar.setProgress(100);
                cancelButton.setVisibility(View.GONE);
                viewProgressButton.setVisibility(View.VISIBLE);
                // retrieve data from watch and validate if the exercise was made
                // TODO Save challenge progress to database
            }
        }.start();
    }
}
