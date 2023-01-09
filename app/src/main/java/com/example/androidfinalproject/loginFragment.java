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

public class loginFragment extends Fragment {

    EditText username;
    EditText password;
    Button confirmBtn;
    TextView toRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        username = (EditText) view.findViewById(R.id.login_username_edittxt);
        password = (EditText) view.findViewById(R.id.login_password_edittxt);
        confirmBtn = view.findViewById(R.id.login_login_btn);
        toRegister = view.findViewById(R.id.login_signup_txtview);


        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registrationFragment);
            }
        });

        return view;
    }
}