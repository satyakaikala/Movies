package com.kaikala.movies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kaIkala on 8/18/2016.
 */
public class ImageAdapter extends ArrayAdapter<MoviePoster> {
    private final String TAG = ImageAdapter.class.getSimpleName();
    private Context context;
    ArrayList<MoviePoster> moviePosters = new ArrayList<>();

    public ImageAdapter(Context ctx, ArrayList<MoviePoster> posters) {
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
        final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
        final String POSTER_SIZE = "w185";
        final String POSTER_URL = POSTER_BASE_URL + POSTER_SIZE + image.getmposterUrl();
        Log.v(TAG, "poster urls :" + POSTER_URL);
        Picasso.with(context).load(POSTER_URL.trim()).noFade().resize(185 * 2, 270 * 2).into(imageView);
        return imageView;
    }

    public void clear() {
        moviePosters.clear();
    }
}