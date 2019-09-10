package com.delaroystudios.paginationinfinitescroll.model;

import com.delaroystudios.paginationinfinitescroll.entitys.MoviesResponse;

import retrofit2.Response;

public interface MovieAPIListener {

    void onSuccess(Response<MoviesResponse> response);

    void onError(Response<MoviesResponse> response);

    void onFailure(Throwable t);
}
