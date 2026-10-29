package com.hospital.hms.repository;

import com.hospital.hms.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByOrderByIdAsc();
    List<Patient> findAllByOrderByNameAsc();
    List<Patient> findAllByOrderByAgeAsc();
    List<Patient> findByNameContainingIgnoreCaseOrAilmentContainingIgnoreCase(String name, String ailment);
}
