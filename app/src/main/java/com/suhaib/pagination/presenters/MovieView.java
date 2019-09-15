package com.suhaib.pagination.presenters;

import com.suhaib.pagination.entitys.Movie;

import java.util.List;

public interface MovieView {

    void onError (Throwable e);

    void displayPageContent (List<Movie> movies, int total);

    void showMessage(String s);



}
