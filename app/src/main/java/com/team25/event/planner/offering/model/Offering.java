package com.team25.event.planner.offering.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Offering implements Parcelable {
    private int id;
    private String name;
    private double price;
    private String owner;
    private String description;

    public Offering(int id, String name, double price, String owner, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.owner = owner;
        this.description = description;
    }

    protected Offering(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        owner = in.readString();
        description = in.readString();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static final Creator<Offering> CREATOR = new Creator<Offering>() {
        @Override
        public Offering createFromParcel(Parcel in) {
            return new Offering(in);
        }

        @Override
        public Offering[] newArray(int size) {
            return new Offering[size];
        }
    };
}
