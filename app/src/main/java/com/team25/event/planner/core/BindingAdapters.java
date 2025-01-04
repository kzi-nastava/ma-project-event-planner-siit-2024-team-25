package com.team25.event.planner.core;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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


    @BindingAdapter(value = {"app:onSeeking"})
    public static void onProgressChanged(SeekBar seekBar, final OnProgressChanged seeking
                                         ) {
        if (seeking != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seeking.onSeeking(seekBar, progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }
    public interface OnProgressChanged {
        void onSeeking(SeekBar seekBar, Integer progress);

    }

    @BindingAdapter("formattedTime")
    public static void setFormattedTime(TextView textView, LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            textView.setText(dateTime.toLocalTime().format(formatter));
        } else {
            textView.setText("");
        }
    }

    @BindingAdapter("formattedDate")
    public static void setFormattedDate(TextView textView, LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            textView.setText(dateTime.toLocalDate().format(formatter));
        } else {
            textView.setText("");
        }
    }

    @BindingAdapter("formattedDateTime")
    public static void setFormattedDateTime(TextView textView, LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            textView.setText(dateTime.format(formatter));
        } else {
            textView.setText("");
        }
    }

}
