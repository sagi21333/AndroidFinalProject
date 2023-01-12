package com.example.androidfinalproject.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

public class Movie {
    @PrimaryKey
    @NonNull
    String movieId;
    String title;
    String overview;
    ArrayList<String> genres = new ArrayList<String>(); //Not sure which type is
    Date releaseDate;
    String movieImageUrl;

    public Movie(@NonNull String movieId, String title, String overview, ArrayList<String> genres, Date releaseDate, String movieImageUrl) {
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.genres = genres;
        this.releaseDate = releaseDate;
        this.movieImageUrl = movieImageUrl;
    }

    @NonNull
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }
}
