package com.ayevbeosa.popularmoviesprojectstage2.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class TrailerViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String id;

    public TrailerViewModelFactory(String id) {
        this.id = id;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TrailerViewModel(id);
    }
}
