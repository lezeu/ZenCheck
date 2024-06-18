package com.example.phoneapp.api.stress;

import com.example.phoneapp.api.ApiClient;
import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.dtos.pulse.StressDto;
import com.example.phoneapp.utils.ZenCheckException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public enum StressApi {
    INSTANCE;

    private final StressService service;

    StressApi() {
        service = new ApiClient<>(StressService.class).createService();
    }

    public void getAllStressValues(MyCallback<List<StressDto>> stressCallback) {
        Call<List<StressDto>> call = service.getAllStress();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<StressDto>> call, Response<List<StressDto>> response) {
                if (response.isSuccessful()) {
                    stressCallback.onSuccess(response.body());
                } else {
                    stressCallback.onFailure(new ZenCheckException(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<StressDto>> call, Throwable t) {
                stressCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }

    public void sendStress(StressDto stressDto, MyCallback<StressDto> stressCallback) {
        Call<StressDto> call = service.sendStress(stressDto);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<StressDto> call, Response<StressDto> response) {
                if (response.isSuccessful()) {
                    stressCallback.onSuccess(response.body());
                } else {
                    stressCallback.onFailure(new ZenCheckException(response.message()));
                }
            }

            @Override
            public void onFailure(Call<StressDto> call, Throwable t) {
                stressCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }

    public void getDailyStress(MyCallback<List<StressDto>> stressCallback) {
        Call<List<StressDto>> call = service.getDailyStress();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<StressDto>> call, Response<List<StressDto>> response) {
                if (response.isSuccessful()) {
                    stressCallback.onSuccess(response.body());
                } else {
                    stressCallback.onFailure(new ZenCheckException(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<StressDto>> call, Throwable t) {
                stressCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }
}
