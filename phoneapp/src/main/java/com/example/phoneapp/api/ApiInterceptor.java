package com.example.phoneapp.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        // for future use
        Request originalRequest = chain.request();
        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + "your_token")
                .build();
        return chain.proceed(modifiedRequest);
    }
}
