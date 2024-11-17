package com.team25.event.planner.offering.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferingCard implements Parcelable {
    private int id;
    private String name;
    private double price;
    private String owner;
    private double rating;

    protected OfferingCard(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        owner = in.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(owner);
        dest.writeDouble(rating);
    }

    public static final Creator<OfferingCard> CREATOR = new Creator<OfferingCard>() {
        @Override
        public OfferingCard createFromParcel(Parcel in) {
            return new OfferingCard(in);
        }

        @Override
        public OfferingCard[] newArray(int size) {
            return new OfferingCard[size];
        }
    };
}
