package com.example.androidfinalproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import com.example.androidfinalproject.databinding.FragmentSetReviewBinding;
import com.example.androidfinalproject.databinding.FragmentUserDetailsBinding;
import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.ModelFirebase;
import com.example.androidfinalproject.model.Review;
import com.example.androidfinalproject.model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class setReviewFragment extends Fragment {

    ActivityResultLauncher<Uri> mTakeImage;
    ActivityResultLauncher<Void> cameraLauncer;
    Uri movieImageUri;

    String movieId = "";
    String documentId = null;
    Boolean isEsit;
    ActivityResultLauncher<String> mGetContent;

    FragmentSetReviewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetReviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        movieId = setReviewFragmentArgs.fromBundle(getArguments()).getMovieId();
        documentId = setReviewFragmentArgs.fromBundle(getArguments()).getDocumentId();
        isEsit = setReviewFragmentArgs.fromBundle(getArguments()).getIsEdit();

        if (documentId != null) {
            if (!isEsit) {
                binding.post.setVisibility(View.GONE);
                binding.cancel.setVisibility(View.GONE);
                binding.openPhotos.setVisibility(View.GONE);
                binding.openCamera.setVisibility(View.GONE);
                binding.review.setFocusable(false);
            }

            Review review = Model.instance().getReviewById(documentId);
            binding.review.setText(review.getReviewDesc());
            Picasso.get().load(review.getMovieImageUrl()).into(binding.reviewImg);
        }

        binding.openCamera.setOnClickListener(v -> {
            cameraLauncer.launch(null);
        });
        binding.openPhotos.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });

        binding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable) binding.reviewImg.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                Review review = new Review("", Model.instance().getUserEmail(), movieId, binding.review.getText().toString(), "", false);

                Model.instance().saveMovieImage(bitmap, Model.instance().getUserEmail() + movieId, new ModelFirebase.SaveImageListener() {
                    @Override
                    public void onComplete(String url) {
                        review.setMovieImageUrl(url);

                        Model.instance().setReview(review, "", new ModelFirebase.SetReviewListener() {
                            @Override
                            public void onComplete() {
                                Toast.makeText(MyApplication.getContext(), "Review was successfully created", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getView()).popBackStack();
                            };

                        });
                    }
                });
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