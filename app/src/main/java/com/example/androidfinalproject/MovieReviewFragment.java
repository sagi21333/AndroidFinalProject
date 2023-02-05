package com.example.androidfinalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidfinalproject.databinding.FragmentMovieReviewBinding;
import com.example.androidfinalproject.databinding.FragmentReviewListBinding;
import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.Movie;
import com.example.androidfinalproject.model.MovieModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class MovieReviewFragment extends Fragment {
    FragmentMovieReviewBinding binding;
    ReviewRecyclerAdapter adapter;
    ReviewListViewModel viewModel;
    SwipeRefreshLayout swipeRefresh;
    String movieId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        String movieId = MovieReviewFragmentArgs.fromBundle(getArguments()).getMovieId();
        viewModel = new ReviewListViewModel(movieId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMovieReviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView reviewList = binding.listReviewRV;
        swipeRefresh = view.findViewById(R.id.listReviewSwipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance().refreshReviews());

        reviewList.setHasFixedSize(true);
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));

        //Get the movie id
        movieId = MovieReviewFragmentArgs.fromBundle(getArguments()).getMovieId();
        Movie movie = MovieModel.instance.getMovieById(movieId);

        adapter = new ReviewRecyclerAdapter(0);


        adapter.setOnItemClickListener(new ReviewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String documentId = viewModel.getReviewByPos(pos).getDocumentId();
                Navigation.findNavController(view).navigate(MovieReviewFragmentDirections.actionMovieReviewFragmentToSetReviewFragment(movieId, documentId, false));
            }
        });

        binding.listReviewRV.setAdapter(adapter);
        binding.movieReviewsNameTxt.setText(movie.getTitle());
        binding.movieReviewsGenresTxt.setText(movie.getMovieVoteAverage());

        reviewList.setAdapter(adapter);
        Model.instance().getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            if (loadingState == Model.LoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }
        });

        // Get all the reviews of the movie
        ReviewListViewModel.getData().observe(getViewLifecycleOwner(), list -> refresh());

        if (movie.getMovieImageUrl() != null &&
                !movie.getMovieImageUrl().equals("")) {
            Picasso.get()
                    .load(movie.getMovieImageUrl())
                    .into(binding.movieReviewsImg);
        } else {
            binding.movieReviewsImg.setImageResource(R.drawable.empty_movie);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        binding.movieReviewsReleasedateTxt.setText(movie.getReleaseDate());

//        for (String s : movie.getGenres()) {
//            binding.movieReviewsGenresTxt.append(s + "  ");
//        }

        binding.reviewMovieAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(MovieReviewFragmentDirections.actionMovieReviewFragmentToSetReviewFragment(movieId, null, false));
            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Movie reviews");

        return view;

    }
        private void refresh() {
        adapter.notifyDataSetChanged();
    }
}