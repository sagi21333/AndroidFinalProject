package com.example.androidfinalproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfinalproject.databinding.FragmentRegistrationBinding;
import com.example.androidfinalproject.databinding.FragmentUserDetailsBinding;
import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.ModelFirebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class registrationFragment extends Fragment {

    ActivityResultLauncher<Uri> mTakeImage;
    ActivityResultLauncher<Void> cameraLauncer;
    Uri movieImageUri;

    FragmentRegistrationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sign Up");

        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.cameraImg.setOnClickListener(v -> {
            cameraLauncer.launch(null);
        });

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnableScreen(false);
                String userEmail = binding.emailTxt.getText().toString().toLowerCase(Locale.ROOT);
                Model.instance().register(userEmail,
                        binding.passwordTxt.getText().toString(),
                        new ModelFirebase.Register() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) binding.userImg.getDrawable()).getBitmap();
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                                Model.instance().saveImageAvatar(bitmap, userEmail, new ModelFirebase.SaveImageListener() {
                                    @Override
                                    public void onComplete(String url) {
                                        Model.instance().setUserDetails(userEmail,
                                                binding.firstnameTxt.getText().toString(),
                                                binding.lastnameTxt.getText().toString(),
                                                url,
                                                new ModelFirebase.RegisterDetails() {
                                                    @Override
                                                    public void onSuccess() {
                                                        Toast.makeText(MyApplication.getContext(), "User was created successfully", Toast.LENGTH_SHORT).show();
                                                        Navigation.findNavController(getView()).navigate(R.id.action_registrationFragment_to_moviesFragment);
                                                    }
                                                });
                                    }
                                });
                            }

                            @Override
                            public void onFailed(String failReason) {
                                Toast.makeText(MyApplication.getContext(), failReason, Toast.LENGTH_SHORT).show();
                                setEnableScreen(true);
                                return;
                            }
                        });
            }
        });

        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).popBackStack();
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

        return view;
    }

    private void setEnableScreen(boolean enableScreen) {
        binding.userImg.setEnabled(enableScreen);
        binding.cameraImg.setEnabled(enableScreen);
        binding.firstnameTxt.setEnabled(enableScreen);
        binding.lastnameTxt.setEnabled(enableScreen);
        binding.emailTxt.setEnabled(enableScreen);
        binding.passwordTxt.setEnabled(enableScreen);
        binding.confirmpasswordTxt.setEnabled(enableScreen);
        binding.signupBtn.setEnabled(enableScreen);
        binding.signinBtn.setEnabled(enableScreen);
    }
}
