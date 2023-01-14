package com.example.androidfinalproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.Movie;
import com.example.androidfinalproject.model.Review;

import java.util.List;

public class ReviewListViewModel extends ViewModel{
    static LiveData<List<Review>> data;

    public ReviewListViewModel(String movieId) {
        if(movieId != null){
            data = Model.instance.getReviews();
        } else {
            data = Model.instance.getMyReviews();
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

