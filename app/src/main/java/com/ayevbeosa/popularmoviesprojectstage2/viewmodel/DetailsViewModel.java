package com.ayevbeosa.popularmoviesprojectstage2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ayevbeosa.popularmoviesprojectstage2.database.FavouriteMovies;
import com.ayevbeosa.popularmoviesprojectstage2.database.MovieDatabase;

public class DetailsViewModel extends ViewModel {

    private LiveData<FavouriteMovies> favouriteMovies;

    DetailsViewModel(MovieDatabase database, int id) {
        favouriteMovies = database.favouriteMoviesDao().loadFavouriteMovieById(id);
    }

    public LiveData<FavouriteMovies> getFavouriteMovies() {
        return favouriteMovies;
    }
}
