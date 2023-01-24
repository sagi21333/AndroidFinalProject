package com.example.androidfinalproject.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity()
public class Review {
    public static final String COLLECTION_NAME = "reviews";
    @PrimaryKey
    @NonNull
    String documentId;
    @NonNull
    String userId;
    @NonNull
    String movieId;
    String reviewDesc;
    String movieImageUrl;
    Date updateDate;
    Boolean isDeleted;

    @Ignore
    public Review() {
        this.userId = "";
        this.movieId = "";
        this.movieImageUrl = "";
        this.updateDate = new Date();
        this.reviewDesc = "";
        this.isDeleted = false;
    }

//    public Review(String userId, String movieId, String reviewDesc, String movieImageUrl, float movieRate, Date watchDate) {
//        this.userId = userId;
//        this.movieId = movieId;
//        this.reviewDesc = reviewDesc;
//        this.movieImageUrl = movieImageUrl;
//        this.movieRate = movieRate;
//        this.watchDate = watchDate;
//        this.updateDate = new Date();
//        this.isDeleted = false;
//    }

    public Review(String documentId, String userId, String movieId, String reviewDesc,  String movieImageUrl, boolean isDeleted) {
        this.documentId = documentId;
        this.userId = userId;
        this.movieId = movieId;
        this.reviewDesc = reviewDesc;
        this.movieImageUrl = movieImageUrl;
        this.updateDate = new Date();
        this.isDeleted = isDeleted;
    }


    public static Review fromJSON(Map<String, Object> json, String documentId) {
        String userId = (String) json.get("userId");
        String movieId = (String) json.get("movieId");
        String reviewDesc = (String) json.get("reviewDesc");
        String movieImageUrl = (String) json.get("movieImageUrl");
        Boolean isDeleted = (Boolean) json.get("isDeleted");

        Review review = new Review(documentId, userId, movieId, reviewDesc, movieImageUrl, isDeleted);
        return review;
    }

    public Map<String, Object> toJSON() {
        Map<String, Object> jsonReview = new HashMap<>();
        jsonReview.put("userId", this.userId);
        jsonReview.put("movieId", this.movieId);
        jsonReview.put("reviewDesc", this.reviewDesc);
        jsonReview.put("movieImageUrl", this.movieImageUrl);
        jsonReview.put("updateDate", this.updateDate);
        jsonReview.put("isDeleted", this.isDeleted);

        return jsonReview;
    }

    @NonNull
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(@NonNull String documentId) {
        this.documentId = documentId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }

    public String getReviewDesc() {
        return reviewDesc;
    }

    public void setReviewDesc(String reviewDesc) {
        this.reviewDesc = reviewDesc;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
