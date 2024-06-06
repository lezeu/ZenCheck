package com.example.phoneapp.api;

import com.example.phoneapp.exceptions.ZenCheckException;

public interface MyCallback<T> {
    void onSuccess(T result);
    void onFailure(ZenCheckException exception);
}
