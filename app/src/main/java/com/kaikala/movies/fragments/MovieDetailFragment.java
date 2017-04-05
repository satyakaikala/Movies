package com.kaikala.movies.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaikala.movies.adapters.MoviePoster;
import com.kaikala.movies.R;
import com.kaikala.movies.adapters.MovieReviewAdapter;
import com.kaikala.movies.adapters.MovieReviews;
import com.kaikala.movies.adapters.MovieTrailerAdapter;
import com.kaikala.movies.adapters.MovieTrailers;
import com.kaikala.movies.data.MovieContract;
import com.kaikala.movies.operations.FetchTrailers;
import com.kaikala.movies.operations.FetchReviews;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaIkala on 8/24/2016.
 */
public class MovieDetailFragment extends Fragment implements FetchTrailers.TrailerFetchCompleted, FetchReviews.ReviewFetchCompleted {

    @BindView(R.id.movie_title)
    TextView title;
    @BindView(R.id.movie_releaseDate)
    TextView releaseDate;
    @BindView(R.id.userRating)
    TextView rating;
    @BindView(R.id.movie_overView)
    TextView overview;
    @BindView(R.id.movieRating)
    RatingBar ratingBar;
    @BindView(R.id.separator)
    View separator;
    @BindView(R.id.movie_poster)
    ImageView posterImage;
    @BindView(R.id.trailerText)
    TextView trailerText;
    @BindView(R.id.trailerSeparator)
    View trailerSeperator;
    @BindView(R.id.trailers_info)
    ListView trailersInfo;
    @BindView(R.id.favorite_button)
    ImageButton favButton;
    @BindView(R.id.reviews_info)
    ListView reviewsInfo;


    private MoviePoster movie;
    private boolean isFavorite;
    private ArrayList<MovieTrailers> trailersArrayList;
    private ArrayList<MovieReviews> reviewsArrayList;

    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_button, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
        menuItem.setIntent(shareMovieInfoIntent());
    }

    private Intent shareMovieInfoIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String movieTitle = movie.getTitle();
        String rating = movie.getRating();
        String msg = "Hey there ! How about a movie plan, I am likely to watch" + " " + "'" + movieTitle + "'" + " " + "has rating" + " " + rating;

        shareIntent.putExtra(Intent.EXTRA_TEXT, msg);

        return shareIntent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
        final Intent movieData = getActivity().getIntent();
        trailersArrayList=new ArrayList<>();
        reviewsArrayList = new ArrayList<>();
        movie = movieData.getExtras().getParcelable(Intent.EXTRA_TEXT);

        if (movie != null && movieData.hasExtra(Intent.EXTRA_TEXT)) {
            String imagePath;
            String imageBaseUrl;
            imageBaseUrl = getString(R.string.poster_base_url);

            title.setText(movie.getTitle());
            releaseDate.setText(getString(R.string.movie_releaseDate)+" :" + (movie.getReleaseDate()).substring(0, 4));
            rating.setText(getString(R.string.movie_ratting)+" : " + (movie.getRating()) + "/10");
            ratingBar.setRating(Float.valueOf(movie.getRating()));
            overview.setText(getString(R.string.movie_plot)+" :" + movie.getMovieOverview());
            imagePath = movie.getPosterUrl();

            Picasso.with(context).load(imageBaseUrl + imagePath)
                    .noFade().placeholder(R.drawable.place_holder_image_1)
                    .error(R.drawable.error_loading_image)
                    .into(posterImage);
        }

        isFavorite = isFavorite();
        favButton.setSelected(isFavorite);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();

                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getRating());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterUrl());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getMovieOverview());

                if (!isFavorite){
                    getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                    favButton.setSelected(true);
                    isFavorite = true;
                    Toast.makeText(getActivity(),"selected "+ movie.getTitle()+"  as fav movie", Toast.LENGTH_SHORT).show();
                } else {
                    getContext().getContentResolver().delete(MovieContract.MovieEntry.movieUriId(movie.getId()), null, new String[]{movie.getId()});
                    favButton.setSelected(false);
                    isFavorite = false;
                    Toast.makeText(getActivity(),"removed "+ movie.getTitle()+"  as fav movie", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateTrailers();
        updateReviews();

        return view;
    }

    public boolean isFavorite(){
        Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_COLUMNS,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{movie.getId()},null);
       boolean fav = false;
        if (cursor.moveToFirst()){
            fav = true;
        }
        cursor.close();
        return fav;
    }

    public void updateTrailers(){
        FetchTrailers fetchMovieTrailer = new FetchTrailers(this);
        fetchMovieTrailer.execute(movie.getId());
        Log.d(TAG, "trailer fetch completed");
    }

    public void updateReviews(){
        FetchReviews fetchReviews = new FetchReviews(this);
        fetchReviews.execute(movie.getId());
        Log.d(TAG, "Review fetch completed");

    }
    @Override
    public void trailerFetchCompleted(ArrayList<MovieTrailers> list) {
        if (list != null && list.size() > 0){
            trailersArrayList.clear();
            trailersArrayList.addAll(list);
            trailersInfo.setAdapter(new MovieTrailerAdapter(getContext(),trailersArrayList));
        }
    }

    @Override
    public void reviewFetchCompleted(ArrayList<MovieReviews> list) {
        if (list != null && list.size() > 0) {
            reviewsArrayList.clear();
            reviewsArrayList.addAll(list);
            reviewsInfo.setAdapter(new MovieReviewAdapter(getContext(), reviewsArrayList));
        }
    }
}
