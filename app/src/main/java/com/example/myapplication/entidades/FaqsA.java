package com.example.myapplication.entidades;

import android.os.Parcel;
import android.os.Parcelable;

public class FaqsA implements Parcelable {
    public final String name;

    public FaqsA(String name) {
        this.name = name;
    }

    protected FaqsA(Parcel in) {
        name = in.readString();
    }

    public static final Creator<FaqsA> CREATOR = new Creator<FaqsA>() {
        @Override
        public FaqsA createFromParcel(Parcel in) {
            return new FaqsA(in);
        }

        @Override
        public FaqsA[] newArray(int size) {
            return new FaqsA[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
