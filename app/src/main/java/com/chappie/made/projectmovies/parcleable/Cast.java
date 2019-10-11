package com.chappie.made.projectmovies.parcleable;

import android.os.Parcel;
import android.os.Parcelable;

public class Cast implements Parcelable {
    private String name, role, img_cast;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImg_cast() {
        return img_cast;
    }

    public void setImg_cast(String img_cast) {
        this.img_cast = img_cast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.role);
        dest.writeString(this.img_cast);
    }

    public Cast() {
    }

    protected Cast(Parcel in) {
        this.name = in.readString();
        this.role = in.readString();
        this.img_cast = in.readString();
    }

    public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel source) {
            return new Cast(source);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };
}