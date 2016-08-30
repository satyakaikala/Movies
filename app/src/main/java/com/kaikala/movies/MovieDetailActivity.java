package com.kaikala.movies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.moviePosterContainer, new MovieDetailFragment()).commit();
        }
    }
}
