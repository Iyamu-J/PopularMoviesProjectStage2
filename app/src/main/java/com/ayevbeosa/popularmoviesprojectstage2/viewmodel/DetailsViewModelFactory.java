package com.ayevbeosa.popularmoviesprojectstage2.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ayevbeosa.popularmoviesprojectstage2.database.MovieDatabase;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private MovieDatabase movieDatabase;
    private int movieId;

    public DetailsViewModelFactory(MovieDatabase database, int movieId) {
        this.movieDatabase = database;
        this.movieId = movieId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(movieDatabase, movieId);
    }
}
