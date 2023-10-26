package com.iic.lab.RealEstate.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name is required.")
    @NotNull
    @NotEmpty
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Invalid email format.")
    private String email;

    @Column(name = "date_of_birth", nullable = false)
    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateOfBirth;

    @Column(name = "password", nullable = false)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*0-9]).{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one special character (!@#$%^&*), and one number")
    private String password;

    @Column(name = "profile_photo_url")
    // ~~ MUST ADD A VALIDATOR HERE ~~
    private String profilePhotoUrl;

    @Column(name = "description")
    @Size(max = 255, message = "Description must not exceed 255 characters.")
    private String description;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RealEstateListing> listings = new ArrayList<>();

    public User(String firstName, String lastName, String email, LocalDate dateOfBirth, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, LocalDate dateOfBirth, String password, String profilePhotoUrl, String description, List<RealEstateListing> listings) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.profilePhotoUrl = profilePhotoUrl;
        this.description = description;
        this.listings = listings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RealEstateListing> getListings() {
        return listings;
    }

    public void setListings(List<RealEstateListing> listings) {
        this.listings = listings;
    }
}
