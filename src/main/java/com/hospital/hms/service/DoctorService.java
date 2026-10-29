package com.hospital.hms.service;

import com.hospital.hms.dto.DoctorDto;
import java.util.List;

public interface DoctorService {
    DoctorDto createDoctor(DoctorDto doctorDto);
    DoctorDto updateDoctor(Long id, DoctorDto doctorDto);
    void deleteDoctor(Long id);
    DoctorDto getDoctorById(Long id);
    List<DoctorDto> getAllDoctors(String sortBy);
    List<DoctorDto> searchDoctors(String query);
}
