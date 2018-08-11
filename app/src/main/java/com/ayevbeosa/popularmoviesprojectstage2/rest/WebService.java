package com.ayevbeosa.popularmoviesprojectstage2.rest;

import com.ayevbeosa.popularmoviesprojectstage2.model.MovieResponse;
import com.ayevbeosa.popularmoviesprojectstage2.model.ReviewResponse;
import com.ayevbeosa.popularmoviesprojectstage2.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This interface specifies the endpoints and uses
 * annotations to specify the parameters and request methods
 */
public interface WebService {

    /**
     * This method specifies the Movies endpoint based on its popularity
     *
     * @param pageNo  the present page number
     * @param api_key the required key for connecting to the API
     * @return a {@link MovieResponse} {@link Call}
     */
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("page") int pageNo, @Query("api_key") String api_key);

    /**
     * This method specifies the Movies endpoint based on its rating
     *
     * @param pageNo  the present page number
     * @param api_key the required key for connecting to the API
     * @return a {@link MovieResponse} {@link Call}
     */
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("page") int pageNo, @Query("api_key") String api_key);

    /**
     * This method specifies the Movie Review endpoint
     *
     * @param id      the id of the selected movie
     * @param pageNo  the present page number
     * @param api_key the required key for connecting to the API
     * @return a {@link ReviewResponse} {@link Call}
     */
    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") String id, @Query("page") int pageNo, @Query("api_key") String api_key);

    /**
     * This method specifies the Movie Trailer endpoint
     *
     * @param id      the id of the selected movie
     * @param api_key the required key for connecting to the API
     * @return a {@link TrailerResponse} {@link Call}
     */
    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("id") String id, @Query("api_key") String api_key);
}
