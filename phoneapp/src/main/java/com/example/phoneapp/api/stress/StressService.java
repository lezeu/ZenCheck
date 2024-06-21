package com.example.phoneapp.api.stress;

import com.example.phoneapp.dtos.pulse.StressDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StressService {
    @GET("/api/stress")
    Call<List<StressDto>> getAllStress();
    @POST("/api/stress")
    Call<StressDto> sendStress(@Body StressDto stressDto);
    @GET("/api/stress/daily")
    Call<List<StressDto>> getDailyStress();
    @GET("/api/stress/hour")
    Call<List<StressDto>> getHourStress(@Query("hour") int hour);
}
