package com.example.androidfinalproject.model;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidfinalproject.MyApplication;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Model {
    private static final Model instance = new Model();
    Executor executor = Executors.newFixedThreadPool(1);
    MutableLiveData<List<Review>> reviewList;
    MutableLiveData<List<Review>> myReviews;
//    MutableLiveData<List<Movie>> movieList;

    ModelFirebase modelFirebase;
    private String avatarLocation = "users_avatars/";
    private String movieLocation = "users_movies/";

    MutableLiveData<LoadingState> loadingState = new MutableLiveData<>();

    private Model() {
        reviewList = new MutableLiveData<List<Review>>();
        myReviews = new MutableLiveData<List<Review>>();
//        movieList = new MutableLiveData<List<Movie>>();
        modelFirebase = new ModelFirebase();
        loadingState.setValue(LoadingState.loaded);
    }

    public static Model instance(){
        return instance;
    }

    public enum LoadingState {
        loading,
        loaded
    }

    public LiveData<List<Review>> getReviews() {
        if (reviewList.getValue() == null) {
            refreshReviews();
        }

        return reviewList;
    }

    public LiveData<List<Review>> getMyReviews() {
        if (myReviews.getValue() == null) {
            refreshReviews();
        }

        return myReviews;
    }

//    public LiveData<List<Movie>> getMovies() {
//        if (movieList.getValue() == null) {
//            getAllMovies();
//        }
//
//        return movieList;
//    }

    public void deleteReview(String reviewKey, ModelFirebase.DeleteReviewListener listener) {
        modelFirebase.deleteReview(reviewKey, () -> {
            listener.onComplete();
            refreshReviews();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Review getReviewById(String documentId) {
        List<Review> reviewsList = reviewList.getValue();
        Review review = reviewsList.stream().filter(rev -> rev.getDocumentId().equals(documentId)).findFirst().orElse(null);
        return review;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Review> getReviewsMovie(String movieId) {
        List<Review> reviewsList = reviewList.getValue();
        List<Review> reviewlistMovie = (List<Review>) reviewsList.stream().map(rev -> rev.getMovieId().equals(movieId));
        return reviewlistMovie;
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public Movie getMovieById(String MovieId) {
//
//        List<Movie> moviesList = movieList.getValue();
//        Movie movie = moviesList.stream().filter(rev -> rev.getMovieId().equals(MovieId)).findFirst().orElse(null);
//        return movie;
//    }

    public MutableLiveData<LoadingState> getLoadingState() {
        return loadingState;
    }

//    public void getAllMovies() {
//        loadingState.setValue(LoadingState.loading);
//
//        //Todo: after taking it from api remove the hardcoded array
//        ArrayList<Movie> movies = new ArrayList<Movie>();
//        ArrayList<String> geners = new ArrayList<String>();
//        geners.add("drama");
//        for (int i =0; i<10; i++) {
//            Movie movie = new Movie("" + i, "movie"+i, "bla", geners,new Date (), "");
//            movies.add(movie);
//        }
//
//        //Todo: get all the movies from the api and localdb
//
//        movieList.postValue(movies);
//        loadingState.postValue(LoadingState.loaded);
//    }
    public void refreshReviews() {

        loadingState.setValue(LoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("LastUpdateDate", 0);

        // Get all reviews from local db
        executor.execute(() -> {
            List<Review> reviews;
            reviews = AppLocalDb.db.reviewDao().getAllReviews();
            reviewList.postValue(reviews);
            reviews = AppLocalDb.db.reviewDao().getMyReviews(getUserEmail());
            myReviews.postValue(reviews);
        });

        // Get all reviews from firebase
        modelFirebase.getReviews(new Date(lastUpdateDate), new ModelFirebase.GetReviewsListener() {
            @Override
            public void onComplete(List<Review> list) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);

                        for (Review review : list) {
                            if (review.getIsDeleted()) {
                                AppLocalDb.db.reviewDao().deleteReview(review);
                            } else {
                                AppLocalDb.db.reviewDao().insertAll(review);
                            }
                            if (review.getUpdateDate() != null &&
                                    review.getUpdateDate().getTime() < lud) {
                                lud = review.getUpdateDate().getTime();
                            }
                        }

                        // Update last update date local
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("LastUpdateDate", lud)
                                .commit();

                        List<Review> reviews;
                        reviews = AppLocalDb.db.reviewDao().getAllReviews();
                        reviewList.postValue(reviews);
                        reviews = AppLocalDb.db.reviewDao().getMyReviews(getUserEmail());
                        myReviews.postValue(reviews);
                        loadingState.postValue(LoadingState.loaded);
                    }
                });
            }
        });
    }

    public void setReview(Review review, String documentId, ModelFirebase.SetReviewListener listener) {
        modelFirebase.setReview(review, documentId, () -> {
            listener.onComplete();
            refreshReviews();
        });
    }

    public void register(String email, String password, ModelFirebase.Register listener) {
        modelFirebase.register(email, password, listener);
    }

    public void updatePassword(String password, ModelFirebase.UpdatePassword listener) {
        modelFirebase.updatePassword(password, listener);
    }

    public void setUserDetails(String email, String firstName, String lastName, String imageUrl, ModelFirebase.RegisterDetails listener) {
        modelFirebase.setUserDetails(email, firstName, lastName, imageUrl, listener);
    }

    public void signIn(String email, String password, ModelFirebase.SignIn listener) {
        modelFirebase.signIn(email, password, listener);
    }

    public String getUserEmail() {
        return modelFirebase.getUserEmail();
    }

    public void getUserDetails(ModelFirebase.GetUserDetailsListener listener) {
        modelFirebase.getUserDetails(listener);
    }

    public void saveImageAvatar(Bitmap imageBitmap, String imageName, ModelFirebase.SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener, avatarLocation);
    }

    public void saveMovieImage(Bitmap imageBitmap, String imageName, ModelFirebase.SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener, movieLocation);
    }

    public void deleteMovieImage(String movieImageUrl, ModelFirebase.DeleteImageListener listener) {
        modelFirebase.deleteImage(movieImageUrl, listener);
    }

    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

    public void logout() {
        reviewList.postValue(null);
        myReviews.postValue(null);
        modelFirebase.logout();
    }
}

