package com.hospital.hms.model.entity;

import com.hospital.hms.model.enums.Gender;
import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Double age;

    @Column(nullable = false)
    private String ailment;

    public Patient() {}

    public Patient(Long id, String name, Gender gender, Double age, String ailment) {
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
