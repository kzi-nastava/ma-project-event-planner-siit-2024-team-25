package com.team25.event.planner.core;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class BindingAdapters {
    @BindingAdapter("app:decimalNumberText")
    public static void setDecimalText(TextView view, Double value) {
        if (value != null) {
            String formattedValue = String.format(Locale.US, "%.1f", value);
            if (view.getText() == null || !formattedValue.equals(view.getText().toString())) {
                view.setText(formattedValue);
            }
        } else {
            view.setText("");
        }
    }

    @BindingAdapter("app:decimalText")
    public static void setDecimalText(TextInputEditText view, Double value) {
        if (value != null) {
            String formattedValue = String.format(Locale.US, "%.2f", value);

            if (view.getText() == null || !formattedValue.equals(view.getText().toString())) {
                int cursorPosition = view.getSelectionStart();
                view.setText(formattedValue);
                view.setSelection(Math.min(cursorPosition, formattedValue.length()));
            }
        } else {
            view.setText("");
        }
    }

    @InverseBindingAdapter(attribute = "app:decimalText", event = "app:decimalTextAttrChanged")
    public static Double getDecimalText(TextInputEditText view) {
        String text = view.getText() == null ? "" : view.getText().toString().trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @BindingAdapter("app:decimalTextAttrChanged")
    public static void setDecimalTextListener(TextInputEditText view, final InverseBindingListener listener) {
        if (listener != null) {
            view.addTextChangedListener(new TextWatcher() {
                private String previousValue = "";

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String currentValue = s.toString();

                    // Only notify listener if the value has changed
                    if (!currentValue.equals(previousValue)) {
                        previousValue = currentValue;
                        listener.onChange();
                    }
                }
            });
        }
    }


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

}
