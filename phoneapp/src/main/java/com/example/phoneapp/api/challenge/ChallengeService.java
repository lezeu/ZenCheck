package com.example.phoneapp.api.challenge;

import com.example.phoneapp.dtos.challenges.ChallengeDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

public interface ChallengeService {

    @GET("/api/challenges")
    Call<List<ChallengeDto>> getChallenges();

    @PATCH("/api/challenges/update")
    Call<ChallengeDto> updateChallenge(@Body ChallengeDto challengeDto);
}
