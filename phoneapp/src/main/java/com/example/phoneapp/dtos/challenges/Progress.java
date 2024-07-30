package com.example.phoneapp.dtos.challenges;

import java.time.Duration;

public record Progress(String title, String description, long points, boolean streak, int image, boolean status, Duration duration) {
}
