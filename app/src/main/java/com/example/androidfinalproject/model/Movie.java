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
    private String overview;
    private List<String> genres;
    private String release_date;
    private String poster_path;

    public Movie(@NonNull String id, String title, String overview, ArrayList<String> genres, String releaseDate, String movieImageUrl) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.genres = genres;
        this.release_date = releaseDate;
        this.poster_path = movieImageUrl;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
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
}
