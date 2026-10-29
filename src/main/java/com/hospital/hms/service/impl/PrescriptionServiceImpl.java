package com.hospital.hms.service.impl;

import com.hospital.hms.dto.PrescriptionDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Appointment;
import com.hospital.hms.model.entity.Prescription;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.repository.PrescriptionRepository;
import com.hospital.hms.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public PrescriptionDto createPrescription(PrescriptionDto dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + dto.getAppointmentId()));

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setMedicineDetails(dto.getMedicineDetails());
        prescription.setDosage(dto.getDosage());

        Prescription saved = prescriptionRepository.save(prescription);
        return mapToDto(saved);
    }

    @Override
    public PrescriptionDto updatePrescription(Long id, PrescriptionDto dto) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));

        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + dto.getAppointmentId()));

        prescription.setAppointment(appointment);
        prescription.setMedicineDetails(dto.getMedicineDetails());
        prescription.setDosage(dto.getDosage());

        Prescription updated = prescriptionRepository.save(prescription);
        return mapToDto(updated);
    }

    @Override
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        prescriptionRepository.delete(prescription);
    }

    @Override
    public PrescriptionDto getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        return mapToDto(prescription);
    }

    @Override
    public List<PrescriptionDto> getAllPrescriptions(String sortBy) {
        List<Prescription> prescriptions;
        if (sortBy == null) {
            sortBy = "ID";
        }
        switch (sortBy.toUpperCase()) {
            case "MEDICINE":
                prescriptions = prescriptionRepository.findAllByOrderByMedicineDetailsAsc();
                break;
            default:
                prescriptions = prescriptionRepository.findAllByOrderByIdAsc();
                break;
        }
        return prescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDto> searchPrescriptions(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllPrescriptions("ID");
        }
        return prescriptionRepository.findByMedicineDetailsContainingIgnoreCase(query)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private PrescriptionDto mapToDto(Prescription prescription) {
        String appointmentRef = "Appt ID: " + prescription.getAppointment().getId() + " - Patient: " + prescription.getAppointment().getPatient().getName() + " on " + prescription.getAppointment().getDate();
        return new PrescriptionDto(
                prescription.getId(),
                prescription.getAppointment().getId(),
                appointmentRef,
                prescription.getMedicineDetails(),
                prescription.getDosage()
        );
    }
}
