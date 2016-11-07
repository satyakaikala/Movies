package com.kaikala.movies.operations;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.kaikala.movies.BuildConfig;
import com.kaikala.movies.adapters.MovieReviews;
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
 * Created by kaikala on 11/4/16.
 */

public class FetchReviews extends AsyncTask <String, Void, ArrayList<MovieReviews>>{

    private static final String TAG = FetchReviews.class.getSimpleName();

    private ReviewFetchCompleted fetchCompleted;

    public FetchReviews(ReviewFetchCompleted completed){
        this.fetchCompleted = completed;
    }

    public interface ReviewFetchCompleted{
        void reviewFetchCompleted(ArrayList<MovieReviews> list);
    }

    @Override
    protected ArrayList<MovieReviews> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        String movieId = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewJsonStr = null;

        try {

            //construct the url for the movie trailers

            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme(Constants.SCHEME)
                    .authority(Constants.AUTHORITY)
                    .appendPath(Constants.NUMBER)
                    .appendPath(Constants.MOVIE)
                    .appendPath(movieId)
                    .appendPath(Constants.REVIEWS)
                    .appendQueryParameter(Constants.API_KEY, BuildConfig.API_KEY);


            URL url = new URL(uriBuilder.build().toString());
            Log.v(TAG, "Built uri for Reviews:" + uriBuilder.toString());

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
            reviewJsonStr = buffer.toString();
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
            return getMovieReviewsFromJson(reviewJsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieReviews> movieReviews) {
        super.onPostExecute(movieReviews);
        fetchCompleted.reviewFetchCompleted(movieReviews);
        for (MovieReviews r : movieReviews){
            Log.d(TAG, "Reviews:" + " " + movieReviews);
        }
    }

    private ArrayList<MovieReviews> getMovieReviewsFromJson(String movieReviews) throws JSONException {

        final String RESULTS = "results";
        final String MOVIE_REVIEW_ID = "id";
        final String MOVIE_REVIEW_AUTHOR = "author";
        final String MOVIE_REVIEW_CONTENT = "content";
        final String MOVIE_REVIEW_URL = "url";

        Log.v(TAG+"results",movieReviews);

        JSONObject reviewJson = new JSONObject(movieReviews);
        JSONArray moviesReviewArray = reviewJson.getJSONArray(RESULTS);
        ArrayList<MovieReviews> movieReviewslist = new ArrayList<>();
        for (int i = 0; i < moviesReviewArray.length(); i++) {
            String id;
            String author;
            String content;
            String url;

            JSONObject movietrailer = moviesReviewArray.getJSONObject(i);

            id = movietrailer.getString(MOVIE_REVIEW_ID);
            author = movietrailer.getString(MOVIE_REVIEW_AUTHOR);
            content = movietrailer.getString(MOVIE_REVIEW_CONTENT);
            url = movietrailer.getString(MOVIE_REVIEW_URL);

            MovieReviews reviews = new MovieReviews(id, author, content, url);
            movieReviewslist.add(reviews);
            Log.v(TAG + "Reviews list :", reviews.getReviewID()+" ," + reviews.getReviewAuthor());
        }

        return movieReviewslist;
    }
}
