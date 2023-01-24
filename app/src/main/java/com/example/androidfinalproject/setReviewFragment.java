package com.example.androidfinalproject;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidfinalproject.databinding.FragmentSetReviewBinding;
import com.example.androidfinalproject.databinding.FragmentUserDetailsBinding;
import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.Review;

import java.io.IOException;
import java.util.Date;

public class setReviewFragment extends Fragment {

    ActivityResultLauncher<Uri> mTakeImage;
    ActivityResultLauncher<Void> cameraLauncer;
    Uri movieImageUri;

    ActivityResultLauncher<String> mGetContent;

    FragmentSetReviewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSetReviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.openCamera.setOnClickListener(v -> {
            cameraLauncer.launch(null);
        });
        binding.openPhotos.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });

        binding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Review review = new Review("", Model.getUserEmail(),"0", "testing", "", 0, new Date(), false);
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).popBackStack();
            }
        });

        cameraLauncer = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.reviewImg.setImageBitmap(result);
                }
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                // Handle the returned Uri
                if (uri == null) {
                    return;
                }

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    binding.reviewImg.setImageBitmap(bitmap);
                }
            }
        });

        mTakeImage = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result.booleanValue()) {
                    binding.reviewImg.setImageURI(movieImageUri);
                }
            }
        });

        return view;
    }
}