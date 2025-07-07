package com.example.praventa.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class User {
    private int id;
    private String username;
    private String password;
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

    @XmlElement
    public int getId() {
        return id;
    }

    @XmlElement
    public String getUsername() {
        return username;
    }

    @XmlElement
    public String getPassword() {
        return password;
    }

    @XmlElement
    public String getEmail() {
        return email;
    }

    @XmlElement
    public String getRole() {
        return role;
    }

    @XmlElement(name = "profile_picture")
    public String getProfilePicture() {
        return profilePicture;
    }

    @XmlElement(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
