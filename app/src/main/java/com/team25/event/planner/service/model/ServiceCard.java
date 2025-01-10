package com.team25.event.planner.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.Getter;
import lombok.Setter;

public class ServiceCard implements Parcelable {

    @Setter
    @Getter
    private Long id;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private Double price;
    @Setter
    @Getter
    private String image;

    protected ServiceCard(Parcel in) {
        id=in.readLong();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        image = in.readString();
    }

    public static final Creator<ServiceCard> CREATOR = new Creator<ServiceCard>() {
        @Override
        public ServiceCard createFromParcel(Parcel in) {
            return new ServiceCard(in);
        }

        @Override
        public ServiceCard[] newArray(int size) {
            return new ServiceCard[size];
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
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeString(image);
    }

}
