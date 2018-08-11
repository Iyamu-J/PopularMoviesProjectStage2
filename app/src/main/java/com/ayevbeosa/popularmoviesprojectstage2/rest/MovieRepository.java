package com.ayevbeosa.popularmoviesprojectstage2.rest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ayevbeosa.popularmoviesprojectstage2.model.MovieResponse;
import com.ayevbeosa.popularmoviesprojectstage2.model.ReviewResponse;
import com.ayevbeosa.popularmoviesprojectstage2.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ayevbeosa.popularmoviesprojectstage2.utils.AppConstants.api_key;

public class MovieRepository {

    // an instance of MovieRepository
    private static MovieRepository sInstance;

    private final static String TAG = MovieRepository.class.getSimpleName();

    /**
     * Returns an instance of {@link MovieRepository}
     *
     * @return MovieRepository
     */
    public static MovieRepository getInstance() {
        if (sInstance == null) {
            Log.d(TAG, "MovieRepository instance created");
            sInstance = new MovieRepository();
        }
        return sInstance;
    }

    /**
     * Retrieves the Movies based on its popularity
     *
     * @param currentPage required page number
     * @return a {@link LiveData} of {@link MovieResponse} {@link Response}
     */
    public LiveData<Response<MovieResponse>> getMoviesResponse(int currentPage) {
        final MutableLiveData<Response<MovieResponse>> moviesResponse = new MutableLiveData<>();
        Call<MovieResponse> call = callPopularMovies(currentPage);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                Log.d(TAG, "PopularMovieResponse: " + response.body());
                moviesResponse.setValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "PopularMovieResponse Error: " + t.getLocalizedMessage());
            }
        });
        return moviesResponse;
    }

    /**
     * Retrieves the Movies based on its rating
     *
     * @param currentPage required page number
     * @return a {@link LiveData} of {@link MovieResponse} {@link Response}
     */
    public LiveData<Response<MovieResponse>> getTopRatedMovies(int currentPage) {
        final MutableLiveData<Response<MovieResponse>> moviesResponse = new MutableLiveData<>();
        Call<MovieResponse> call = callTopRatedMovies(currentPage);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                Log.d(TAG, "RatedMovieResponse: " + response.body());
                moviesResponse.setValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "PopularMovieResponse Error: " + t.getLocalizedMessage());
            }
        });
        return moviesResponse;
    }

    /**
     * Retrieves the Movie Review(s)
     *
     * @param id          the required Movie Id
     * @param currentPage required page number
     * @return a {@link LiveData} of {@link ReviewResponse} {@link Response}
     */
    public LiveData<Response<ReviewResponse>> getReviewResponse(String id, int currentPage) {
        final MutableLiveData<Response<ReviewResponse>> reviewResponse = new MutableLiveData<>();
        Call<ReviewResponse> call = callMovieReviews(id, currentPage);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                Log.d(TAG, "Movie review responded");
                reviewResponse.setValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "MovieReviews: " + t.getLocalizedMessage());
            }
        });
        return reviewResponse;
    }

    /**
     * Retrieves the Movie Trailer(s)
     *
     * @param id the required Movie Id
     * @return a {@link LiveData} of {@link TrailerResponse} {@link Response}
     */
    public LiveData<Response<TrailerResponse>> getTrailerResponse(String id) {
        final MutableLiveData<Response<TrailerResponse>> trailerResponse = new MutableLiveData<>();
        Call<TrailerResponse> call = callMovieTrailers(id);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                Log.d(TAG, "Movie Trailer responded");
                trailerResponse.setValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "MovieTrailer: " + t.getLocalizedMessage());
            }
        });
        return trailerResponse;
    }

    /**
     * Makes direct call to the API to get the Movies based on its popularity
     *
     * @param currentPage required page number
     * @return a {@link MovieResponse} {@link Call}
     */
    private Call<MovieResponse> callPopularMovies(int currentPage) {
        return ApiClient.getRetrofit().create(WebService.class).getPopularMovies(currentPage, api_key);
    }

    /**
     * Makes direct call to the API to get the Movies based on its rating
     *
     * @param currentPage required page number
     * @return a {@link MovieResponse} {@link Call}
     */
    private Call<MovieResponse> callTopRatedMovies(int currentPage) {
        return ApiClient.getRetrofit().create(WebService.class).getTopRatedMovies(currentPage, api_key);
    }

    /**
     * Makes direct call to the API to get the Movies Reviews
     *
     * @param id          the required Movie Id
     * @param currentPage the page number
     * @return a {@link ReviewResponse} {@link Call}
     */
    private Call<ReviewResponse> callMovieReviews(String id, int currentPage) {
        return ApiClient.getRetrofit().create(WebService.class).getMovieReviews(id, currentPage, api_key);
    }

    /**
     * Makes a direct call to the API to get the Movies Trailers
     *
     * @param id the id of the selected movie
     * @return a {@link TrailerResponse} {@link Call}
     */
    private Call<TrailerResponse> callMovieTrailers(String id) {
        return ApiClient.getRetrofit().create(WebService.class).getMovieTrailers(id, api_key);
    }
}
