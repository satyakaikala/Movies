package com.kaikala.movies;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.kaikala.movies.adapters.MovieTraileBaseAdapter;
import com.kaikala.movies.adapters.MovieTrailers;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaIkala on 8/17/2016.
 */
public class FragmentMoviesList extends Fragment {


    private static final String TAG = FragmentMoviesList.class.getSimpleName();
    private MoviePosterAdapter moviePosterAdapter;
    private MovieTraileBaseAdapter movieTrailersAdapter;

    @BindView(R.id.grid_view) GridView movieThumbnailView;
    ArrayList<MoviePoster> mMovieAdapter;
    ArrayList<MovieTrailers> movieTrailers;
    int index;

    public FragmentMoviesList() {

    }
    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    @Override
    public void onStart() {
        super.onStart();
        String order = Constants.getSelectedOrder(getActivity());
        fetchMovies(order);
        getActivity().registerReceiver(networkChangeReceiver, filter);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("myAdapter", mMovieAdapter);
        outState.putInt("position", movieThumbnailView.getFirstVisiblePosition());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState !=null){
            mMovieAdapter = savedInstanceState.getParcelableArrayList("myAdapter");
            index = savedInstanceState.getInt("position");


        }
        setHasOptionsMenu(true);

    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String selectedOrder;
        switch (id){
            case R.id.popular:
                selectedOrder = "popular";
                Constants.setSelectedOrder(getActivity(),selectedOrder);
                fetchMovies(selectedOrder);
//                if (item.isChecked()) {
//                    item.setChecked(false);
//                }else {
//                    item.setChecked(true);
//                }
                return true;
            case R.id.topRated:
                selectedOrder = "top_rated";
                Constants.setSelectedOrder(getActivity(), selectedOrder);
                fetchMovies(selectedOrder);
//                if (item.isChecked()) {
//                    item.setChecked(false);
//                }else {
//                    item.setChecked(true);
//                }
               return true;
            default:

                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        String order = Constants.getSelectedOrder(getActivity());
        switch (order) {
            case Constants.POPULAR:
                menu.findItem(R.id.popular).setChecked(true);

                break;
            case Constants.TOP_RATED:
                menu.findItem(R.id.topRated).setChecked(true);
                break;
     }
        super.onPrepareOptionsMenu(menu);
    }

    private void fetchMovies(String selectedOrder) {

        FetchMoviePostersTask fetchMovies = new FetchMoviePostersTask();
        fetchMovies.execute(selectedOrder);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, view);
        if (!isOnline()){
           Toast.makeText(getActivity(),"no internet",Toast.LENGTH_SHORT).show();
       }
        if (savedInstanceState != null){
            mMovieAdapter = savedInstanceState.getParcelableArrayList("myAdapter");
        } else {
            mMovieAdapter = new ArrayList<MoviePoster>();
        }
        moviePosterAdapter = new MoviePosterAdapter(getActivity(), mMovieAdapter);
        movieTrailersAdapter = new MovieTraileBaseAdapter(getActivity(), movieTrailers);

        movieThumbnailView.setAdapter(moviePosterAdapter);

        movieThumbnailView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "pressed item is" + position, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, mMovieAdapter.get(position));
               // intent.putExtra(Intent.EXTRA_TEXT, movieTrailers.get(position));
                intent.putExtra("movieTrailers",movieTrailers);

                startActivity(intent);
            }
        });
        movieThumbnailView.setVerticalScrollbarPosition(index);
        movieThumbnailView.setSmoothScrollbarEnabled(true);
        return view;
    }

    private class FetchMoviePostersTask extends AsyncTask<String, Void, ArrayList<MoviePoster>> {
        private final String TAG = FetchMoviePostersTask.class.getSimpleName();
        final String KEY = "d37d289fc8a6ecc38b01e7b306e1665b";

        @Override
        protected ArrayList<MoviePoster> doInBackground(String... params) {

            String sortOrder = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;

            try {

                //construct the url for the movies

                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(sortOrder)
                        .appendQueryParameter("api_key", KEY);


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
        protected void onPostExecute(ArrayList<MoviePoster> movies) {
            mMovieAdapter.clear();

            if (movies != null) {
                for (MoviePoster m : movies) {
                    mMovieAdapter.add(m);
                }
            }
            moviePosterAdapter.notifyDataSetChanged();
            
            Log.v(TAG, "movie results : " + movies);
            super.onPostExecute(movies);
        }
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


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Change in Network connectivity");
            if (intent.getExtras() != null) {
                if (isOnline()) {
                    String order = Constants.getSelectedOrder(getContext());
                    fetchMovies(order);
                }
                Log.d(TAG, "There's no network connectivity");
            }
        }
    }
}

