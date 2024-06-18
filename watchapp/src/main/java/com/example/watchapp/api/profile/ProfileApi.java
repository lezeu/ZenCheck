package com.example.watchapp.api.profile;

import com.example.watchapp.api.ApiClient;
import com.example.watchapp.api.MyCallback;
import com.example.watchapp.dtos.ProfileDto;
import com.example.watchapp.utils.ZenCheckException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public enum ProfileApi {
    INSTANCE;
    private final ProfileService service;

    ProfileApi() {
        service = new ApiClient<>(ProfileService.class).createService();
    }

    public void getProfile(MyCallback<ProfileDto> profileCallback) {
        Call<ProfileDto> call = service.getProfile();

        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ProfileDto> call, Response<ProfileDto> response) {
                if (response.isSuccessful()) {
                    profileCallback.onSuccess(response.body());
                } else {
                    profileCallback.onFailure(new ZenCheckException(
                            "Error occurred when trying to get profile: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<ProfileDto> call, Throwable t) {
                profileCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }

    public void updateProfile(ProfileDto profileDto, MyCallback<ProfileDto> profileCallback) {
        Call<ProfileDto> call = service.updateProfile(profileDto);

        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ProfileDto> call, Response<ProfileDto> response) {
                if (response.isSuccessful()) {
                    profileCallback.onSuccess(response.body());
                } else {
                    profileCallback.onFailure(new ZenCheckException(
                            "Error occurred when trying to update profile: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<ProfileDto> call, Throwable t) {
                profileCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }
}
