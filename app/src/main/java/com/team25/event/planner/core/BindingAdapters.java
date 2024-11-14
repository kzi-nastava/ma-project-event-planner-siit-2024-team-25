package com.team25.event.planner.core;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class BindingAdapters {
    @BindingAdapter("app:errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }

    // used for phone number input
    @BindingAdapter("app:bindText")
    public static void bindText(TextInputEditText editText, LiveData<String> liveData) {
        if (liveData == null) return;

        String currentValue = liveData.getValue();
        if (currentValue != null && editText.getText() != null && !currentValue.equals(editText.getText().toString())) {
            editText.setText(currentValue);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (liveData instanceof MutableLiveData) {
                    if (!s.toString().equals(liveData.getValue())) {
                        ((MutableLiveData<String>) liveData).setValue(s.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
