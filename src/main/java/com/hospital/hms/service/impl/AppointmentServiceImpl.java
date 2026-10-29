package com.hospital.hms.service.impl;

import com.hospital.hms.dto.AppointmentDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Appointment;
import com.hospital.hms.model.entity.Doctor;
import com.hospital.hms.model.entity.Patient;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.repository.DoctorRepository;
import com.hospital.hms.repository.PatientRepository;
import com.hospital.hms.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  PatientRepository patientRepository,
                                  DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public AppointmentDto createAppointment(AppointmentDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + dto.getDoctorId()));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDate(dto.getDate());
        appointment.setTime(dto.getTime());

        Appointment saved = appointmentRepository.save(appointment);
        return mapToDto(saved);
    }

    @Override
    public AppointmentDto updateAppointment(Long id, AppointmentDto dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + dto.getDoctorId()));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDate(dto.getDate());
        appointment.setTime(dto.getTime());

        Appointment updated = appointmentRepository.save(appointment);
        return mapToDto(updated);
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        appointmentRepository.delete(appointment);
    }

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        return mapToDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAllAppointments(String sortBy) {
        List<Appointment> appointments;
        if (sortBy == null) {
            sortBy = "ID";
        }
        switch (sortBy.toUpperCase()) {
            case "DATE":
                appointments = appointmentRepository.findAllByOrderByDateAsc();
                break;
            default:
                appointments = appointmentRepository.findAllByOrderByIdAsc();
                break;
        }
        return appointments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> searchAppointments(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllAppointments("ID");
        }
        return appointmentRepository.findByPatientNameContainingIgnoreCaseOrDoctorNameContainingIgnoreCaseOrDateContainingIgnoreCase(query, query, query)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        return new AppointmentDto(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getName(),
                appointment.getDate(),
                appointment.getTime()
        );
    }
}
