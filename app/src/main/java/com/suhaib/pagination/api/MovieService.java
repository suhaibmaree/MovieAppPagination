package com.suhaib.pagination.api;

import com.suhaib.pagination.entitys.Movie;
import com.suhaib.pagination.entitys.MoviesResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MovieService {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);


    @GET("movie/{movieId}")
    Call<Movie> getMovieById(@Path("movieId") int key ,@Query("api_key") String apiKey );

}
