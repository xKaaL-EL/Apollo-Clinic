package com.hospital.hms.dto;

import com.hospital.hms.model.enums.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PatientDto {
    private Long id;

    @NotBlank(message = "Patient name is required")
    private String name;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age cannot be negative")
    private Double age;

    @NotBlank(message = "Ailment details are required")
    private String ailment;

    public PatientDto() {}

    public PatientDto(Long id, String name, Gender gender, Double age, String ailment) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.ailment = ailment;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public Double getAge() { return age; }
    public void setAge(Double age) { this.age = age; }

    public String getAilment() { return ailment; }
    public void setAilment(String ailment) { this.ailment = ailment; }
}
