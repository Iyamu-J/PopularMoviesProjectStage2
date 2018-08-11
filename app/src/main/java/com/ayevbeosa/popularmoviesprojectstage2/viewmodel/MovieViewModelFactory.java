package com.ayevbeosa.popularmoviesprojectstage2.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private int currentPage;

    public MovieViewModelFactory(int currentPage) {
        this.currentPage = currentPage;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(currentPage);
    }
}
