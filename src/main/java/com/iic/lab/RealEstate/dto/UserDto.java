package com.iic.lab.RealEstate.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Date;

public class UserDto {
    @NotBlank(message = "First name is required.")
    @NotNull
    @NotEmpty
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Email(message = "Invalid email format.")
    private String email;

    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*0-9]).{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one special character (!@#$%^&*), and one number")
    private String password;

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

    public UserDto() {
    }

    public UserDto(String firstName, String lastName, String email, LocalDate dateOfBirth, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }

}
