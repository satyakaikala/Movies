package com.kaikala.movies.operations;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.kaikala.movies.BuildConfig;
import com.kaikala.movies.adapters.MovieTrailers;
import com.kaikala.movies.constants.Constants;

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
 * Created by kaikala on 10/9/16.
 */

public class FetchTrailers extends AsyncTask<String, Void, ArrayList<MovieTrailers>> {

    private static final String TAG = FetchTrailers.class.getSimpleName();

    private TrailerFetchCompleted fetchCompleted;

    public FetchTrailers(TrailerFetchCompleted completed){
        this.fetchCompleted = completed;
    }

    public interface TrailerFetchCompleted {
        void trailerFetchCompleted(ArrayList<MovieTrailers> list);
    }

    @Override
    protected ArrayList<MovieTrailers> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String movieId = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailerJsonStr = null;

        try {
            //construct the url for the movie trailers
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme(Constants.SCHEME)
                    .authority(Constants.AUTHORITY)
                    .appendPath(Constants.NUMBER)
                    .appendPath(Constants.MOVIE)
                    .appendPath(movieId)
                    .appendPath(Constants.TRAILERS)
                    .appendQueryParameter(Constants.API_KEY, BuildConfig.API_KEY);


            URL url = new URL(uriBuilder.build().toString());
            Log.v(TAG, "Built uri for trailer:" + uriBuilder.toString());

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
            trailerJsonStr = buffer.toString();
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
            return getMovieTrailersFromJson(trailerJsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieTrailers> movieTrailerses) {
        super.onPostExecute(movieTrailerses);
        if (movieTrailerses != null) {
            fetchCompleted.trailerFetchCompleted(movieTrailerses);
                for (MovieTrailers t : movieTrailerses) {
                    Log.d(TAG, "Trailers:" + " " + movieTrailerses);
                }
        }
    }

    private ArrayList<MovieTrailers> getMovieTrailersFromJson(String movieJsonStr) throws JSONException {

        final String RESULTS = "results";
        final String MOVIE_TRAILER_ID = "id";
        final String MOVIE_TRAILER_KEY = "key";
        final String MOVIE_TRAILER_NAME = "name";
        final String MOVIE_TRAILER_SITE = "site";
        final String MOVIE_TRAILER_SIZE = "size";
        Log.v(TAG+"results",movieJsonStr);

        JSONObject trailerJson = new JSONObject(movieJsonStr);
        JSONArray movietrailerArray = trailerJson.getJSONArray(RESULTS);
        ArrayList<MovieTrailers> moviestrailerlist = new ArrayList<>();
        for (int i = 0; i < movietrailerArray.length(); i++) {
            String id;
            String key;
            String name;
            String site;
            String size;

            JSONObject movietrailer = movietrailerArray.getJSONObject(i);

            id = movietrailer.getString(MOVIE_TRAILER_ID);
            key = movietrailer.getString(MOVIE_TRAILER_KEY);
            name = movietrailer.getString(MOVIE_TRAILER_NAME);
            site = movietrailer.getString(MOVIE_TRAILER_SITE);
            size = movietrailer.getString(MOVIE_TRAILER_SIZE);

            MovieTrailers trailer = new MovieTrailers(id, key, name, size, site);
            moviestrailerlist.add(trailer);
            Log.v(TAG + "Movies list :", trailer.getName()+" ," + trailer.getId());
        }

        return moviestrailerlist;
    }
}
