package com.example.androidfinalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidfinalproject.databinding.FragmentMovieListBinding;
import com.example.androidfinalproject.databinding.FragmentReviewListBinding;
import com.example.androidfinalproject.model.Model;

public class ReviewListFragment extends Fragment {

    FragmentReviewListBinding binding;
    ReviewRecyclerAdapter adapter;
    ReviewListViewModel viewModel;
    Fragment parentFragment;
    int visibility;
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentFragment = getParentFragment();
        if(parentFragment instanceof MovieReviewFragment){
//            String movieId = MovieReviewFragmentArgs.fromBundle(getArguments()).getMovieId();
            if(getArguments()!=null) {
                String movieId = getArguments().getString("movieId");
                viewModel = new ReviewListViewModel(movieId);
            }
        } else {
            viewModel = new ReviewListViewModel(null);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReviewListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView reviewList = binding.listReviewRV;
        swipeRefresh = view.findViewById(R.id.listReviewSwipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance().refreshReviews());

        reviewList.setHasFixedSize(true);
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));

        if (parentFragment instanceof myReviewsFragment) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }

       adapter = new ReviewRecyclerAdapter(visibility);

        binding.listReviewRV.setAdapter(adapter);

        adapter.setOnItemClickListener(new ReviewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String documentId = viewModel.getReviewByPos(pos).getDocumentId();

                if (parentFragment instanceof MovieReviewFragment) {
                    Navigation.findNavController(view).navigate(MovieReviewFragmentDirections.actionMovieReviewFragmentToDisplayReviewFragment2(documentId));
                } else if (parentFragment instanceof myReviewsFragment) {
                    Navigation.findNavController(view).navigate(myReviewsFragmentDirections.actionMyReviewsFragmentToDisplayReviewFragment(documentId));
                }
            }

        });

        reviewList.setAdapter(adapter);

        Model.instance().getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            if (loadingState == Model.LoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }
        });

        // Get all the reviews
        ReviewListViewModel.getData().observe(getViewLifecycleOwner(), list -> refresh());

        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }
}