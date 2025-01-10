package com.team25.event.planner.core.api.serialization;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;

public class InstantAdapter extends TypeAdapter<Instant> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        out.value(value.atZone(ZoneOffset.UTC).format(formatter));
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        return Instant.from(formatter.parse(in.nextString()));
    }
}
