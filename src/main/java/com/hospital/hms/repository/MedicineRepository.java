package com.hospital.hms.repository;

import com.hospital.hms.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findAllByOrderByIdAsc();
    List<Medicine> findAllByOrderByNameAsc();
    List<Medicine> findAllByOrderByStockAsc();
    List<Medicine> findAllByOrderByPriceAsc();
    List<Medicine> findByNameContainingIgnoreCase(String name);
}
