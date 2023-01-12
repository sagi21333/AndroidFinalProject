package com.example.androidfinalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.androidfinalproject.model.Model;

public class MoviesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toMyReviews:
                Navigation.findNavController(getView()).navigate(R.id.action_moviesFragment_to_myReviewsFragment);
                return true;
            case R.id.toMyDetails:
                Navigation.findNavController(getView()).navigate(R.id.action_moviesFragment_to_userDetailsFragment);
                return true;
            case R.id.Logout:
                Model.instance.logout();
                Navigation.findNavController(getView()).popBackStack(R.id.loginFragment, true);
                Navigation.findNavController(getView()).navigate(R.id.loginFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}