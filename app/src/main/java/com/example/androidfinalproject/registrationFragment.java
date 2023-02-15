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
import android.util.Log;
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
import java.util.regex.Pattern;

public class registrationFragment extends Fragment {

    ActivityResultLauncher<Uri> mTakeImage;
    ActivityResultLauncher<Void> cameraLauncer;
    Uri movieImageUri;

    FragmentRegistrationBinding binding;

    Boolean setImg = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.cameraImg.setOnClickListener(v -> {
            cameraLauncer.launch(null);
        });

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = binding.emailTxt.getText().toString().toLowerCase(Locale.ROOT);

                if (userEmail.equals("")) {
                    Toast.makeText(MyApplication.getContext(), "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Pattern.compile("^(.+)@(\\S+)$").matcher(userEmail).matches()) {
                    Toast.makeText(MyApplication.getContext(), "Bad email type", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = binding.passwordTxt.getText().toString();
                String confirmPassword = binding.confirmpasswordTxt.getText().toString();
                if (! password.equals("")) {
                    if (password.equals(confirmPassword)) {
                        if (Pattern.compile("^.{0,5}$").matcher(password).matches()) {
                            Toast.makeText(MyApplication.getContext(), "password is too short (at least 6)", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(MyApplication.getContext(), "Password and Confirm password are different", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(MyApplication.getContext(), "Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!setImg) {
                    Toast.makeText(MyApplication.getContext(), "You have to set an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                setEnableScreen(false);
                Model.instance().register(userEmail,
                        password,
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
                    setImg = true;
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
