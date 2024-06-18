package com.example.watchapp.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDto {
    private String name;
    private String email;
    private String sex;
    private float hrThreshold;
    private float sdnnThreshold;
    private float rmssdThreshold;
}
