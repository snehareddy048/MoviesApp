package com.example.snehaanand.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by snehaanandyeluguri on 10/18/15.
 */
public class TrailerClass implements Parcelable
{
    private String name;

    private String key;

    protected TrailerClass(Parcel in) {
        name = in.readString();
        key = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
    }

    public static final Creator<TrailerClass> CREATOR = new Creator<TrailerClass>() {
        @Override
        public TrailerClass createFromParcel(Parcel in) {
            return new TrailerClass(in);
        }

        @Override
        public TrailerClass[] newArray(int size) {
            return new TrailerClass[size];
        }
    };
}
