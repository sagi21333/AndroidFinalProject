package com.example.androidfinalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidfinalproject.databinding.FragmentMovieListBinding;

public class MovieListFragment extends Fragment {

    FragmentMovieListBinding binding;
    MovieRecyclerAdapter adapter;
    MovieListViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new MovieListViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMovieListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView reviewList = binding.listMovieRV;
        reviewList.setHasFixedSize(true);
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MovieRecyclerAdapter();
        binding.listMovieRV.setAdapter(adapter);

        adapter.setOnItemClickListener(new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String movieId = viewModel.getMovieByPos(pos).getMovieId();
                Navigation.findNavController(view).navigate(MoviesFragmentDirections.actionMoviesFragmentToMovieReviewFragment(movieId));
            }

        });

        reviewList.setAdapter(adapter);

        // Get all the reviews
        viewModel.getData();

        return view;
    }


}