package com.kaikala.movies.adapters;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kaIkala on 8/17/2016.
 */
public class MoviePoster implements Parcelable {

    @SerializedName("title")
    private String title;
    @SerializedName("popularity")
    private String rating;
    @SerializedName("poster_path")
    private String posterUrl;
    @SerializedName("overview")
    private String movieOverview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("id")
    private String id;

    private String isFav;

    public MoviePoster() {

    }

    public MoviePoster(String posterUrl, String overview, String title, String release, String rating, String id) {
        this.title = title;
        this.posterUrl = posterUrl;
        movieOverview = overview;
        releaseDate = release;
        this.rating = rating;
        this.id = id;

    }

    protected MoviePoster(Parcel in) {
        title = in.readString();
        rating = in.readString();
        posterUrl = in.readString();
        movieOverview = in.readString();
        releaseDate = in.readString();
        id = in.readString();
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
        dest.writeString(title);
        dest.writeString(rating);
        dest.writeString(posterUrl);
        dest.writeString(movieOverview);
        dest.writeString(releaseDate);
        dest.writeString(id);
        dest.writeString(isFav);
    }

    public String getTitle() {
        return title;
    }

    public String getMovieOverview() {
        return movieOverview;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRating() {
        return rating;
    }


    public String getPosterUrl() {
        return posterUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String mId) {
        this.id = mId;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public void setPosterUrl(String mposterUrl) {
        this.posterUrl = mposterUrl;
    }

    public void setRating(String mrating) {
        this.rating = mrating;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    @Override
    public String toString() {
        return "MoviePoster{" +
                "title='" + title + '\'' +
                ", rating='" + rating + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", movieOverview='" + movieOverview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", id='" + id + '\'' +
                ", isFav='" + isFav + '\'' +
                '}';
    }
}
