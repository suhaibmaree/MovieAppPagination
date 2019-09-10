package com.delaroystudios.paginationinfinitescroll.presenter;

import com.delaroystudios.paginationinfinitescroll.MainActivity;
import com.delaroystudios.paginationinfinitescroll.entity.MoviesResponse;
import com.delaroystudios.paginationinfinitescroll.model.MovieAPIListener;
import com.delaroystudios.paginationinfinitescroll.model.MovieModel;

import retrofit2.Response;

public class Presenter implements MovieAPIListener {


    MovieModel mModel;
    MainActivity mView;
    int currentPage;

    public Presenter(MainActivity mView, int currentPage) {
        this.mView = mView;
        mModel = new MovieModel();
        this.currentPage = currentPage;
        getTopMovies(mView.getAPIKey());
    }


    public void getTopMovies(String apiKey) {

        mModel.getTopMovies(apiKey, this,currentPage);

    }


    @Override
    public void onSuccess(Response<MoviesResponse> response) {

        mView.loadPage(response.body().getResults(),response.body().getTotalPages());
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
