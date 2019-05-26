package com.example.rottentomatoes;

public interface OnGetMovieCallback {

    void onSuccess(Movie movie);

    void onError();
}
