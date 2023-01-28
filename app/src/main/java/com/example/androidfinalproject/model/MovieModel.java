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
    MutableLiveData<List<Movie>> data = new MutableLiveData<List<Movie>>();

    final String BASE_URL = "https://api.themoviedb.org/3/";
    Retrofit retrofit;
    MovieApi movieApi;

    private MovieModel() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        movieApi = retrofit.create(MovieApi.class);

    }


    public Movie getMovieById(String MovieId) {

        Movie movie = data.getValue().stream().filter(rev -> rev.getId().equals(MovieId)).findFirst().orElse(null);
        return movie;
    }

    public LiveData<List<Movie>> loadMovies() {
        Model.instance().loadingState.setValue(Model.LoadingState.loading);

        Call<MoviesResponse> call = movieApi.getPopularMovies("f314f0faba46f8dfcb54ae84d8f584a2", "en-US", 1);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()){
                    Log.d("tag", response.body().toString());
                    MoviesResponse res = response.body();
                    data.setValue(res.getResults());

                }else{
                    Log.d("TAG","----- searchMoviesByTitle response error");
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.d("TAG","----- searchMoviesByTitle fail");
            }
        });

        Model.instance().loadingState.postValue(Model.LoadingState.loaded);

        return data;
    }

    public void refreshMovies() {
        loadMovies();
    }

}


