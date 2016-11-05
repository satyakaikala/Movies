package com.kaikala.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaikala.movies.R;

import java.util.ArrayList;

/**
 * Created by Kaikala on 11/4/16.
 */

public class MovieReviewAdapter extends BaseAdapter {

    private ArrayList<MovieReviews> movieReviewsList;
    private Context context;
    private LayoutInflater layoutInflater;

    public MovieReviewAdapter(Context context, ArrayList<MovieReviews> movieReviewsList) {
        this.context = context;
        this.movieReviewsList = movieReviewsList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return movieReviewsList.size();
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

        ReviewsHolder reviewsHolder = new ReviewsHolder();

        View reviewRow;
        reviewRow = layoutInflater.inflate(R.layout.movie_review_info, null);

        reviewsHolder.authorText = (TextView) reviewRow.findViewById(R.id.reviewAuthor);
        reviewsHolder.reviewContent = (TextView) reviewRow.findViewById(R.id.reviewContent);
        reviewsHolder.authorText.setText(movieReviewsList.get(position).getReviewAuthor());
        reviewsHolder.reviewContent.setText(movieReviewsList.get(position).getReviewContent());
        return reviewRow;
    }

    private class ReviewsHolder {

        TextView authorText;
        TextView reviewContent;
    }
}
