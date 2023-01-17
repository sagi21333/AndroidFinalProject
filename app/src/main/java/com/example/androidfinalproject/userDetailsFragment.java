package com.example.androidfinalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidfinalproject.databinding.FragmentMovieReviewBinding;
import com.example.androidfinalproject.databinding.FragmentUserDetailsBinding;
import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.ModelFirebase;
import com.example.androidfinalproject.model.User;
import com.squareup.picasso.Picasso;

public class userDetailsFragment extends Fragment {

    FragmentUserDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUserDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Model.instance.getUserDetails(new ModelFirebase.GetUserDetailsListener() {
            @Override
            public void onComplete(User userDetails) {
                User myDetails = userDetails;
                if (!myDetails.getEmail().equals("")) {
                    binding.emailTxt.setText(myDetails.getEmail());
                    binding.firstnameTxt.setText(myDetails.getFirstName());
                    binding.lastnameTxt.setText(myDetails.getLastName());
                    if (myDetails.getUserImageUrl() != null &&
                            !myDetails.getUserImageUrl().equals("")) {
                        Picasso.get().load(myDetails.getUserImageUrl()).into(binding.userImg);
                    }
                }

//                setEnableScreen(true); // TODO
            }
        });

        binding.detailesEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            }
        });


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Profile");


        return view;
    }
}