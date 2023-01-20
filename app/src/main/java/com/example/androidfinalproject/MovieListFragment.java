package com.example.androidfinalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidfinalproject.databinding.FragmentMovieListBinding;
import com.example.androidfinalproject.model.Model;

public class MovieListFragment extends Fragment {

    FragmentMovieListBinding binding;
    MovieRecyclerAdapter adapter;
    MovieListViewModel viewModel;
    SwipeRefreshLayout swipeRefresh;

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

        swipeRefresh = binding.listMovieSwipeRefresh;
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshReviews());

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

        Model.instance.getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            if (loadingState == Model.LoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }
        });

        // Get all the reviews
        viewModel.getData();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Movies");
        return view;
    }
    private void refresh() {
        adapter.notifyDataSetChanged();
    }
}