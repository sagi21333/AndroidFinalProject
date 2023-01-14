package com.example.androidfinalproject;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.Movie;

import org.w3c.dom.Text;

public class MovieReviewFragment extends Fragment {

    ImageView movieImg;
    TextView name;
    TextView director;
    TextView releasedate;

//    Bundle bundle = new Bundle();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_review, container, false);

        String movieId = MovieReviewFragmentArgs.fromBundle(getArguments()).getMovieId();
        Movie movie = Model.instance.getMovieById(movieId);

//        bundle.putString("movieId", movieId);
//        FragmentContainerView fragmentContainerView = (FragmentContainerView) view.findViewById(R.id.fragmentContainerView);
//        ReviewListFragment reviewListFragment = (ReviewListFragment) fragmentContainerView.getFragment();
//
//        if(reviewListFragment!=null) reviewListFragment.setArguments(bundle);


        return view;

    }

}