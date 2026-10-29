package com.hospital.hms.service.impl;

import com.hospital.hms.dto.DoctorDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Doctor;
import com.hospital.hms.repository.DoctorRepository;
import com.hospital.hms.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        Doctor doctor = mapToEntity(doctorDto);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return mapToDto(savedDoctor);
    }

    @Override
    public DoctorDto updateDoctor(Long id, DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        doctor.setName(doctorDto.getName());
        doctor.setGender(doctorDto.getGender());
        doctor.setAge(doctorDto.getAge());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setContact(doctorDto.getContact());

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return mapToDto(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        doctorRepository.delete(doctor);
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return mapToDto(doctor);
    }

    @Override
    public List<DoctorDto> getAllDoctors(String sortBy) {
        List<Doctor> doctors;
        if (sortBy == null) {
            sortBy = "ID";
        }
        switch (sortBy.toUpperCase()) {
            case "NAME":
                doctors = doctorRepository.findAllByOrderByNameAsc();
                break;
            case "AGE":
                doctors = doctorRepository.findAllByOrderByAgeAsc();
                break;
            default:
                doctors = doctorRepository.findAllByOrderByIdAsc();
                break;
        }
        return doctors.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<DoctorDto> searchDoctors(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllDoctors("ID");
        }
        return doctorRepository.findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(query, query)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private DoctorDto mapToDto(Doctor doctor) {
        return new DoctorDto(
                doctor.getId(),
                doctor.getName(),
                doctor.getGender(),
                doctor.getAge(),
                doctor.getSpecialization(),
                doctor.getContact()
        );
    }

    private Doctor mapToEntity(DoctorDto doctorDto) {
        return new Doctor(
                doctorDto.getId(),
                doctorDto.getName(),
                doctorDto.getGender(),
                doctorDto.getAge(),
                doctorDto.getSpecialization(),
                doctorDto.getContact()
        );
    }
}
