package com.suhaib.pagination.presenters;

import com.suhaib.pagination.entitys.Movie;

public interface DetailsView {

    void onError(Throwable e);
    void showMessage();
    void displayDetailsContent(Movie movie);
}
