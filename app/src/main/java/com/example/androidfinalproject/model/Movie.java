package com.example.androidfinalproject.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String release_date;
    private String poster_path;
    private String vote_average;

    public Movie(@NonNull String id, String title, String releaseDate, String movieImageUrl, String voteAverage) {
        this.id = id;
        this.title = title;
        this.release_date = releaseDate;
        this.poster_path = movieImageUrl;
        this.vote_average = voteAverage;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

    public String getMovieImageUrl() {
        return poster_path;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.poster_path = movieImageUrl;
    }

    public String getMovieVoteAverage() {
        return vote_average;
    }

    public void setMovieVoteAverage(String voteAverage) {
        this.vote_average = voteAverage;
    }
}
