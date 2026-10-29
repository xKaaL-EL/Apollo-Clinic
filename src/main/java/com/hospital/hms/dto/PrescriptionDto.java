package com.hospital.hms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PrescriptionDto {
    private Long id;

    @NotNull(message = "Appointment reference is required")
    private Long appointmentId;
    private String appointmentRef; // Describes the appointment (e.g. "Patient on Date")

    @NotBlank(message = "Medicine details are required")
    private String medicineDetails;

    @NotBlank(message = "Dosage instructions are required")
    private String dosage;

    public PrescriptionDto() {}

    public PrescriptionDto(Long id, Long appointmentId, String appointmentRef, String medicineDetails, String dosage) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.appointmentRef = appointmentRef;
        this.medicineDetails = medicineDetails;
        this.dosage = dosage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getAppointmentRef() { return appointmentRef; }
    public void setAppointmentRef(String appointmentRef) { this.appointmentRef = appointmentRef; }

    public String getMedicineDetails() { return medicineDetails; }
    public void setMedicineDetails(String medicineDetails) { this.medicineDetails = medicineDetails; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
}
