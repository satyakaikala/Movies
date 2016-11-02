package com.kaikala.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kaikala.movies.adapters.MovieTraileBaseAdapter;
import com.kaikala.movies.adapters.MovieTrailers;
import com.kaikala.movies.operations.FetchMovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaIkala on 8/24/2016.
 */
public class MovieDetailFragment extends Fragment implements FetchMovieTrailer.TrailerFetchCompleted {

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
     ListView trailers_info;


    private MoviePoster movie;
    private MovieTrailers trailersList;
    FetchMovieTrailer fetchMovieTrailer;

    private ArrayList<MovieTrailers> trailersArrayList;
    private MovieTraileBaseAdapter trailersAdapter;

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

        ShareActionProvider mShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareAction != null) {
            mShareAction.setShareIntent(shareMovieInfoIntent());
        } else {
            Log.d(TAG, "share action provider is null");
        }
    }

    private Intent shareMovieInfoIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String movieTitle = movie.getmTitle();
        String rating = movie.getmrating();
        String msg = "Hey there ! How about a movie plan, I am likely to watch" + " " + "'" + movieTitle + "'" + " " + "has rating" + " " + rating;

        shareIntent.putExtra(Intent.EXTRA_TEXT, msg);

        return shareIntent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
        Intent movieData = getActivity().getIntent();
        trailersArrayList=new ArrayList<>();
        movie = movieData.getExtras().getParcelable(Intent.EXTRA_TEXT);
//        trailersList = movieData.getExtras().getParcelable(Intent.EXTRA_TEXT);

        if (movie != null && movieData.hasExtra(Intent.EXTRA_TEXT)) {
            String imagePath, imageBaseUrl;
            imagePath = null;
            imageBaseUrl = "http://image.tmdb.org/t/p/w500/";

            title.setText("Movie Title :" + movie.getmTitle());
            releaseDate.setText("Release Date :" + (movie.getReleaseDate()).substring(0, 4));
            rating.setText("Rating : " + (movie.getmrating()) + "/10");
            ratingBar.setRating(Float.valueOf(movie.getmrating()));
            overview.setText("Movie Plot :" + movie.getMovieOverview());
            imagePath = movie.getmposterUrl();

            Picasso.with(context).load(imageBaseUrl + imagePath)
                    .noFade().placeholder(R.drawable.place_holder_image_1)
                    .error(R.drawable.error_loading_image)
                    .into(posterImage);
        }
        updateTrailers();


        return view;
    }

    public void updateTrailers(){
        fetchMovieTrailer = new FetchMovieTrailer(this);
        fetchMovieTrailer.execute(movie.getmId());
        Log.d(TAG, "trailer fetch completed");
    }
    @Override
    public void trailerFetchCompleted(ArrayList<MovieTrailers> list) {
        if (list != null && list.size() > 0){
            trailersArrayList.clear();
            trailersArrayList.addAll(list);
            trailers_info.setAdapter(new MovieTraileBaseAdapter(getContext(),trailersArrayList));
        }
    }
}
