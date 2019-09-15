package com.suhaib.pagination.presenters;

import android.util.Log;

import com.suhaib.pagination.entitys.Movie;
import com.suhaib.pagination.entitys.MoviesResponse;
import com.suhaib.pagination.model.MovieAPIListener;
import com.suhaib.pagination.model.MovieModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Presenter {


    private MovieModel mModel;
    private MovieView mView;
    private static final String TAG = "MovieAPIListener";


    public Presenter(MovieView mView) {
        this.mView = mView;
        mModel = new MovieModel();
    }


    public void getTopMovies(String apiKey, int currentPage) {

        mModel.getTopMovies(apiKey , currentPage).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                // Got data. Send it to adapter

                if (response.isSuccessful()) {

                   mView.displayPageContent(fetchResults(response),response.body().getTotalPages());
                    Log.d(TAG, "loading Page number : " + response.body().getPage());

                } else {

                    mView.showMessage("can not load data");

                }

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

                mView.onError(t);
            }
        });;

    }

    private List<Movie> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }
}
