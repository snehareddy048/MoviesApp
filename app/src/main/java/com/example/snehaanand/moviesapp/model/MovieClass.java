package com.example.snehaanand.moviesapp.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by snehaanandyeluguri on 9/9/15.
 */
public class MovieClass implements Parcelable {

    private Integer id;

    private String original_title;

    private String poster_path;

    private String overview;

    private String release_date;

    private Float vote_average;

    //bitmap to be displayed
    private Bitmap display_image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Float getVote_average() {
        return vote_average;
    }

    public void setVote_average(Float vote_average) {
        this.vote_average = vote_average;
    }

    public Bitmap getDisplay_image() {
        return display_image;
    }

    public void setDisplay_image(Bitmap display_image) {
        this.display_image = display_image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private MovieClass(Parcel in) {
        id=in.readInt();
        original_title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        vote_average = in.readFloat();
        display_image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeFloat(vote_average);
        dest.writeParcelable(display_image, flags);
    }

    public static final Parcelable.Creator<MovieClass> CREATOR
            = new Parcelable.Creator<MovieClass>() {
        public MovieClass createFromParcel(Parcel in) {
            return new MovieClass(in);
        }

        public MovieClass[] newArray(int size) {
            return new MovieClass[size];
        }
    };
}
