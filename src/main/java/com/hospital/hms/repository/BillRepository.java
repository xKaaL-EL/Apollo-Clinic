package com.hospital.hms.repository;

import com.hospital.hms.model.entity.Bill;
import com.hospital.hms.model.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByOrderByIdAsc();
    List<Bill> findAllByOrderByAmountAsc();
    List<Bill> findAllByOrderByStatusAsc();
    List<Bill> findAllByPatientId(Long patientId);
    List<Bill> findByPatientNameContainingIgnoreCase(String name);
}
