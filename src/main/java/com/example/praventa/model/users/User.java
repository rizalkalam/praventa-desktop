package com.example.praventa.model.users;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD) // ✅ Solusi utama
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;

    @XmlElement(name = "profile_picture")
    private String profilePicture;

    @XmlElement(name = "phone_number")
    private String phoneNumber;

    @XmlElement(name = "personal_data")
    private PersonalData personalData;

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

    // Getter dan Setter (boleh dibiarkan, JAXB akan abaikan)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public PersonalData getPersonalData() { return personalData; }
    public void setPersonalData(PersonalData personalData) { this.personalData = personalData; }
}
