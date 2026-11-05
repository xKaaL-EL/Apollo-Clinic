package com.hospital.hms.service.impl;

import com.hospital.hms.dto.PrescriptionDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Appointment;
import com.hospital.hms.model.entity.Prescription;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.repository.PrescriptionRepository;
import com.hospital.hms.service.PrescriptionService;
import com.hospital.hms.service.DoctorAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorAccessService doctorAccessService;

    @Autowired
    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository,
                                   DoctorAccessService doctorAccessService) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorAccessService = doctorAccessService;
    }

    @Override
    public PrescriptionDto createPrescription(PrescriptionDto dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + dto.getAppointmentId()));

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setMedicineDetails(dto.getMedicineDetails());
        prescription.setDosage(dto.getDosage());
        prescription.setPrescriptionDate(LocalDate.now());

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

    @Override
    public PrescriptionDto createPrescriptionForDoctor(PrescriptionDto dto, Long userId) {
        if (dto.getAppointmentId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment reference is required");
        }
        if (!doctorAccessService.ownsAppointment(userId, dto.getAppointmentId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Doctors may prescribe only for their assigned appointments");
        }
        return createPrescription(dto);
    }

    @Override
    public PrescriptionDto updatePrescriptionForDoctor(Long id, PrescriptionDto dto, Long userId) {
        Prescription existing = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        if (!doctorAccessService.ownsAppointment(userId, existing.getAppointment().getId())
                || !existing.getAppointment().getDoctor().getAccount().getId().equals(userId)
                || !doctorAccessService.ownsAppointment(userId, dto.getAppointmentId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Doctors may update only their assigned prescriptions");
        }
        return updatePrescription(id, dto);
    }

    @Override
    public void deletePrescriptionForDoctor(Long id, Long userId) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        if (!doctorAccessService.ownsAppointment(userId, prescription.getAppointment().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Doctors may delete only their assigned prescriptions");
        }
        deletePrescription(id);
    }

    @Override
    public boolean canDoctorAccessPrescription(Long id, Long userId) {
        return prescriptionRepository.findById(id)
                .map(p -> doctorAccessService.canAccessPatient(userId, p.getAppointment().getPatient().getId()))
                .orElse(false);
    }

    @Override
    public List<PrescriptionDto> getPrescriptionsForDoctorUser(Long userId) {
        return prescriptionRepository.findAll().stream()
                .filter(p -> doctorAccessService.canAccessPatient(userId, p.getAppointment().getPatient().getId()))
                .map(p -> {
                    PrescriptionDto dto = mapToDto(p);
                    dto.setEditable(doctorAccessService.ownsAppointment(userId, p.getAppointment().getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDto> searchPrescriptionsForDoctorUser(String query, Long userId) {
        String normalized = query == null ? "" : query.trim().toLowerCase();
        return getPrescriptionsForDoctorUser(userId).stream()
                .filter(p -> normalized.isEmpty()
                        || p.getMedicineDetails().toLowerCase().contains(normalized)
                        || p.getAppointmentRef().toLowerCase().contains(normalized))
                .collect(Collectors.toList());
    }

    private PrescriptionDto mapToDto(Prescription prescription) {
        String appointmentRef = "Appt ID: " + prescription.getAppointment().getId() + " - Patient: " + prescription.getAppointment().getPatient().getName() + " on " + prescription.getAppointment().getDate();
        PrescriptionDto dto = new PrescriptionDto(
                prescription.getId(),
                prescription.getAppointment().getId(),
                appointmentRef,
                prescription.getMedicineDetails(),
                prescription.getDosage()
        );
        dto.setPrescriptionDate(prescription.getPrescriptionDate());
        return dto;
    }
}
