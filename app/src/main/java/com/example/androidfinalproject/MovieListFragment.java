package com.example.androidfinalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidfinalproject.databinding.FragmentMovieListBinding;
import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.Movie;
import com.example.androidfinalproject.model.MovieModel;

import java.util.LinkedList;
import java.util.List;

public class MovieListFragment extends Fragment {

    FragmentMovieListBinding binding;
    MovieRecyclerAdapter adapter;
    List<Movie> data = new LinkedList<>();
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMovieListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        swipeRefresh = binding.listMovieSwipeRefresh;
        swipeRefresh.setOnRefreshListener(() -> MovieModel.instance.refreshMovies());

        RecyclerView movieList = binding.listMovieRV;
        movieList.setHasFixedSize(true);
        movieList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MovieRecyclerAdapter(getLayoutInflater(),data);
        binding.listMovieRV.setAdapter(adapter);

        LiveData<List<Movie>> movies = MovieModel.instance.loadMovies();
        movies.observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
        });


        adapter.setOnItemClickListener(new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String movieId = movies.getValue().get(pos).getId();
                Navigation.findNavController(view).navigate(MoviesFragmentDirections.actionMoviesFragmentToMovieReviewFragment(movieId));
            }

        });

        movieList.setAdapter(adapter);

//        Model.instance().getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
//            if (loadingState == Model.LoadingState.loading) {
//                swipeRefresh.setRefreshing(true);
//            } else {
//                swipeRefresh.setRefreshing(false);
//            }
//        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Movies");
        return view;
    }
    private void refresh() {
        adapter.notifyDataSetChanged();
    }
}