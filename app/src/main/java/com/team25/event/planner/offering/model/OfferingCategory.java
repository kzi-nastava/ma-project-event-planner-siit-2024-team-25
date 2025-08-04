package com.team25.event.planner.offering.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferingCategory implements Parcelable {
    private Long id;
    private String name;
    private String description;
    private OfferingCategoryType status;

    OfferingCategory(Parcel in){
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        status = OfferingCategoryType.valueOf(in.readString());
    }

    public OfferingCategory(Long id, String name){
        this.id = id;
        this.name = name;
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
        dest.writeString(status.toString());
    }
    public static final Creator<OfferingCategory> CREATOR = new Creator<OfferingCategory>() {
        @Override
        public OfferingCategory createFromParcel(Parcel in) {
            return new OfferingCategory(in);
        }

        @Override
        public OfferingCategory[] newArray(int size) {
            return new OfferingCategory[size];
        }
    };
    @NonNull
    @Override
    public String toString() {
        return name;
    }

}
