package com.dtp.doctor_appointment_booking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

public class SignupRequest {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must not contain special characters and should only include letters and spaces.")
    @Size(min = 3, max = 50)
    private String fullName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 30)
    private String password;

    private Set<String> roles;

    public String getFullName() {
        return fullName;
    }

    public void setUsername(String username) {
        this.fullName = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public void setRole(Set<String> role) {
        this.roles = role;
    }
}
