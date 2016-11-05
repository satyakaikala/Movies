package com.kaikala.movies.adapters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kaikala on 10/9/16.
 */

public class MovieTrailers implements Parcelable {

    private String id;
    private String key;
    private String name;
    private String size;
    private String type;
    private String site;

    public static final String BASE_TRAILER_URL = "http://www.youtube.com/watch?v=";
    public static final String BASE_TRAILER_IMAGE_URL = "http://img.youtube.com/vi/";
    public static final String IMAGE_EXTRAS = "/0.jpg";


    public MovieTrailers(String id, String key, String name, String size, String site){
        this.id = id;
        this.key = key;
        this.name = name;
        this.size = size;
        this.site = site;

    }
    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }


    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getrailerUrl(){
        return BASE_TRAILER_URL + key;
    }

    public String getTrailerImageUrl(){
        return BASE_TRAILER_IMAGE_URL + key + IMAGE_EXTRAS;
    }

    protected MovieTrailers(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readString();
    }

    public static final Creator<MovieTrailers> CREATOR = new Creator<MovieTrailers>() {
        @Override
        public MovieTrailers createFromParcel(Parcel in) {
            return new MovieTrailers(in);
        }

        @Override
        public MovieTrailers[] newArray(int size) {
            return new MovieTrailers[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(size);
    }

}
