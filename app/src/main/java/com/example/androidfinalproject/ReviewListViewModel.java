package com.example.androidfinalproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.Movie;
import com.example.androidfinalproject.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewListViewModel extends ViewModel{
    static LiveData<List<Review>> data;
    static String movieId;

    public ReviewListViewModel(String movieId) {
        if(movieId != null){
            data = Model.instance().getReviews();
            data = Transformations.map(data, reviews -> {
                List<Review> filteredReviews = new ArrayList<>();
                for (Review review : reviews) {
                    if (review.getMovieId().equals(movieId)) {
                        filteredReviews.add(review);
                    }
                }
                return filteredReviews;
            });
            this.movieId = movieId;
        } else {
            data = Model.instance( ).getMyReviews();
            this.movieId = null;
        }

    }

    public Review getReviewByPos(int position) {
        List<Review> reviewList = data.getValue();
        Review review = reviewList.get(position);
        return review;
    }

    public static LiveData<List<Review>> getData() {
        return data;
    }
}

