package com.example.androidfinalproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.Movie;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    static LiveData<List<Movie>> data;

    public MovieListViewModel() {
        data = Model.instance.getMovies();
    }

    public Movie getMovieByPos(int position) {
        List<Movie> movieList = data.getValue();
        Movie movie = movieList.get(position);
        return movie;
    }

    public static LiveData<List<Movie>> getData() {
        return data;
    }
}
