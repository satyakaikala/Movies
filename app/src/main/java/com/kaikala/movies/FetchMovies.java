package com.kaikala.movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kaIkala on 8/17/2016.
 */
public class FetchMovies extends AsyncTask<String, Void, String[]>{


    public static final String TAG = FetchMovies.class.getSimpleName();
    private Context mContext;
    MoviePoster poster;
    public static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String ORDER_POPULAR = "popular?";
    public static final String ORDER_TOP_RATED = "top_rated?";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185/";
    public static final String API_KEY = "";

   private String[] getMoviePostersFromJson(String movieJsonStr) throws JSONException{
       // these are the names of the JSON objects that need to be extracted.

       final String RESULTS = "results";
       final String MOVIE_POSTER_PATH = "poster_path";
       final boolean MOVIE_ADULT = Boolean.parseBoolean("adult");
       final String MOVIE_OVERVIEW = "overview";
       final String MOVIE_RELEASE_DATE = "release_date";
       final String MOVIE_ID = "id";
       final String MOVIE_ORIGINAL_TITLE = "original_title";
       final String MOVIE_TITLE = "title";
       final String MOVIE_BACKDROP_PATH = "backdrop_path";
       final long MOVIE_POPULARITY = Long.parseLong("popularity");
       final String MOVIE_VOTE_COUNT = "vote_count";
       final long MOVIE_VOTE_AVERAGE = Long.parseLong("vote_average");

       String[] movieThumbnailpaths = new String[0];
       JSONObject movieJson = new JSONObject(movieJsonStr);
       JSONArray moviesArray = movieJson.getJSONArray(RESULTS);

       for (int i = 0; i < moviesArray.length(); i++){
           String movieOverview;
           String id;
           String releaseDate;
           String title;
           String posterPath;

           JSONObject movie = moviesArray.getJSONObject(i);

           movieOverview = movie.getString(MOVIE_OVERVIEW);
           id = movie.getString(MOVIE_ID);
           releaseDate = movie.getString(MOVIE_RELEASE_DATE);
           title = movie.getString(MOVIE_TITLE);
           posterPath = movie.getString(MOVIE_POSTER_PATH);

           poster = new MoviePoster();
           poster.setmId(id);
           poster.setmTitle(title);
           poster.setmUrl(IMAGE_BASE_URL+IMAGE_SIZE+posterPath);
           movieThumbnailpaths[i] = posterPath;
       }
       for (String path : movieThumbnailpaths){
           Log.v(TAG, " poster thumbnail path :" +path);
       }
       return movieThumbnailpaths;
   }


    @Override
    protected String[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJson = null;

        try {
            //construct the url for the movies

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendQueryParameter("popular", ORDER_POPULAR)
                    .appendQueryParameter("API_KEY", API_KEY).build();

            URL url = new URL(builtUri.toString());
            Log.v(TAG, "Built uri:" + builtUri.toString());

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null ) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0){
                return null;
            }
            moviesJson = buffer.toString();
        } catch (IOException e){
            Log.e(TAG, "ERROR : ", e);
            return null;
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try{
                    reader.close();
                } catch (final IOException e){
                    Log.e(TAG, "ERROR clossing buffer stream ", e);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] posterThumbnailPath) {
        if (posterThumbnailPath != null){

            for (String posterURL : posterThumbnailPath){
                Picasso.with(mContext).load(poster.getmUrl());
            }
        }
    }
}
