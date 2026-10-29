package com.hospital.hms.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "followups")
public class FollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "next_date", nullable = false)
    private String nextDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public FollowUp() {}

    public FollowUp(Long id, Patient patient, String nextDate, String notes) {
        this.id = id;
        this.patient = patient;
        this.nextDate = nextDate;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getNextDate() { return nextDate; }
    public void setNextDate(String nextDate) { this.nextDate = nextDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
