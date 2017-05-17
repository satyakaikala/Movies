package com.kaikala.movies.adapters;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.kaikala.movies.constants.Constants;

/**
 * Created by kaikala on 10/9/16.
 */

public class MovieTrailers implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("size")
    private String size;
    @SerializedName("site")
    private String site;

    public MovieTrailers (String id, String key, String name, String size, String site) {
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

    public String getrailerUrl(){
        return Constants.BASE_TRAILER_URL + key;
    }

    public String getTrailerImageUrl(){
        return Constants.BASE_TRAILER_IMAGE_URL + key + Constants.IMAGE_EXTRAS;
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
