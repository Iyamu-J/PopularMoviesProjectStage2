package com.ayevbeosa.popularmoviesprojectstage2.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {FavouriteMovies.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "movies.db";
    private static final String TAG = MovieDatabase.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context, MovieDatabase.class, DATABASE_NAME)
                        .build();
                Log.d(TAG, "New Database created");
            }
        }
        Log.d(TAG, "Getting the Database instance");
        return sInstance;
    }

    public abstract FavouriteMoviesDao favouriteMoviesDao();
}
