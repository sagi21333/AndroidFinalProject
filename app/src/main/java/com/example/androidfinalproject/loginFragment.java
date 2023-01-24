package com.example.androidfinalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfinalproject.databinding.FragmentLoginBinding;
import com.example.androidfinalproject.databinding.FragmentUserDetailsBinding;
import com.example.androidfinalproject.model.Model;
import com.example.androidfinalproject.model.ModelFirebase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginFragment extends Fragment {

    EditText email;
    EditText password;
    Button confirmBtn;
    TextView toRegister;

    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        email = binding.emailTxt;
        password = binding.passwordTxt;
        confirmBtn = binding.login;
        toRegister = binding.signup;

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                if (emailString.equals("") ||
                        passwordString.equals("")) {
                    Toast.makeText(MyApplication.getContext(), "email and password are required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Pattern.compile("^(.+)@(\\S+)$").matcher(emailString).matches()) {
                    Toast.makeText(MyApplication.getContext(), "not an email type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Pattern.compile("^.{0,5}$").matcher(passwordString).matches()) {
                    Toast.makeText(MyApplication.getContext(), "password is too short (at least 6)", Toast.LENGTH_SHORT).show();
                    return;
                }
                Model.instance().signIn(emailString, passwordString, new ModelFirebase.SignIn() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MyApplication.getContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_moviesFragment2);
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(MyApplication.getContext(), "Please check your credentials details", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registrationFragment);
            }
        });

        return view;
    }
}
