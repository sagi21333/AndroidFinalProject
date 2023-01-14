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

import com.example.androidfinalproject.databinding.FragmentMovieReviewBinding;
import com.example.androidfinalproject.databinding.FragmentReviewListBinding;
import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class MovieReviewFragment extends Fragment {

//    ImageView movieImg;
//    TextView name;
//    TextView director;
//    TextView releasedate;

    FragmentMovieReviewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMovieReviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String movieId = MovieReviewFragmentArgs.fromBundle(getArguments()).getMovieId();
        Movie movie = Model.instance.getMovieById(movieId);

        binding.movieReviewsNameTxt.setText(movie.getTitle());

        if (movie.getMovieImageUrl() != null &&
                !movie.getMovieImageUrl().equals("")) {
            Picasso.get()
                    .load(movie.getMovieImageUrl())
                    .into(binding.movieReviewsImg);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        binding.movieReviewsReleasedateTxt.setText(dateFormat.format(movie.getReleaseDate()));

        return view;

    }

}