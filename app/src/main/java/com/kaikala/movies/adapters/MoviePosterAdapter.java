package com.kaikala.movies.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.kaikala.movies.R;
import com.kaikala.movies.constants.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kaIkala on 8/18/2016.
 */
public class MoviePosterAdapter extends ArrayAdapter<MoviePoster> {
    private final String TAG = MoviePosterAdapter.class.getSimpleName();
    private Context context;
    ArrayList<MoviePoster> moviePosters = new ArrayList<>();

    public MoviePosterAdapter(Context ctx, ArrayList<MoviePoster> posters) {
        super(ctx, 0, posters);

        context = ctx;
        this.moviePosters = posters;
    }


    @Override
    public int getCount() {
        return moviePosters.size();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        MoviePoster image = getItem(position);
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        //https://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
        final String POSTER_URL = Constants.POSTER_BASE_URL + Constants.POSTER_SIZE + image.getmposterUrl();
        Log.v(TAG, "poster urls :" + POSTER_URL);
        Picasso.with(context).load(POSTER_URL.trim())
                .noFade()
                .placeholder(R.drawable.place_holder_image_2)
                .error(R.drawable.error_loading_image)
                .into(imageView);
        return imageView;
    }

    public void clear() {
        moviePosters.clear();
    }
}