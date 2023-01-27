package com.example.androidfinalproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.Movie;
import com.example.androidfinalproject.model.MovieModel;
import com.example.androidfinalproject.model.MoviesResponse;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    static LiveData<List<Movie>> data;

    public MovieListViewModel() {
        data = MovieModel.instance.loadMovies();

    }

    public Movie getMovieByPos(int position) {
        List<Movie> movieList = data.getValue();
        Movie movie = movieList.get(position);
        return movie;
    }

    public static LiveData<List<Movie>> getData() {
        return data;
    }

    //    public Movie getMovieById(String MovieId) {
//
//        LiveData<List<Movie>> moviesList = MovieModel.instance.loadMovies();
//        Movie movie = moviesList.getValue().stream().filter(rev -> rev.getId().equals(MovieId)).findFirst().orElse(null);
//        return movie;
//    }

}


