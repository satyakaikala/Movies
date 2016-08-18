package com.kaikala.movies;

/**
 * Created by kaIkala on 8/17/2016.
 */
public class MoviePoster {

    private String mTitle;
    private String mId;
    private String mUrl;
    private String movieOverview;
    private String releaseDate;

    public String getmTitle(){
        return mTitle;
    }

    public void setmTitle(String mTitle){
        this.mTitle = mTitle;
    }

    public String getMovieOverview(){
        return movieOverview;
    }

    public void setMovieOverview(String overview){
        this.movieOverview = overview;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }
    public String getmId(){
        return mId;
    }

    public void setmId(String mId){
        this.mId = mId;
    }

    public String getmUrl(){
        return  mUrl;
    }

    public void setmUrl(String mUrl){
        this.mUrl = mUrl;
    }

}
