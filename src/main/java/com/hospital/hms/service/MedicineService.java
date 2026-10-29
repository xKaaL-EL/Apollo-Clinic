package com.hospital.hms.service;

import com.hospital.hms.dto.MedicineDto;
import java.util.List;

public interface MedicineService {
    MedicineDto createMedicine(MedicineDto medicineDto);
    MedicineDto updateMedicine(Long id, MedicineDto medicineDto);
    void deleteMedicine(Long id);
    MedicineDto getMedicineById(Long id);
    List<MedicineDto> getAllMedicines(String sortBy);
    List<MedicineDto> searchMedicines(String query);
}
