package com.ayevbeosa.popularmoviesprojectstage2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ayevbeosa.popularmoviesprojectstage2.database.FavouriteMovies;
import com.ayevbeosa.popularmoviesprojectstage2.database.MovieDatabase;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {

    private LiveData<List<FavouriteMovies>> favouriteMovies;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        favouriteMovies = MovieDatabase.getInstance(this.getApplication())
                .favouriteMoviesDao()
                .loadAllFavouriteMovies();
    }

    public LiveData<List<FavouriteMovies>> getFavouriteMovies() {
        return favouriteMovies;
    }
}
