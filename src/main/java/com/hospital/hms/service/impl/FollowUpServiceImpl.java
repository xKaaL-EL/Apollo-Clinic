package com.hospital.hms.service.impl;

import com.hospital.hms.dto.FollowUpDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.FollowUp;
import com.hospital.hms.model.entity.Patient;
import com.hospital.hms.repository.FollowUpRepository;
import com.hospital.hms.repository.PatientRepository;
import com.hospital.hms.service.FollowUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowUpServiceImpl implements FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public FollowUpServiceImpl(FollowUpRepository followUpRepository, PatientRepository patientRepository) {
        this.followUpRepository = followUpRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public FollowUpDto createFollowUp(FollowUpDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));

        FollowUp followUp = new FollowUp();
        followUp.setPatient(patient);
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

    private FollowUpDto mapToDto(FollowUp followUp) {
        return new FollowUpDto(
                followUp.getId(),
                followUp.getPatient().getId(),
                followUp.getPatient().getName(),
                followUp.getNextDate(),
                followUp.getNotes()
        );
    }
}
