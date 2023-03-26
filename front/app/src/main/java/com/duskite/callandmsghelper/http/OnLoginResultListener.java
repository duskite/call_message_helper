package com.duskite.callandmsghelper.http;

import androidx.annotation.Nullable;

public interface OnLoginResultListener {

    void loginResult(boolean isLogin, @Nullable String errorMessage);
}
