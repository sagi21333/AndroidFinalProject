package com.example.androidfinalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidfinalproject.databinding.FragmentMovieListBinding;
import com.example.androidfinalproject.model.Model;

public class MovieListFragment extends Fragment {

    FragmentMovieListBinding binding;
    MovieAdapter adapter;
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

        RecyclerView reviewList = view.findViewById(R.id.listMovieRV);
        reviewList.setHasFixedSize(true);
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MovieAdapter();
        binding.listMovieRV.setAdapter(adapter);

        adapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String documentId = viewModel.getMovieByPos(pos).getMovieId();

//                MovieListFragmentDirections.ActionStudentsListFragmentToBlueFragment action = MovieListFragmentDirections.action_moviesFragment_to_movieReviewFragment(documentId);
//                Navigation.findNavController(view).navigate(action);
            }

        });

        reviewList.setAdapter(adapter);

        // Get all the reviews
        viewModel.getData();

        return view;
    }


}