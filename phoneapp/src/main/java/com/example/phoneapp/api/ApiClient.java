package com.example.phoneapp.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient<T> {
    private static final String BASE_URL = "http://192.168.1.143:8080";
    private final Class<T> serviceClass;

    public ApiClient(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public T createService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
                .client(client).build();

        return retrofit.create(serviceClass);
    }
}
