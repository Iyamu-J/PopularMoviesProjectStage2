package com.ayevbeosa.popularmoviesprojectstage2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ayevbeosa.popularmoviesprojectstage2.model.TrailerResponse;
import com.ayevbeosa.popularmoviesprojectstage2.rest.MovieRepository;

import retrofit2.Response;

public class TrailerViewModel extends ViewModel {

    private LiveData<Response<TrailerResponse>> trailerResponse;

    TrailerViewModel(String id) {
        trailerResponse = MovieRepository.getInstance().getTrailerResponse(id);
    }

    public LiveData<Response<TrailerResponse>> getTrailerResponse() {
        return trailerResponse;
    }
}
