package com.team25.event.planner.event.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class EventCard implements Parcelable {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDateTime;
    private String city;
    private String country;
    private String organizerFirstName;
    private String organizerLastName;
    private Boolean isFavorite;

    protected EventCard(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        organizerFirstName = in.readString();
        organizerLastName = in.readString();
        country = in.readString();
        city = in.readString();
        startDateTime = LocalDateTime.parse(in.readString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME); // Parsiranje u LocalDateTime
        isFavorite = in.readBoolean();
    }

    public static final Creator<EventCard> CREATOR = new Creator<EventCard>() {
        @Override
        public EventCard createFromParcel(Parcel in) {
            return new EventCard(in);
        }

        @Override
        public EventCard[] newArray(int size) {
            return new EventCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(organizerFirstName);
        dest.writeString(organizerLastName);
        dest.writeString(description);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // Formatiranje LocalDateTime u String
        dest.writeBoolean(isFavorite);
    }
}
