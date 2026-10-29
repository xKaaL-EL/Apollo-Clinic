package com.hospital.hms.repository;

import com.hospital.hms.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByOrderByIdAsc();
    List<Doctor> findAllByOrderByNameAsc();
    List<Doctor> findAllByOrderByAgeAsc();
    List<Doctor> findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(String name, String specialization);
}
