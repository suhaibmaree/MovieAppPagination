package com.suhaib.pagination.model;

import com.suhaib.pagination.entitys.MoviesResponse;

import retrofit2.Response;

public interface MovieAPIListener {

    void onSuccess(Response<MoviesResponse> response);

    void onError(Response<MoviesResponse> response);

    void onFailure(Throwable t);
}
