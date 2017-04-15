package com.kaikala.movies.fragments;


import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaikala.movies.BuildConfig;
import com.kaikala.movies.R;
import com.kaikala.movies.activities.MovieDetailActivity;
import com.kaikala.movies.adapters.MoviePoster;
import com.kaikala.movies.adapters.MoviePosterAdapter;
import com.kaikala.movies.adapters.MovieResponse;
import com.kaikala.movies.constants.Constants;
import com.kaikala.movies.data.MovieContract;
import com.kaikala.movies.operations.ApiClient;
import com.kaikala.movies.operations.FetchPosters;
import com.kaikala.movies.operations.MovieNetworkInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kaikala.movies.fragments.GridSpacingItemDecoration.dpTopx;

/**
 * Created by kaIkala on 8/17/2016.
 */
public class FragmentMoviesList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, FetchPosters.PostersFetchCompleted, MoviePosterAdapter.MoviePosterOnClickHandler {


    private static final String TAG = FragmentMoviesList.class.getSimpleName();
    private MoviePosterAdapter moviePosterAdapter;
    private ArrayList<MoviePoster> moviePosters;
    private static int index;

    private MovieNetworkInterface networkService = ApiClient.getClient().create(MovieNetworkInterface.class);

    private static final int LOADER = 0;

    @BindView(R.id.recycler_view)
    RecyclerView movieThumbnailView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart called");
        String order = Constants.getSelectedOrder(getActivity());
        fetchMovies(order);
        getActivity().registerReceiver(networkChangeReceiver, filter);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.MOVIE_POSTERS, moviePosters);
        outState.putInt(Constants.SCROLL_POSITION, movieThumbnailView.getScrollState());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called");
        if (savedInstanceState != null) {
            moviePosters = savedInstanceState.getParcelableArrayList(Constants.MOVIE_POSTERS);
            index = savedInstanceState.getInt(Constants.SCROLL_POSITION);
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

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    private void handleCall(Call<MovieResponse> call) {
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                moviePosters = response.body().getResults();
                Log.d("Response","===:  "+moviePosters.toString());
                moviePosterAdapter = new MoviePosterAdapter(getActivity(), FragmentMoviesList.this, moviePosters);
                movieThumbnailView.setAdapter(moviePosterAdapter);
                moviePosterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String selectedOrder;
        Call<MovieResponse> call;
        switch (id) {
            case R.id.popular:
                selectedOrder = Constants.POPULAR;
                Constants.setSelectedOrder(getActivity(), selectedOrder);
                call = networkService.getPopularMovie(BuildConfig.API_KEY);
                handleCall(call);
                return true;
            case R.id.topRated:
                selectedOrder = Constants.TOP_RATED;
                Constants.setSelectedOrder(getActivity(), selectedOrder);
                call = networkService.getTopRatedMovie(BuildConfig.API_KEY);
                handleCall(call);
                return true;
            case R.id.favoirte:
                selectedOrder = Constants.FAVORITE;
                Constants.setSelectedOrder(getActivity(), selectedOrder);
                fetchFavoriteCollection();
                return true;
            case R.id.action_search:
                //implement search functionality
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
            case Constants.FAVORITE:
                menu.findItem(R.id.favoirte).setChecked(true);
                break;
        }
        super.onPrepareOptionsMenu(menu);
    }

    public void fetchFavoriteCollection() {
        moviePosters = getFavoriteCollection();
        moviePosterAdapter = new MoviePosterAdapter(getActivity(), this, moviePosters);
        movieThumbnailView.setAdapter(moviePosterAdapter);
        moviePosterAdapter.notifyDataSetChanged();
    }

    public void fetchMovies(String selectedOrder) {
        FetchPosters fetchMovies = new FetchPosters(this);
        fetchMovies.execute(selectedOrder);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchFavoriteCollection();
    }

    public ArrayList<MoviePoster> getFavoriteCollection() {
        moviePosters = new ArrayList<>();
        Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_COLUMNS,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                MoviePoster poster = new MoviePoster();
                poster.setId(cursor.getString(MovieContract.MovieEntry.COL_MOVIE_ID));
                poster.setTitle(cursor.getString(MovieContract.MovieEntry.COL_MOVIE_TITLE));
                poster.setPosterUrl(cursor.getString(MovieContract.MovieEntry.COL_MOVIE_POSTER_PATH));
                poster.setMovieOverview(cursor.getString(MovieContract.MovieEntry.COL_MOVIE_OVERVIEW));
                poster.setRating(cursor.getString(MovieContract.MovieEntry.COL_MOVIE_VOTE_AVERAGE));
                poster.setReleaseDate(cursor.getString(MovieContract.MovieEntry.COL_MOVIE_RELEASE_DATE));
                Log.d(TAG, "poster values : " + poster);
                moviePosters.add(poster);
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "cursor values : " + cursor);
        return moviePosters;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, view);
        movieThumbnailView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (!Constants.isOnline(getContext())) {
            Toast.makeText(getActivity(), "no internet", Toast.LENGTH_SHORT).show();
        }
        if (savedInstanceState != null) {
            moviePosters = savedInstanceState.getParcelableArrayList(Constants.MOVIE_POSTERS);
        } else {
            moviePosters = new ArrayList<>();
        }
        moviePosterAdapter = new MoviePosterAdapter(getActivity(), this, moviePosters);
        movieThumbnailView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        movieThumbnailView.addItemDecoration(new GridSpacingItemDecoration(2, dpTopx(getActivity(), 10), true));
        movieThumbnailView.setItemAnimator(new DefaultItemAnimator());
        movieThumbnailView.setHasFixedSize(true);
        movieThumbnailView.setAdapter(moviePosterAdapter);
        movieThumbnailView.setVerticalScrollbarPosition(index);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
        swipeAction();
        getActivity().getSupportLoaderManager().initLoader(LOADER, null, this);

        return view;
    }

    @Override
    public void posterFetchCompleted(ArrayList<MoviePoster> list) {
        if (list != null && list.size() > 0) {
            moviePosters.clear();
            moviePosters.addAll(list);
        }
        moviePosterAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(MoviePoster poster) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, poster);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_COLUMNS,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swipeRefreshLayout.setRefreshing(false);
        if (data.getCount() != 0) {

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {

        if (Constants.isOnline(getContext()) && moviePosterAdapter.getItemCount() == 0) {
            String order = Constants.getSelectedOrder(getContext());
            fetchMovies(order);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void swipeAction() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                getActivity().getContentResolver().delete(MovieContract.MovieEntry.movieUriId(moviePosters.get(viewHolder.getAdapterPosition()).getId()), null, null);
                moviePosterAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(movieThumbnailView);
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Change in Network connectivity");
            if (intent.getExtras() != null) {
                if (Constants.isOnline(getContext())) {
                    String order = Constants.getSelectedOrder(getContext());
                    fetchMovies(order);
                }
                Log.d(TAG, "There's no network connectivity");
            }
        }
    }
}

