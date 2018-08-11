package com.ayevbeosa.popularmoviesprojectstage2.utils;

import com.ayevbeosa.popularmoviesprojectstage2.BuildConfig;

public interface AppConstants {

    String api_key = BuildConfig.API_KEY;
    String BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    String IMAGE_SIZE = "w185/";
    String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/";
    String THUMBNAIL_QUALITY = "/hqdefault.jpg";
    String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    String review = "REVIEW";
    String trailer = "TRAILER";
}
