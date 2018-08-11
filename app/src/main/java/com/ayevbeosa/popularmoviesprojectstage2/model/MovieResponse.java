package com.ayevbeosa.popularmoviesprojectstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class contains other information from the
 * JSON response and all fetched movies
 */
public class MovieResponse {

    @SerializedName("page")
    private String page;
    @SerializedName("total_pages")
    private Integer total_pages;
    @SerializedName("results")
    private List<Movie> movieList;


    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }
}
