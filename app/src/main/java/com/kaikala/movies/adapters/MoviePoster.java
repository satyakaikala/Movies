package com.kaikala.movies.adapters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kaIkala on 8/17/2016.
 */
public class MoviePoster implements Parcelable{

    private String mTitle;
    private String mrating;
    private String mposterUrl;
    private String movieOverview;
    private String releaseDate;
    private String mId;
    private String isFav;

    public MoviePoster(String posterUrl, String overview, String title, String release, String rating, String id) {
        mTitle = title;
        mposterUrl = posterUrl;
        movieOverview = overview;
        releaseDate = release;
        mrating = rating;
        mId = id;

    }

    protected MoviePoster(Parcel in) {
        mTitle = in.readString();
        mrating = in.readString();
        mposterUrl = in.readString();
        movieOverview = in.readString();
        releaseDate = in.readString();
        mId = in.readString();
        isFav = in.readString();
    }

    public static final Creator<MoviePoster> CREATOR = new Creator<MoviePoster>() {
        @Override
        public MoviePoster createFromParcel(Parcel in) {
            return new MoviePoster(in);
        }

        @Override
        public MoviePoster[] newArray(int size) {
            return new MoviePoster[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mrating);
        dest.writeString(mposterUrl);
        dest.writeString(movieOverview);
        dest.writeString(releaseDate);
        dest.writeString(mId);
        dest.writeString(isFav);
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    public String getmrating() {
        return mrating;
    }


    public String getmposterUrl() {
        return mposterUrl;
    }

    public String getmId(){
        return mId;
    }

}
