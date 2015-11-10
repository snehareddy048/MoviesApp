package com.example.snehaanand.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by snehaanandyeluguri on 10/18/15.
 */
public class ReviewClass implements Parcelable {

    private String author;

    private String content;

    protected ReviewClass(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewClass> CREATOR = new Creator<ReviewClass>() {
        @Override
        public ReviewClass createFromParcel(Parcel in) {
            return new ReviewClass(in);
        }

        @Override
        public ReviewClass[] newArray(int size) {
            return new ReviewClass[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }
}
