package com.example.phoneapp.api.challenge;

import com.example.phoneapp.api.ApiClient;
import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.dtos.challenges.ChallengeDto;
import com.example.phoneapp.utils.ZenCheckException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public enum ChallengeApi {
    INSTANCE;
    private final ChallengeService service;

    ChallengeApi() {
        service = new ApiClient<>(ChallengeService.class).createService();
    }

    public void getChallenges(MyCallback<List<ChallengeDto>> challengesCallback) {
        Call<List<ChallengeDto>> call = service.getChallenges();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ChallengeDto>> call, Response<List<ChallengeDto>> response) {
                if (response.isSuccessful()) {
                    challengesCallback.onSuccess(response.body());
                } else {
                    challengesCallback.onFailure(new ZenCheckException(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<ChallengeDto>> call, Throwable t) {
                challengesCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }

    public void updateChallenges(ChallengeDto challengeDto, MyCallback<ChallengeDto> challengesCallback) {
        Call<ChallengeDto> call = service.updateChallenge(challengeDto);

        call.enqueue(new Callback <>() {
            @Override
            public void onResponse(Call<ChallengeDto> call, Response<ChallengeDto> response) {
                if (response.isSuccessful()) {
                    challengesCallback.onSuccess(response.body());
                } else {
                    challengesCallback.onFailure(new ZenCheckException(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ChallengeDto> call, Throwable t) {
                challengesCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }
}
