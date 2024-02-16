package com.example.traficoreto.helpers;

import com.example.traficoreto.structure.User;

public interface UserCallback {
    void onSuccess(User user);
    void onError(String errorMessage);
}

