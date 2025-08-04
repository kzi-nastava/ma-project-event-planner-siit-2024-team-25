package com.team25.event.planner.product.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyProductCard implements Parcelable {
    private Long id;
    private String name;
    private double price;
    private String ownerName;
    private double rating;
    private boolean isService;
    private String thumbnail;

    protected MyProductCard(Parcel in) {
        id = in.readLong();
        name = in.readString();
        price = in.readDouble();
        ownerName = in.readString();
        isService = in.readBoolean();
        thumbnail = in.readString();
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
        dest.writeString(thumbnail);
    }

    public static final Creator<MyProductCard> CREATOR = new Creator<>() {
        @Override
        public MyProductCard createFromParcel(Parcel in) {
            return new MyProductCard(in);
        }

        @Override
        public MyProductCard[] newArray(int size) {
            return new MyProductCard[size];
        }
    };
}
