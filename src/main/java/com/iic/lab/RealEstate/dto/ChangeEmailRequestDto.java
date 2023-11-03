package com.iic.lab.RealEstate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ChangeEmailRequestDto {

    @NotBlank(message = "Current email cannot be blank.")
    @Email(message = "Invalid email format for current email.")
    private String currentEmail;

    @NotBlank(message = "New email cannot be blank.")
    @Email(message = "Invalid email format for new email.")
    private String newEmail;

    @NotBlank(message = "Password cannot be blank.")
    private String password;

    public String getCurrentEmail() {
        return currentEmail;
    }

    public void setCurrentEmail(String currentEmail) {
        this.currentEmail = currentEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
