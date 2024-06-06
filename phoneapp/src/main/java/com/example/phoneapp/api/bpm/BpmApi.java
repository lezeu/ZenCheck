package com.example.phoneapp.api.bpm;

import com.example.phoneapp.api.ApiClient;
import com.example.phoneapp.api.MyCallback;
import com.example.phoneapp.dtos.bpm.BpmDto;
import com.example.phoneapp.exceptions.ZenCheckException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public enum BpmApi {
    INSTANCE;

    private final BpmService service;

    BpmApi() {
        service = new ApiClient<>(BpmService.class).createService();
    }

    public void getAllBpmValues(MyCallback<List<BpmDto>> bpmCallback) {
        Call<List<BpmDto>> call = service.getAllBpm();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<BpmDto>> call, Response<List<BpmDto>> response) {
                if (response.isSuccessful()) {
                    bpmCallback.onSuccess(response.body());
                } else {
                    bpmCallback.onFailure(new ZenCheckException(
                            "Error occurred when trying to get all bpm values: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<BpmDto>> call, Throwable t) {
                bpmCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }

    public void sendBpm(BpmDto bpmDto, MyCallback<BpmDto> bpmCallback) {
        Call<BpmDto> call = service.sendBpm(bpmDto);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<BpmDto> call, Response<BpmDto> response) {
                if (response.isSuccessful()) {
                    bpmCallback.onSuccess(response.body());
                } else {
                    bpmCallback.onFailure(new ZenCheckException(
                            "Error occurred when trying to send bpm value: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<BpmDto> call, Throwable t) {
                bpmCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }

    public void getDailyBpmValues(MyCallback<List<BpmDto>> bpmCallback) {
        Call<List<BpmDto>> call = service.getDailyBpm();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<BpmDto>> call, Response<List<BpmDto>> response) {
                if (response.isSuccessful()) {
                    bpmCallback.onSuccess(response.body());
                } else {
                    bpmCallback.onFailure(new ZenCheckException(
                            "Error occurred when trying to get all bpm values: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<BpmDto>> call, Throwable t) {
                bpmCallback.onFailure(new ZenCheckException(t.getMessage()));
            }
        });
    }
}
