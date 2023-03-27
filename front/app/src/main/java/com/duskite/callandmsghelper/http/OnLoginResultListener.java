package com.duskite.callandmsghelper.http;

import androidx.annotation.Nullable;

public interface OnLoginResultListener {

    void loginResult(String userId, @Nullable String errorMessage);
}
