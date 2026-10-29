package com.hospital.hms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FollowUpDto {
    private Long id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
    private String patientName;

    @NotBlank(message = "Follow-up date is required")
    private String nextDate;

    private String notes;

    public FollowUpDto() {}

    public FollowUpDto(Long id, Long patientId, String patientName, String nextDate, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.nextDate = nextDate;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getNextDate() { return nextDate; }
    public void setNextDate(String nextDate) { this.nextDate = nextDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
