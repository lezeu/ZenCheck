package com.example.watchapp.api;

import com.example.watchapp.utils.ZenCheckException;

public interface MyCallback<T> {
    void onSuccess(T result);
    void onFailure(ZenCheckException exception);
}
