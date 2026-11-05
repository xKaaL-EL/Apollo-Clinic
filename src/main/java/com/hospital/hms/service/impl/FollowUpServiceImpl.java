package com.hospital.hms.service.impl;

import com.hospital.hms.dto.FollowUpDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.FollowUp;
import com.hospital.hms.model.entity.Patient;
import com.hospital.hms.model.entity.Appointment;
import com.hospital.hms.repository.FollowUpRepository;
import com.hospital.hms.repository.PatientRepository;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.service.FollowUpService;
import com.hospital.hms.service.DoctorAccessService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowUpServiceImpl implements FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorAccessService doctorAccessService;

    @Autowired
    public FollowUpServiceImpl(FollowUpRepository followUpRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository, DoctorAccessService doctorAccessService) {
        this.followUpRepository = followUpRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorAccessService = doctorAccessService;
    }

    @Override
    public FollowUpDto createFollowUp(FollowUpDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));

        FollowUp followUp = new FollowUp();
        followUp.setPatient(patient);
        if (dto.getAppointmentId() != null) followUp.setAppointment(appointmentRepository.findById(dto.getAppointmentId()).orElseThrow(() -> new ResourceNotFoundException("Appointment not found")));
        followUp.setNextDate(dto.getNextDate());
        followUp.setNotes(dto.getNotes());

        FollowUp saved = followUpRepository.save(followUp);
        return mapToDto(saved);
    }

    @Override
    public FollowUpDto updateFollowUp(Long id, FollowUpDto dto) {
        FollowUp followUp = followUpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow-up not found with id: " + id));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));

        followUp.setPatient(patient);
        if (dto.getAppointmentId() != null) followUp.setAppointment(appointmentRepository.findById(dto.getAppointmentId()).orElseThrow(() -> new ResourceNotFoundException("Appointment not found")));
        followUp.setNextDate(dto.getNextDate());
        followUp.setNotes(dto.getNotes());

        FollowUp updated = followUpRepository.save(followUp);
        return mapToDto(updated);
    }

    @Override
    public void deleteFollowUp(Long id) {
        FollowUp followUp = followUpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow-up not found with id: " + id));
        followUpRepository.delete(followUp);
    }

    @Override
    public FollowUpDto getFollowUpById(Long id) {
        FollowUp followUp = followUpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow-up not found with id: " + id));
        return mapToDto(followUp);
    }

    @Override
    public List<FollowUpDto> getAllFollowUps(String sortBy) {
        List<FollowUp> followUps;
        if (sortBy == null) {
            sortBy = "ID";
        }
        switch (sortBy.toUpperCase()) {
            case "DATE":
                followUps = followUpRepository.findAllByOrderByNextDateAsc();
                break;
            default:
                followUps = followUpRepository.findAllByOrderByIdAsc();
                break;
        }
        return followUps.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<FollowUpDto> searchFollowUps(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllFollowUps("ID");
        }
        return followUpRepository.findByPatientNameContainingIgnoreCaseOrNotesContainingIgnoreCaseOrNextDateContainingIgnoreCase(query, query, query)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override public FollowUpDto createFollowUpForDoctor(FollowUpDto dto, Long userId) { validateAppointment(dto.getAppointmentId(), dto.getPatientId(), userId); return createFollowUp(dto); }
    @Override public FollowUpDto updateFollowUpForDoctor(Long id, FollowUpDto dto, Long userId) { FollowUp old = owned(id, userId); validateAppointment(dto.getAppointmentId(), dto.getPatientId(), userId); return updateFollowUp(id, dto); }
    @Override public void deleteFollowUpForDoctor(Long id, Long userId) { owned(id, userId); deleteFollowUp(id); }
    @Override public FollowUpDto getFollowUpForDoctor(Long id, Long userId) { owned(id, userId); return getFollowUpById(id); }
    @Override public List<FollowUpDto> getFollowUpsForDoctor(Long userId) { return followUpRepository.findAllByAppointmentDoctorAccountId(userId).stream().map(this::mapToDto).collect(Collectors.toList()); }

    private FollowUp owned(Long id, Long userId) { FollowUp f = followUpRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Follow-up not found with id: " + id)); if (f.getAppointment() == null || !doctorAccessService.ownsAppointment(userId, f.getAppointment().getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Follow-up is not assigned to this doctor"); return f; }
    private void validateAppointment(Long appointmentId, Long patientId, Long userId) { if (appointmentId == null || !doctorAccessService.ownsAppointment(userId, appointmentId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Doctors may use only their assigned appointments"); Appointment a = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResourceNotFoundException("Appointment not found")); if (!a.getPatient().getId().equals(patientId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Appointment does not belong to this patient"); }

    private FollowUpDto mapToDto(FollowUp followUp) {
        FollowUpDto dto = new FollowUpDto(
                followUp.getId(),
                followUp.getPatient().getId(),
                followUp.getPatient().getName(),
                followUp.getNextDate(),
                followUp.getNotes());
        if (followUp.getAppointment() != null) dto.setAppointmentId(followUp.getAppointment().getId());
        return dto;
    }
}
