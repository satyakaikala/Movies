package com.kaikala.movies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaikala.movies.R;
import com.kaikala.movies.constants.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaIkala on 8/18/2016.
 */
public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterViewHolder> {
    private final String TAG = MoviePosterAdapter.class.getSimpleName();
    private final Context context;
    private Cursor cursor;
    private ArrayList<MoviePoster> moviePosters = new ArrayList<>();
    private final MoviePosterOnClickHandler posterOnClickHandler;

    public MoviePosterAdapter(Context ctx, MoviePosterOnClickHandler clickHandler, ArrayList<MoviePoster> moviePosters) {
        context = ctx;
        posterOnClickHandler = clickHandler;
        this.moviePosters = moviePosters;
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.image_view_movie, parent, false);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        //https://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
        final String POSTER_URL = Constants.POSTER_BASE_URL + Constants.POSTER_SIZE + moviePosters.get(position).getPosterUrl();
        Log.v(TAG, "poster urls :" + POSTER_URL);
        Picasso.with(context).load(POSTER_URL.trim())
                .noFade()
                .placeholder(R.drawable.place_holder_image_2)
                .error(R.drawable.error_loading_image)
                .into(holder.posterImage);
        holder.posterTitle.setText(moviePosters.get(position).getTitle());
        holder.posterReleaseDate.setText(moviePosters.get(position).getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return moviePosters.size();
    }

    public interface MoviePosterOnClickHandler {
        void onClick(MoviePoster poster);
    }

    class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_thumbnail)
        ImageView posterImage;
        @BindView(R.id.cv)
        CardView cv;
        @BindView(R.id.poster_title)
        TextView posterTitle;
        @BindView(R.id.poster_release_date)
        TextView posterReleaseDate;

        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            posterTitle.setOnClickListener(this);
            posterReleaseDate.setOnClickListener(this);
            posterImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            posterOnClickHandler.onClick(moviePosters.get(getAdapterPosition()));
        }
    }
}