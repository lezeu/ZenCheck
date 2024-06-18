package com.example.watchapp.api.profile;

import com.example.watchapp.dtos.ProfileDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProfileService {
    @GET("/api/profile")
    Call<ProfileDto> getProfile();
    @POST("/api/profile")
    Call<ProfileDto> updateProfile(@Body ProfileDto profileDto);
}
