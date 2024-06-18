package com.example.phoneapp.api.bpm;

import com.example.phoneapp.dtos.bpm.BpmDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BpmService {
    @GET("/api/bpm")
    Call<List<BpmDto>> getAllBpm();
    @POST("/api/bpm")
    Call<BpmDto> sendBpm(@Body BpmDto bpmDto);
    @GET("/api/bpm/daily")
    Call<List<BpmDto>> getDailyBpm();
}
