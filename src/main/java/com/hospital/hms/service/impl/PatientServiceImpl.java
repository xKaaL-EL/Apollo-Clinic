package com.hospital.hms.service.impl;

import com.hospital.hms.dto.PatientDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Patient;
import com.hospital.hms.repository.PatientRepository;
import com.hospital.hms.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientDto createPatient(PatientDto patientDto) {
        Patient patient = mapToEntity(patientDto);
        Patient savedPatient = patientRepository.save(patient);
        return mapToDto(savedPatient);
    }

    @Override
    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        patient.setName(patientDto.getName());
        patient.setGender(patientDto.getGender());
        patient.setAge(patientDto.getAge());
        patient.setAilment(patientDto.getAilment());

        Patient updatedPatient = patientRepository.save(patient);
        return mapToDto(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    @Override
    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return mapToDto(patient);
    }

    @Override
    public List<PatientDto> getAllPatients(String sortBy) {
        List<Patient> patients;
        if (sortBy == null) {
            sortBy = "ID";
        }
        switch (sortBy.toUpperCase()) {
            case "NAME":
                patients = patientRepository.findAllByOrderByNameAsc();
                break;
            case "AGE":
                patients = patientRepository.findAllByOrderByAgeAsc();
                break;
            default:
                patients = patientRepository.findAllByOrderByIdAsc();
                break;
        }
        return patients.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<PatientDto> searchPatients(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllPatients("ID");
        }
        return patientRepository.findByNameContainingIgnoreCaseOrAilmentContainingIgnoreCase(query, query)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private PatientDto mapToDto(Patient patient) {
        return new PatientDto(
                patient.getId(),
                patient.getName(),
                patient.getGender(),
                patient.getAge(),
                patient.getAilment()
        );
    }

    private Patient mapToEntity(PatientDto patientDto) {
        return new Patient(
                patientDto.getId(),
                patientDto.getName(),
                patientDto.getGender(),
                patientDto.getAge(),
                patientDto.getAilment()
        );
    }
}
