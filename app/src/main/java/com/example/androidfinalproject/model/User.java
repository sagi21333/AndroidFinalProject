package com.example.androidfinalproject.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.util.Date;
import java.util.Map;

@Entity
public class User {
    public static final String COLLECTION_NAME = "users";

    @PrimaryKey
    @NonNull
    String email;
    String firstName;
    String lastName;
    String userImageUrl;

    public User() {
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.userImageUrl = "";
    }

    public User(String email, String firstName, String lastName, String userImageUrl) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userImageUrl = userImageUrl;
    }

    public static User fromJSON(Map<String, Object> json, String emailId) {
        String firstName = (String) json.get("firstName");
        String lastName = (String) json.get("lastName");
        String userImageUrl = (String) json.get("imageUrl");

        User userDetails = new User(emailId, firstName, lastName, userImageUrl);
        return userDetails;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }
}
