package com.suhaib.pagination.eventbus;

import com.suhaib.pagination.entitys.Movie;

public class FavoriteMovieEvent {

    private Movie movie;

    public enum SOURCE {
        MAIN, DETAIL
    }

    private FavoriteMovieEvent(Movie movie) {
        this.movie = movie;
    }


    public static FavoriteMovieEvent getFavoriteEvent(SOURCE type, Movie movie) {

        if (type.name().equalsIgnoreCase(SOURCE.MAIN.name())) {
            return new FavoriteMovieEvent.MAIN(movie);
        } else {
            return new FavoriteMovieEvent.DETAIL(movie);
        }
    }

    public static class MAIN extends FavoriteMovieEvent {

        private MAIN(Movie movie) {
            super(movie);
        }
    }

    public static class DETAIL extends FavoriteMovieEvent {
        private DETAIL(Movie movie) {
            super(movie);
        }
    }


    public Movie getMovie() {
        return movie;
    }
}
