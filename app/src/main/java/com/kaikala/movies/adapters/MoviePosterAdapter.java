package com.kaikala.movies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.kaikala.movies.R;
import com.kaikala.movies.constants.Constants;
import com.kaikala.movies.data.MovieContract;
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

    void setCursor(Cursor c) {
        this.cursor = c;
        notifyDataSetChanged();
    }

    String getPosterAtPosition(int position) {
        cursor.moveToPosition(position);
        return cursor.getString(MovieContract.MovieEntry.COL_MOVIE_POSTER_PATH);
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.image_view_movie, parent, false);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
      //  cursor.moveToPosition(position);

        MoviePoster moviePoster = moviePosters.get(position);
        //https://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
        final String POSTER_URL = Constants.POSTER_BASE_URL + Constants.POSTER_SIZE + moviePoster.getmposterUrl();
        Log.v(TAG, "poster urls :" + POSTER_URL);
        Picasso.with(context).load(POSTER_URL.trim())
                .noFade()
                .placeholder(R.drawable.place_holder_image_2)
                .error(R.drawable.error_loading_image)
                .into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
        }
        return moviePosters.size();
    }

    public interface MoviePosterOnClickHandler {
        void onClick(String poster);
    }

    class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_thumbnail)
        ImageView posterImage;
        @BindView(R.id.cv)
        CardView cv;

        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            int posterColumn = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH);
            posterOnClickHandler.onClick(cursor.getString(posterColumn));
        }
    }
}