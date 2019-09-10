package com.delaroystudios.paginationinfinitescroll.model;

import android.util.Log;

import com.delaroystudios.paginationinfinitescroll.api.Client;
import com.delaroystudios.paginationinfinitescroll.api.Service;
import com.delaroystudios.paginationinfinitescroll.entity.Movie;
import com.delaroystudios.paginationinfinitescroll.entity.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieModel{

    private Service movieService;
    private static final String TAG = "MovieModel";
    String apiKey;
    int currentPage;


    public void getTopMovies(String apiKey, final MovieAPIListener listener, int currentPage){

        this.apiKey = apiKey;
        this.currentPage = currentPage;
        movieService = Client.getClient().create(Service.class);

        Log.d(TAG, "loadFirstPage: ");

        callTopRatedMoviesApi().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                // Got data. Send it to adapter

                if(response.isSuccessful()){

                    listener.onSuccess(response);

                }else {
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



    private List<Movie> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse topRatedMovies = response.body();
        Log.d(TAG,"Total pages: " + response.body().getTotalPages() );
        return topRatedMovies.getResults();
    }


}//end movie model class


