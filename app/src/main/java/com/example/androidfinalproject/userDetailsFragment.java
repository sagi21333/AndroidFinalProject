package com.example.androidfinalproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfinalproject.databinding.FragmentMovieReviewBinding;
import com.example.androidfinalproject.databinding.FragmentUserDetailsBinding;
import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.ModelFirebase;
import com.example.androidfinalproject.model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

public class userDetailsFragment extends Fragment {

    ActivityResultLauncher<Uri> mTakeImage;
    ActivityResultLauncher<Void> cameraLauncer;
    Uri movieImageUri;

    FragmentUserDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUserDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.openCamera.setOnClickListener(v -> {
            cameraLauncer.launch(null);
        });

        binding.newpasswordBox.setVisibility(View.GONE);
        binding.confirmnewpasswordBox.setVisibility(View.GONE);
        binding.cancel.setVisibility(View.GONE);
        binding.save.setVisibility(View.GONE);
        binding.cameraBox.setVisibility(View.GONE);

        Model.instance().getUserDetails(new ModelFirebase.GetUserDetailsListener() {
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
                binding.newpasswordBox.setVisibility(View.VISIBLE);
                binding.confirmnewpasswordBox.setVisibility(View.VISIBLE);
                binding.cancel.setVisibility(View.VISIBLE);
                binding.save.setVisibility(View.VISIBLE);
                binding.cameraBox.setVisibility(View.VISIBLE);

                binding.detailesEditImg.setVisibility(View.GONE);

                setEnable(true);
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.newpasswordBox.setVisibility(View.GONE);
                binding.confirmnewpasswordBox.setVisibility(View.GONE);
                binding.cancel.setVisibility(View.GONE);
                binding.save.setVisibility(View.GONE);
                binding.cameraBox.setVisibility(View.GONE);

                binding.detailesEditImg.setVisibility(View.VISIBLE);

                setEnable(false);
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = binding.newpasswordTxt.getText().toString();
                String confirmPassword = binding.confirmnewpasswordTxt.getText().toString();
                if (! password.equals("") || ! confirmPassword.equals("")) {
                    if (password.equals(confirmPassword)) {
                        if (Pattern.compile("^.{0,5}$").matcher(password).matches()) {
                            Toast.makeText(MyApplication.getContext(), "password is too short (at least 6)", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Model.instance().updatePassword(password, new ModelFirebase.UpdatePassword() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(MyApplication.getContext(), "User was created successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String failReason) {
                                    Log.d("My Profile", "Failed updating the password " + failReason);
                                    Toast.makeText(MyApplication.getContext(), "Failed updating the password", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        }
                    } else {
                        Toast.makeText(MyApplication.getContext(), "Password and Confirm password are different", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Bitmap bitmap = ((BitmapDrawable) binding.userImg.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                Model.instance().saveImageAvatar(bitmap, binding.emailTxt.getText().toString(), new ModelFirebase.SaveImageListener() {
                    @Override
                    public void onComplete(String url) {
                        Model.instance().setUserDetails(binding.emailTxt.getText().toString(),
                                binding.firstnameTxt.getText().toString(),
                                binding.lastnameTxt.getText().toString(),
                                url,
                                new ModelFirebase.RegisterDetails() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(MyApplication.getContext(), "User was updated successfully", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(getView()).popBackStack();
                                    }
                                });
                    }
                });
            }
        });

        cameraLauncer = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.userImg.setImageBitmap(result);
                }
            }
        });

        mTakeImage = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result.booleanValue()) {
                    binding.userImg.setImageURI(movieImageUri);
                }
            }
        });


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Profile");


        return view;
    }

    private void setEnable(boolean enableScreen) {
        binding.firstnameTxt.setEnabled(enableScreen);
        binding.lastnameTxt.setEnabled(enableScreen);
    }
}