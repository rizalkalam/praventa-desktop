package com.example.praventa.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String role;
    private String profilePicture;

    public User(int id, String username, String email, String role, String profilePicture) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getProfilePicture() { return profilePicture; }
}
