package com.team25.event.planner.offering.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OfferingCard implements Parcelable {
    private int id;
    private String name;
    private double price;
    private String owner;

    public OfferingCard(int id, String name, double price, String owner) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.owner = owner;
    }

    protected OfferingCard(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        owner = in.readString();
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getOwner() {
        return owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
