package com.example.phoneapp.dtos.challenges;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDto {
    private Long id;
    private String title;
    private String description;
    private long points;
    private boolean streak;
    private boolean status;
    private Duration duration;
}
