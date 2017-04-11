package com.kaikala.movies.adapters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by skai0001 on 4/10/17.
 */

public class MovieResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private ArrayList<MoviePoster> results;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("totalPages")
    private int totalPages;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<MoviePoster> getResults() {
        return results;
    }

    public void setResults(ArrayList<MoviePoster> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
