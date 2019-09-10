package com.delaroystudios.paginationinfinitescroll.model;

import android.util.Log;

import com.delaroystudios.paginationinfinitescroll.api.MovieClient;
import com.delaroystudios.paginationinfinitescroll.api.MovieService;
import com.delaroystudios.paginationinfinitescroll.entitys.Movie;
import com.delaroystudios.paginationinfinitescroll.entitys.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieModel {

    private MovieService movieService;
    private static final String TAG = "MovieModel";
    public String apiKey;
    int currentPage;


    public void getTopMovies(String apiKey, final MovieAPIListener listener, int currentPage) {

        this.apiKey = apiKey;
        this.currentPage = currentPage;
        movieService = MovieClient.getClient().create(MovieService.class);

        Log.d(TAG, "loading new Page");

        callTopRatedMoviesApi().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                // Got data. Send it to adapter

                if (response.isSuccessful()) {

                    listener.onSuccess(response);
                    Log.d(TAG, "loading Page number : " + response.body().getPage());

                } else {
                    listener.onError(response);

                }

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                listener.onFailure(t);

            }
        });
    }

    private Call<MoviesResponse> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies(
                apiKey,
                currentPage
        );
    }


}//end movie model class


