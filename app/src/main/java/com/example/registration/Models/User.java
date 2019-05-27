package com.example.registration.Models;

public class User{
    private String username, uid, profileImageUrl, email;

    public User(String email, String uid, String profileImageUrl, String username) {
        this.username = username;
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
    }
    public User() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
