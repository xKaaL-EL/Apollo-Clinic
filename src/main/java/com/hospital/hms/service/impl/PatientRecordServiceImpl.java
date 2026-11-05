package com.hospital.hms.service.impl;

import com.hospital.hms.dto.*;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Appointment;
import com.hospital.hms.model.entity.Prescription;
import com.hospital.hms.repository.*;
import com.hospital.hms.service.DoctorAccessService;
import com.hospital.hms.service.PatientRecordService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientRecordServiceImpl implements PatientRecordService {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final FollowUpRepository followUpRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final DoctorAccessService doctorAccessService;

    public PatientRecordServiceImpl(PatientRepository patientRepository, AppointmentRepository appointmentRepository,
                                    FollowUpRepository followUpRepository, PrescriptionRepository prescriptionRepository,
                                    DoctorAccessService doctorAccessService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.followUpRepository = followUpRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.doctorAccessService = doctorAccessService;
    }

    @Override
    public PatientRecordDto getRecord(Long patientId, Long userId) {
        return getRecord(patientId, userId, null);
    }

    @Override
    public PatientRecordDto getRecord(Long patientId, Long userId, Long appointmentId) {
        if (appointmentId != null) {
            Appointment context = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
            if (!context.getPatient().getId().equals(patientId) || !doctorAccessService.ownsAppointment(userId, appointmentId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Appointment is not assigned to this doctor");
            }
        } else if (!doctorAccessService.canAccessPatient(userId, patientId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Patient is not assigned to this doctor");
        }
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentRepository.findAllByPatientId(patientId).stream()
                .sorted(Comparator.comparing((Appointment a) -> date(a.getDate())).thenComparing(a -> a.getTime() == null ? "" : a.getTime()))
                .collect(Collectors.toList());

        List<PatientRecordDto.AppointmentHistory> history = appointments.stream().map(a -> {
            boolean past = !date(a.getDate()).isAfter(today);
            return new PatientRecordDto.AppointmentHistory(
                    a.getId(), a.getDate(), a.getTime(), a.getDoctor().getId(), a.getDoctor().getName(),
                    a.getDoctor().getSpecialization(), past ? patient.getAilment() : null, past);
        }).collect(Collectors.toList());

        List<FollowUpDto> followUps = followUpRepository.findAllByPatientId(patientId).stream()
                .map(f -> { FollowUpDto dto = new FollowUpDto(f.getId(), patientId, patient.getName(), f.getNextDate(), f.getNotes()); if (f.getAppointment() != null) { dto.setAppointmentId(f.getAppointment().getId()); dto.setEditable(doctorAccessService.ownsAppointment(userId, f.getAppointment().getId())); } return dto; })
                .collect(Collectors.toList());

        List<PatientRecordDto.PrescriptionHistory> prescriptions = prescriptionRepository.findAllByOrderByIdAsc().stream()
                .filter(p -> p.getAppointment().getPatient().getId().equals(patientId))
                .map(p -> new PatientRecordDto.PrescriptionHistory(
                        p.getId(), p.getAppointment().getId(), p.getPrescriptionDate() != null ? p.getPrescriptionDate().toString() : p.getAppointment().getDate(),
                        p.getAppointment().getDoctor().getName(), p.getMedicineDetails(), p.getDosage(), p.getPrescriptionDate(),
                        doctorAccessService.ownsAppointment(userId, p.getAppointment().getId())))
                .collect(Collectors.toList());
        List<PatientRecordDto.PrescriptionHistory> otherPrescriptions = prescriptions.stream()
                .filter(p -> !p.isEditable())
                .collect(Collectors.toList());
        List<PatientRecordDto.PrescriptionHistory> myPrescriptions = prescriptions.stream()
                .filter(PatientRecordDto.PrescriptionHistory::isEditable)
                .collect(Collectors.toList());

        PatientDto patientDto = new PatientDto(patient.getId(), patient.getName(), patient.getGender(), patient.getAge(), patient.getAilment());
        return new PatientRecordDto(patientDto, history, followUps, otherPrescriptions, myPrescriptions);
    }

    private LocalDate date(String value) {
        try { return LocalDate.parse(value); } catch (DateTimeParseException ex) { return LocalDate.MAX; }
    }

}
