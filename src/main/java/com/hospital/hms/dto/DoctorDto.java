package com.hospital.hms.dto;

import com.hospital.hms.model.enums.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorDto {
    private Long id;

    @NotBlank(message = "Doctor name is required")
    private String name;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    private Double age;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotBlank(message = "Contact information is required")
    private String contact;

    private String username;
    private String password;
    private String confirmPassword;

    public DoctorDto() {}

    public DoctorDto(Long id, String name, Gender gender, Double age, String specialization, String contact) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.specialization = specialization;
        this.contact = contact;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public Double getAge() { return age; }
    public void setAge(Double age) { this.age = age; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
