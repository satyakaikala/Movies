package com.kaikala.movies.operations;

import com.kaikala.movies.adapters.MoviePoster;
import com.kaikala.movies.adapters.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by skai0001 on 4/10/17.
 */

public interface MovieNetworkInterface {

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovie(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovie(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponse> getSearchResults(@Query("api_key") String apiKey, @Query("query") String query);

}
