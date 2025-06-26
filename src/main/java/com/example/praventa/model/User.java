package com.example.praventa.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String role;
    private String profilePicture;
    private String phoneNumber;

    public User() {
    }

    public User(int id, String username, String email, String role, String profilePicture, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getProfilePicture() { return profilePicture; }
    public String getPhoneNumber() { return phoneNumber; }

    // Setters (agar bisa di-update)
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
