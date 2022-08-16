package com.decider.assistant;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NonNls;

class Option {
    private static final String LOG_TAG = "OptionClass";

    @NonNls
    private String text;
    private final String placeHolder;

    Option(int optionNumber) {
        text = "";
        placeHolder = String.format("Option %d", optionNumber);
    }

    Option(String text, String placeHolder) {
        this.text = text;
        this.placeHolder = placeHolder;
    }

    String getDisplayText() {
        String result = text.isEmpty() ? placeHolder : text;
        if (BuildConfig.DEBUG) {
            Log.i(
                LOG_TAG,
                String.format("Getting Option [text: %s, placeHolder: %s]", text, placeHolder)
            );
        }
        return result;
    }

    String getText() {
        return text;
    }

    void setText(String newText) {
        text = newText;
    }

    String getPlaceholder() {
        return placeHolder;
    }

    @NonNull
    @Override
    public String toString() {
        return getDisplayText();
    }
}
