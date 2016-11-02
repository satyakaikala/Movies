package com.kaikala.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaikala.movies.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by skai0001 on 10/23/16.
 */

public class MovieTraileBaseAdapter extends BaseAdapter
{

    private ArrayList<MovieTrailers> movieTrailersList;
    Context context;
    LayoutInflater layoutInflater;

    public MovieTraileBaseAdapter(Context context,ArrayList<MovieTrailers> movieTrailersList) {
        this.context=context;
        this.movieTrailersList=movieTrailersList;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return movieTrailersList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrailerHolder trailerHolder=new TrailerHolder();
        View view;
        view=layoutInflater.inflate(R.layout.movie_trailer_info,null);

        trailerHolder.iv=(ImageView)view.findViewById(R.id.trailer_image);
        trailerHolder.tv=(TextView)view.findViewById(R.id.movie_trailer_name);

        Picasso.with(context).load(movieTrailersList.get(position).getTrailerImageUrl())
                .noFade().placeholder(R.drawable.place_holder_image_1)
                .error(R.drawable.error_loading_image)
                .into(trailerHolder.iv);

        trailerHolder.tv.setText(movieTrailersList.get(position).getName());
        return view;
    }
   private class TrailerHolder
    {
        ImageView iv;
        TextView tv;
    }
}
