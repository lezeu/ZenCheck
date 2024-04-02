package com.example.phoneapp.challenges;

import lombok.Getter;

@Getter
public class Challenge {
    private String title;
    private String difficulty;
    private int progress;
    private int image;

    public Challenge(String title, String difficulty, int progress, int image) {
        this.title = title;
        this.difficulty = difficulty;
        this.progress = progress;
        this.image = image;
    }
}
