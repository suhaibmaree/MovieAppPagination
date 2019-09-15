package com.suhaib.pagination.model;

import android.util.Log;

import com.suhaib.pagination.api.MovieClient;
import com.suhaib.pagination.api.MovieService;
import com.suhaib.pagination.entitys.MoviesResponse;

import retrofit2.Call;
public class MovieModel {

    private static final String TAG = "MovieModel";

    public static Call<MoviesResponse> getTopMovies(String apiKey, int currentPage) {
        Log.d(TAG, "loading new Page");

        return callTopRatedMoviesApi(apiKey, currentPage);
    }

    private static Call<MoviesResponse> callTopRatedMoviesApi(String apiKey, int currentPage) {
        return MovieClient.getClient().create(MovieService.class).getTopRatedMovies(
                apiKey,
                currentPage
        );
    }


}//end movie model class


