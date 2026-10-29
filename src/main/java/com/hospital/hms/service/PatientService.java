package com.hospital.hms.service;

import com.hospital.hms.dto.PatientDto;
import java.util.List;

public interface PatientService {
    PatientDto createPatient(PatientDto patientDto);
    PatientDto updatePatient(Long id, PatientDto patientDto);
    void deletePatient(Long id);
    PatientDto getPatientById(Long id);
    List<PatientDto> getAllPatients(String sortBy);
    List<PatientDto> searchPatients(String query);
}
