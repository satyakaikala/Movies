package com.kaikala.movies.operations;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.kaikala.movies.BuildConfig;
import com.kaikala.movies.adapters.MoviePoster;
import com.kaikala.movies.constants.Constants;
import com.kaikala.movies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kaikala on 11/4/16.
 */

public class FetchPosters extends AsyncTask<String, Void, ArrayList<MoviePoster>> {
    private final String TAG = FetchPosters.class.getSimpleName();


    private PostersFetchCompleted fetchCompleted;

    public FetchPosters(PostersFetchCompleted completed) {
        this.fetchCompleted = completed;
    }

    public interface PostersFetchCompleted {
        void posterFetchCompleted(ArrayList<MoviePoster> list);
    }

    @Override
    protected ArrayList<MoviePoster> doInBackground(String... params) {

        String sortOrder = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;

        try {
            //construct the url for the movies
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme(Constants.SCHEME)
                    .authority(Constants.AUTHORITY)
                    .appendPath(Constants.NUMBER)
                    .appendPath(Constants.MOVIE)
                    .appendPath(sortOrder)
                    .appendQueryParameter(Constants.API_KEY, BuildConfig.API_KEY);


            URL url = new URL(uriBuilder.build().toString());
            Log.v(TAG, "Built uri:" + uriBuilder.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "ERROR : ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "ERROR closing buffer stream ", e);
                }
            }
        }

        try {
            return getMoviePostersFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MoviePoster> posters) {
        super.onPostExecute(posters);
        fetchCompleted.posterFetchCompleted(posters);
        Log.d(TAG, "movie results : " + posters);
    }

    //https://api.themoviedb.org/3/movie/550?api_key=
    //http://api.themoviedb.org/3/movie/popular?api_key=

    private ArrayList<MoviePoster> getMoviePostersFromJson(String movieJsonStr) throws JSONException {

        final String RESULTS = "results";
        final String MOVIE_POSTER_PATH = MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
        final String MOVIE_OVERVIEW = MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW;
        final String MOVIE_RELEASE_DATE = MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE;
        final String MOVIE_ID = MovieContract.MovieEntry.COLUMN_MOVIE_ID;

        final String MOVIE_TITLE = MovieContract.MovieEntry.COLUMN_MOVIE_TITLE;
        final String MOVIE_RATING = MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE;


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(RESULTS);
        ArrayList<MoviePoster> movieslist = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {
            String movieOverview;
            String id;
            String releaseDate;
            String title;
            String posterPath;
            String rating;

            JSONObject movie = movieArray.getJSONObject(i);

            movieOverview = movie.getString(MOVIE_OVERVIEW);
            id = movie.getString(MOVIE_ID);
            releaseDate = movie.getString(MOVIE_RELEASE_DATE);
            title = movie.getString(MOVIE_TITLE);
            posterPath = movie.getString(MOVIE_POSTER_PATH);
            rating = movie.getString(MOVIE_RATING);


            MoviePoster poster = new MoviePoster(posterPath, movieOverview, title, releaseDate, rating, id);
            movieslist.add(poster);
            Log.v(TAG + "Movies list :", movie.getString(MOVIE_TITLE));
        }

        return movieslist;
    }
}
