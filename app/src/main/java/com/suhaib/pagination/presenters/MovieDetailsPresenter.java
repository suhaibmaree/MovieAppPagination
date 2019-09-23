package com.suhaib.pagination.presenters;

import com.suhaib.pagination.entitys.Movie;
import com.suhaib.pagination.model.MovieModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsPresenter {

    private MovieModel mModel;
    private DetailsView mView;

    public MovieDetailsPresenter(DetailsView mView) {
        this.mView = mView;
        mModel = new MovieModel();
    }// end constructor

    public void getMovie(String apiKey, int movieID) {

        mModel.callGetMovieById(apiKey, movieID).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                mView.displayDetailsContent(response.body());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

                mView.showMessage();

            }
        });

    }

    public void setMovie(Movie movie) {
        mView.displayDetailsContent(movie);
    }
}
