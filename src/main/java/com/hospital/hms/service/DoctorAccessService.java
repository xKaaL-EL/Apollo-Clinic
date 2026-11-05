package com.hospital.hms.service;

import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Doctor;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.repository.DoctorRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorAccessService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorAccessService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Doctor doctorForUser(Long userId) {
        return doctorRepository.findByAccountId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor account is not linked to a doctor profile"));
    }

    public boolean ownsAppointment(Long userId, Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(a -> a.getDoctor().getAccount() != null && userId != null
                        && userId.equals(a.getDoctor().getAccount().getId()))
                .orElse(false);
    }

    public boolean canAccessPatient(Long userId, Long patientId) {
        return appointmentRepository.findAllByPatientId(patientId).stream()
                .anyMatch(a -> a.getDoctor().getAccount() != null
                        && userId.equals(a.getDoctor().getAccount().getId()));
    }
}
