package com.ayevbeosa.popularmoviesprojectstage2.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ReviewsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String id;
    private int currentPage;

    public ReviewsViewModelFactory(String id, int currentPage) {
        this.id = id;
        this.currentPage = currentPage;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReviewsViewModel(id, currentPage);
    }
}
