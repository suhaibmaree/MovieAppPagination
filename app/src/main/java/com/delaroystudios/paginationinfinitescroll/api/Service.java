package com.delaroystudios.paginationinfinitescroll.api;

import com.delaroystudios.paginationinfinitescroll.entity.MoviesResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Service {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);

}
