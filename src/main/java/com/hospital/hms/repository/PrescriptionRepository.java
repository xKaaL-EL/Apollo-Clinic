package com.hospital.hms.repository;

import com.hospital.hms.model.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findAllByOrderByIdAsc();
    List<Prescription> findAllByOrderByMedicineDetailsAsc();
    List<Prescription> findAllByAppointmentId(Long appointmentId);
    List<Prescription> findByMedicineDetailsContainingIgnoreCase(String query);
}
