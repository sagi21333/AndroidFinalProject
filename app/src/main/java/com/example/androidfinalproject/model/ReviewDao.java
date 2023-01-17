package com.example.androidfinalproject.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReviewDao {
    @Query("select * from Review")
    List<Review> getAllReviews();

    @Query("select * from Review where userId = :userId")
    List<Review> getMyReviews(String userId);

    @Query("select * from Review where movieId = :movieId")
    List<Review> getMovieReview(String movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Review ... reviews);

    @Delete
    void deleteReview(Review review);
}
