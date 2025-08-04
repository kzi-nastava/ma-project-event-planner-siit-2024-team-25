package com.team25.event.planner.offering.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferingCard implements Parcelable {
    private Long id;
    private String name;
    private double price;
    private String ownerName;
    private double rating;
    private boolean isService;

    protected OfferingCard(Parcel in) {
        id = in.readLong();
        name = in.readString();
        price = in.readDouble();
        ownerName = in.readString();
        isService = in.readBoolean();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(ownerName);
        dest.writeDouble(rating);
        dest.writeBoolean(isService);
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
