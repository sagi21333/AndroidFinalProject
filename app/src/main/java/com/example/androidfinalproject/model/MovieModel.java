package com.example.androidfinalproject.model;

import java.net.HttpCookie;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MovieModel {
    final public static MovieModel instance = new MovieModel();
    static MutableLiveData<List<Movie>> movieList;

    final String BASE_URL = "https://api.themoviedb.org/3/";
    Retrofit retrofit;
    MovieApi movieApi;

    MutableLiveData<Model.LoadingState> loadingState = new MutableLiveData<>();

    private MovieModel() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        movieApi = retrofit.create(MovieApi.class);

        movieList = new MutableLiveData<List<Movie>>();
        loadingState.setValue(Model.LoadingState.loaded);
    }

//    public LiveData<List<Movie>> getMovies() {
//        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
//        Call<MoviesResponse> call = movieApi.getPopularMovies("f314f0faba46f8dfcb54ae84d8f584a2", "en-US", 1);
//
//        call.enqueue(new Callback<MoviesResponse>() {
//            @Override
//            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
//                if (response.isSuccessful()) {
//                    MoviesResponse res = response.body();
//                    data.setValue(res.getResults());
//                } else {
//                    Log.d("TAG", "----- searchMoviesByTitle response error");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MoviesResponse> call, Throwable t) {
//                Log.d("TAG", "----- searchMoviesByTitle fail");
//            }
//        });
//        return data;
//    }

    public Movie getMovieById(String MovieId) {

        LiveData<List<Movie>> moviesList = MovieModel.movieList;
        Movie movie = moviesList.getValue().stream().filter(rev -> rev.getId().equals(MovieId)).findFirst().orElse(null);
        return movie;
    }

//    public Movie getMovieById(String movieId) {
//        final MutableLiveData<Movie> movie = new MutableLiveData<>();
//        movieApi.getMovieById("f314f0faba46f8dfcb54ae84d8f584a2", movieId,"en-US").enqueue(new Callback<Movie>() {
//            @Override
//            public void onResponse(Call<Movie> call, Response<Movie> response) {
//                if (response.isSuccessful()) {
//                    movie.setValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Movie> call, Throwable t) {
//                Log.d("TAG", "----- getMovieById fail");
//            }
//        });
//        return movie.getValue();
//    }



    public LiveData<List<Movie>> loadMovies() {
        loadingState.setValue(Model.LoadingState.loading);

        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        movieApi.getPopularMovies("f314f0faba46f8dfcb54ae84d8f584a2", "en-US", 1).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse res = response.body();
                    data.setValue(res.getResults());
                    movieList.postValue(res.getResults());

                } else {
                    Log.d("TAG", "----- searchMoviesByTitle response error");
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.d("TAG", "----- searchMoviesByTitle fail");
            }
        });


        loadingState.postValue(Model.LoadingState.loaded);

        return data;
    }
}


