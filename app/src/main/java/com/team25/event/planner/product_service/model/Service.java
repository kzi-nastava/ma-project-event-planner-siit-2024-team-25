package com.team25.event.planner.product_service.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.team25.event.planner.product_service.enums.ProductServiceType;
import com.team25.event.planner.product_service.enums.ReservationType;

import java.time.LocalDateTime;

public class Service extends Offering implements Parcelable {
    private String specifics;
    private int duration;
    private LocalDateTime reservationDate;
    private LocalDateTime cancellationDate;
    private ReservationType reservationType;

    public Service(){}

    public Service(long id, String name, String description, double price, double discount,  int imageURL,
                   boolean isVisible, boolean isAvailable, ProductServiceType status, String specifics, int duration,
                   LocalDateTime reservationDate, LocalDateTime cancellationDate, ReservationType reservationType){
        super(id,name,description,price,discount,imageURL,isVisible,isAvailable,status);
        this.specifics = specifics;
        this.duration = duration;
        this.reservationDate = reservationDate;
        this.cancellationDate = cancellationDate;
        this.reservationType = reservationType;
    }

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
        isVisible = in.readByte() != 0;
        isAvailable = in.readByte() != 0;
        status = ProductServiceType.valueOf(in.readString());
        specifics = in.readString();
        duration = in.readInt();
        reservationDate = (LocalDateTime) in.readSerializable();
        cancellationDate = (LocalDateTime) in.readSerializable();
        reservationType = ReservationType.valueOf(in.readString());
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
        dest.writeByte((byte) (isVisible ? 1 : 0));
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        dest.writeString(status.name());
        dest.writeString(specifics);
        dest.writeInt(duration);
        dest.writeSerializable(reservationDate);
        dest.writeSerializable(cancellationDate);
        dest.writeString(reservationType.name());
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getImageURL() {
        return imageURL;
    }

    public void setImageURL(int imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public ProductServiceType getStatus() {
        return status;
    }

    public void setStatus(ProductServiceType status) {
        this.status = status;
    }

    public String getSpecifics() {
        return specifics;
    }

    public void setSpecifics(String specifics) {
        this.specifics = specifics;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public ReservationType getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationType reservationType) {
        this.reservationType = reservationType;
    }

}
