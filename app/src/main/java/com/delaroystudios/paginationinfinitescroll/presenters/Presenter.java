package com.delaroystudios.paginationinfinitescroll.presenters;

import android.util.Log;

import com.delaroystudios.paginationinfinitescroll.Views.MainActivity;
import com.delaroystudios.paginationinfinitescroll.entitys.Movie;
import com.delaroystudios.paginationinfinitescroll.entitys.MoviesResponse;
import com.delaroystudios.paginationinfinitescroll.model.MovieAPIListener;
import com.delaroystudios.paginationinfinitescroll.model.MovieModel;

import java.util.List;

import retrofit2.Response;

public class Presenter implements MovieAPIListener {


    private MovieModel mModel;
    private MainActivity mView;
    int currentPage;

    public Presenter(MainActivity mView, int currentPage) {
        this.mView = mView;
        mModel = new MovieModel();
        this.currentPage = currentPage;
        getTopMovies(mView.getAPIKey());
    }


    public void getTopMovies(String apiKey) {

        mModel.getTopMovies(apiKey, this, currentPage);

    }

    private List<Movie> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }


    @Override
    public void onSuccess(Response<MoviesResponse> response) {

        mView.loadPage(
                fetchResults(response),
                response.body().getTotalPages());
    }

    @Override
    public void onError(Response<MoviesResponse> response) {

        mView.showMessage("Error Occured.");

    }

    @Override
    public void onFailure(Throwable t) {

        mView.showMessage(t.getMessage());

    }
}
