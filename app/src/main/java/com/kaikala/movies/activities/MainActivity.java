package com.kaikala.movies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.kaikala.movies.fragments.FragmentMoviesList;
import com.kaikala.movies.R;
import com.kaikala.movies.fragments.MovieDetailFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_poster_list, new FragmentMoviesList()).commit();

            if (findViewById(R.id.fragment_poster_detail) != null) {
               getSupportFragmentManager().beginTransaction().add(R.id.fragment_poster_detail, new MovieDetailFragment());
            }
        }

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
