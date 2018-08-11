package com.ayevbeosa.popularmoviesprojectstage2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ayevbeosa.popularmoviesprojectstage2.model.ReviewResponse;
import com.ayevbeosa.popularmoviesprojectstage2.rest.MovieRepository;

import retrofit2.Response;

public class ReviewsViewModel extends ViewModel {

    private LiveData<Response<ReviewResponse>> reviewResponse;

    ReviewsViewModel(String id, int currentPage) {
        reviewResponse = MovieRepository.getInstance().getReviewResponse(id, currentPage);
    }

    public LiveData<Response<ReviewResponse>> getReviewResponse() {
        return reviewResponse;
    }
}
