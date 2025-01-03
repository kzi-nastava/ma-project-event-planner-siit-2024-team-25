package com.team25.event.planner.core.adapters;

import android.widget.TextView;
import androidx.databinding.BindingAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BindingAdapters {
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