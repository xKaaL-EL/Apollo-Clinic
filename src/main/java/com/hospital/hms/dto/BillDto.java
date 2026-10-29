package com.hospital.hms.dto;

import com.hospital.hms.model.enums.BillStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class BillDto {
    private Long id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
    private String patientName;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount cannot be negative")
    private Double amount;

    @NotNull(message = "Bill status is required")
    private BillStatus status;

    public BillDto() {}

    public BillDto(Long id, Long patientId, String patientName, Double amount, BillStatus status) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
}
