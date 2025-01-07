package com.team25.event.planner.product_service.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.product_service.enums.ProductServiceType;
import com.team25.event.planner.product_service.enums.ReservationType;

import java.util.List;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Service extends Offering implements Parcelable {
    private String specifics;
    private int duration;
    private int reservationDeadline;
    private int cancellationDeadline;
    private int minimumArrangement;
    private int maximumArrangement;
    private ReservationType reservationType;
    private OfferingCategory OfferingCategory;
    private List<EventType> eventTypes;

    public Service(){}

    public Service(Long id, String name, String description, int imageURL){
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }

    protected Service(Parcel in){
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        discount = in.readDouble();
        imageURL = in.readInt();
        visible = in.readByte() != 0;
        available = in.readByte() != 0;
        status = ProductServiceType.valueOf(in.readString());
        specifics = in.readString();
        duration = in.readInt();
        reservationDeadline = in.readInt();
        cancellationDeadline = in.readInt();
        minimumArrangement = in.readInt();
        maximumArrangement = in.readInt();
        reservationType = ReservationType.valueOf(in.readString());
        minimumArrangement = in.readInt();
        maximumArrangement = in.readInt();
    }
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
        dest.writeDouble(discount);
        dest.writeInt(imageURL);
        dest.writeByte((byte) (visible ? 1 : 0));
        dest.writeByte((byte) (available ? 1 : 0));
        dest.writeString(status.name());
        dest.writeString(specifics);
        dest.writeInt(duration);
        dest.writeInt(reservationDeadline);
        dest.writeInt(cancellationDeadline);
        dest.writeInt(minimumArrangement);
        dest.writeInt(maximumArrangement);
        dest.writeString(reservationType.name());
        dest.writeInt(minimumArrangement);
        dest.writeInt(maximumArrangement);
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };



}
