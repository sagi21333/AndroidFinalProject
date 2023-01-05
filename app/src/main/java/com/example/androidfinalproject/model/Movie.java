package com.example.androidfinalproject.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.Date;

public class Movie {
    @PrimaryKey
    @NonNull
    String movieId;
    String title;
    String overview;
    String [] genres; //Not sure which type is
    String originalTitle;
    Float movieRate;
    Date releaseDate;

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

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Float getMovieRate() {
        return movieRate;
    }

    public void setMovieRate(Float movieRate) {
        this.movieRate = movieRate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
