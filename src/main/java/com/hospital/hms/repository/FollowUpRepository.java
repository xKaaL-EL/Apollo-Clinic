package com.hospital.hms.repository;

import com.hospital.hms.model.entity.FollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    List<FollowUp> findAllByOrderByIdAsc();
    List<FollowUp> findAllByOrderByNextDateAsc();
    List<FollowUp> findAllByPatientId(Long patientId);
    List<FollowUp> findByPatientNameContainingIgnoreCaseOrNotesContainingIgnoreCaseOrNextDateContainingIgnoreCase(String name, String notes, String nextDate);
}
