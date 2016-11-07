package com.kaikala.movies.adapters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kaikala on 11/4/16.
 */

public class MovieReviews implements Parcelable {

    private String reviewID;
    private String reviewAuthor;
    private String reviewContent;
    private String reviewUrl;

    public MovieReviews(String id, String author, String content, String url){
        this.reviewID = id;
        this.reviewAuthor = author;
        this.reviewContent = content;
        this.reviewUrl = url;
    }
    protected MovieReviews(Parcel in) {
        reviewID = in.readString();
        reviewAuthor = in.readString();
        reviewContent = in.readString();
        reviewUrl = in.readString();
    }

    public String getReviewID() {
        return reviewID;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public static final Creator<MovieReviews> CREATOR = new Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel in) {
            return new MovieReviews(in);
        }

        @Override
        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewID);
        dest.writeString(reviewAuthor);
        dest.writeString(reviewContent);
        dest.writeString(reviewUrl);
    }
}
