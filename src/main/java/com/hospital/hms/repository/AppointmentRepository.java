package com.hospital.hms.repository;

import com.hospital.hms.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByOrderByIdAsc();
    List<Appointment> findAllByOrderByDateAsc();
    List<Appointment> findAllByPatientId(Long patientId);
    List<Appointment> findAllByDoctorId(Long doctorId);
    List<Appointment> findByPatientNameContainingIgnoreCaseOrDoctorNameContainingIgnoreCaseOrDateContainingIgnoreCase(String pName, String dName, String date);
}
