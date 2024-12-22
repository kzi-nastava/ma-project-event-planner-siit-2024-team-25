package com.team25.event.planner.core.api.serialization;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public void write(JsonWriter out, LocalTime value) throws IOException {
        out.value(value.format(formatter));
    }

    @Override
    public LocalTime read(JsonReader in) throws IOException {
        return LocalTime.parse(in.nextString(), formatter);
    }
}