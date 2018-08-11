package com.ayevbeosa.popularmoviesprojectstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class contains other information from the
 * JSON response and all fetched movie trailers
 */
public class TrailerResponse {

    @SerializedName("id")
    private Integer id;
    @SerializedName("results")
    private List<Trailer> trailerList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
    }
}
