package com.delaroystudios.paginationinfinitescroll.api;

import com.delaroystudios.paginationinfinitescroll.entitys.MoviesResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MovieService {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);

}
