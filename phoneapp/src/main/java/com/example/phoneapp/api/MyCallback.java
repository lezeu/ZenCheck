package com.example.phoneapp.api;

import com.example.phoneapp.utils.ZenCheckException;

public interface MyCallback<T> {
    void onSuccess(T result);
    void onFailure(ZenCheckException exception);
}
