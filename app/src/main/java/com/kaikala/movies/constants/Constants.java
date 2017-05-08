package com.kaikala.movies.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * Created by skai0001 on 10/8/16.
 */

public class Constants {

    Context context;
    public static final String TAG = Constants.class.getSimpleName();
    public static final int POPULAR = 0;
    public static final int TOP_RATED = 1;
    public static final int FAVORITE = 2;
    public static final String MOVIE_TRAILERS = "movie_trailers";
    public static final String MOVIE_REVIEWS = "movie_reviews";
    public static final String MOVIE_POSTERS = "movie_posters";
    public static final String SCROLL_POSITION = "scroll_position";
    public static final String TABLET = "tablet";
    //Http constants
    // "http://api.themoviedb.org/3/"
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String SCHEME = "http";
    public static final String AUTHORITY = "api.themoviedb.org";
    public static final String NUMBER = "3";
    public static final String MOVIE = "movie";
    public static final String TRAILERS = "videos";
    public static final String REVIEWS = "reviews";
    public static final String API_KEY = "api_key";
    public static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE = "w185";
    //trailers
    public static final String BASE_TRAILER_URL = "http://www.youtube.com/watch?v=";
    public static final String BASE_TRAILER_IMAGE_URL = "http://img.youtube.com/vi/";
    public static final String IMAGE_EXTRAS = "/0.jpg";

    public static int getSelectedOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("", 0);
    }

    public static void setSelectedOrder(Context context, int order) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("", order);
        editor.apply();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
