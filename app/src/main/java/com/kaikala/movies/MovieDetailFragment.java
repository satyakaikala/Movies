package com.kaikala.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kaIkala on 8/24/2016.
 */
public class MovieDetailFragment extends Fragment {

    ArrayList<String> movieArray;
    Context context;
    public MovieDetailFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent movieData = getActivity().getIntent();
        MoviePoster movie = movieData.getExtras().getParcelable(Intent.EXTRA_TEXT);
        if (movie != null && movieData.hasExtra(Intent.EXTRA_TEXT)){
           String imagePath, imageBaseUrl;
            imagePath = null;
            imageBaseUrl = "http://image.tmdb.org/t/p/w500/";

            TextView title = (TextView)view.findViewById(R.id.movie_title);
            TextView releaseDate = (TextView)view.findViewById(R.id.movie_releaseDate);
            TextView rating = (TextView)view.findViewById(R.id.userRating);
            TextView overview = (TextView)view.findViewById(R.id.movie_overView);
            RatingBar ratingBar = (RatingBar)view.findViewById(R.id.movieRating);
            float rat = (Float.valueOf(movie.getmrating())/2);
            String rel = movie.getReleaseDate();
            String date = rel.substring(0,4);

            //TextView runtime = (TextView)view.findViewById(R.id.runTime);
            ImageView posterImage = (ImageView)view.findViewById(R.id.movie_poster) ;
            View separator = (View)view.findViewById(R.id.separator);

                title.setText("Movie Title :" + movie.getmTitle() );
                releaseDate.setText("Release Date :" + date );
                rating.setText("Rating : " + movie.getmrating());
                ratingBar.setNumStars(5);
                ratingBar.setStepSize(1);
                ratingBar.setClickable(false);
                ratingBar.setRating(rat);
                overview.setText("Movie Plot :" + movie.getMovieOverview());
                imagePath = movie.getmposterUrl();

            Picasso.with(context).load(imageBaseUrl+imagePath).noFade().into(posterImage);
        }
        return view;


    }
}
