package com.hospital.hms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DoctorProfileUpdateDto {
    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username is too long")
    private String username;

    private String password;
    private String confirmPassword;

    @NotBlank(message = "Specialization is required")
    @Size(max = 150, message = "Specialization is too long")
    private String specialization;

    @NotBlank(message = "Contact number is required")
    @Size(max = 50, message = "Contact number is too long")
    private String contact;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}
