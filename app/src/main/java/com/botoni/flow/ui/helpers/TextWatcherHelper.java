package com.botoni.flow.ui.helpers;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

public class TextWatcherHelper {
    @NonNull
    public static TextWatcher SimpleTextWatcher(@NonNull Runnable runnable) {
        return new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runnable.run();
            }
        };
    }

    @NonNull
    public static TextWatcher SearchTextWatcher(@NonNull Runnable runnable) {
        return new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    runnable.run();
                }
            }
        };
    }
}