package com.hospital.hms.service;

import com.hospital.hms.dto.PrescriptionDto;
import java.util.List;

public interface PrescriptionService {
    PrescriptionDto createPrescription(PrescriptionDto prescriptionDto);
    PrescriptionDto updatePrescription(Long id, PrescriptionDto prescriptionDto);
    void deletePrescription(Long id);
    PrescriptionDto getPrescriptionById(Long id);
    List<PrescriptionDto> getAllPrescriptions(String sortBy);
    List<PrescriptionDto> searchPrescriptions(String query);
}
