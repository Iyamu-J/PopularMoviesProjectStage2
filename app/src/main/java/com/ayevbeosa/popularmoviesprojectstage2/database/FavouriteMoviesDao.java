package com.ayevbeosa.popularmoviesprojectstage2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavouriteMoviesDao {

    @Query("SELECT * FROM favouritemovies ORDER BY release_date")
    LiveData<List<FavouriteMovies>> loadAllFavouriteMovies();

    @Query("SELECT * FROM favouritemovies WHERE id = :id")
    LiveData<FavouriteMovies> loadFavouriteMovieById(int id);

    @Query("SELECT * FROM favouritemovies WHERE movieId = :movieId")
    FavouriteMovies loadFavouriteMovieByMovieId(int movieId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addFavouriteMovie(FavouriteMovies favouriteMovies);

    @Query("DELETE FROM favouritemovies WHERE id = :id")
    void deleteFavouriteMovieById(int id);
}