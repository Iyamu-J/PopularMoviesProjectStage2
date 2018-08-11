package com.ayevbeosa.popularmoviesprojectstage2.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ayevbeosa.popularmoviesprojectstage2.rest.MovieRepository;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository movieRepository;

    public ViewModelFactory(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

//    @SuppressWarnings("unchecked")
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(MovieViewModel.class)) {
//            return (T) new MovieViewModel(movieRepository);
//        } else if (modelClass.isAssignableFrom(FavouritesViewModel.class)) {
//            return (T) new FavouritesViewModel(movieRepository);
//        } else if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
//            return (T) new FavouritesViewModel(movieRepository);
//        } else {
//            return super.create(modelClass);
//        }
//    }
}
